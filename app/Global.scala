import models._
import com.mongodb.casbah.commons.conversions.scala._ 
import org.scala_tools.time.Imports._
import com.edropple.velvetrope._
import com.edropple.velvetrope.user.RoleOwner
import com.edropple.velvetrope.user.roles.Role
import play.api.mvc.{Result, Request, RequestHeader}
import play.api.mvc.Results._
import play.api._


object Global extends GlobalSettings with VelvetropeGlobal {
  override def onStart(app: Application) {
	RegisterJodaTimeConversionHelpers()
    //val password = User.encryptor.encryptPassword("secret")  
    //User.add(User("Magnus", password, Set(Admin.name)))
        
  }

   /**
   * Retrieve the connected user email.
   */
   private def username(request: RequestHeader) = request.session.get("username")

  /**
     * Provides Velvetrope with the necessary logic to find the user from whatever
     * stateful data is available. This method is always run inside of an Akka.future
     * block.
     *
     * @param request The request object for this request
     * @return Some[RoleOwner] if the request contains user authentication information, None otherwise
     */
    def getRoleOwner[A](request: Request[A]): Option[RoleOwner] = {
        val userId = username(request)
        userId.map( User.find(_) ).getOrElse(None)

    }

    /**
     * Called when a visitor attempts to access an access-controlled resource without
     * having a role (via getRoleOwner).
     * @param request The visitor's request
     * @return a Result that should be presented to the visitor.
     */
    def onAuthenticationFailure[A](request: Request[A]): Result = Forbidden

    /**
     * Called when a user attempts to access an access-controlled resource without
     * having the correct role.
     * @param user the user accessing the resource
     * @param request the request of the resource access
     * @param missingRoles the missing roles that prevent the user from accessing the resource
     * @return a Result that should be presented to the visitor.
     */
    def onAuthorizationFailure[A](user: Option[RoleOwner], request: Request[A], missingRoles: Seq[Role]): Result = Unauthorized
}
