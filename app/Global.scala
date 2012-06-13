import models._
import com.mongodb.casbah.commons.conversions.scala._ 
import org.scala_tools.time.Imports._
import com.edropple.velvetrope._
import com.edropple.velvetrope.user.RoleOwner
import com.edropple.velvetrope.user.roles.Role
import play.api.mvc.{Result, Request, RequestHeader}
import play.api.mvc.Results._
import play.api._
import play.api.mvc._
import play.api.data._
import views._
import controllers._


object Global extends GlobalSettings with VelvetropeGlobal {
  override def onStart(app: Application) {
	RegisterJodaTimeConversionHelpers()
    val password = User.encryptor.encryptPassword("secret")  
    User.add(User("magnus", password, Set(Admin.name)))
        
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
        userId.map( User.findRoleOwner(_) )
    }

    /**
     * Called when a visitor attempts to access an access-controlled resource without
     * having a role (via getRoleOwner).
     * @param request The visitor's request
     * @return a Result that should be presented to the visitor.
     */
    def onAuthenticationFailure[A](request: Request[A]): Result = Redirect(routes.Application.login).flashing("failure" -> "authentication.failed")

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
