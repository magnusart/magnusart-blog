package models

import com.novus.salat._
import salatctx.salatctx._
import com.novus.salat.annotations._
import com.novus.salat.dao._
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoConnection
import com.mongodb.casbah.commons.MongoDBObject
import org.scala_tools.time.Imports._
import models._
import com.edropple.velvetrope.user.RoleOwner
import com.edropple.velvetrope.user.roles.Role
import org.jasypt.util.password.BasicPasswordEncryptor

/*
* Roles for authorization
*/
object Admin extends Role {
    override lazy val name = "role.admin"
}

case class User(@Key("_id") username: String, password: String, roles: Set[String]) extends RoleOwner {
	  /**
     * Checks for a role on this user.
     * @param role The role to check against.
     * @return True if the user possesses this role, false otherwise.
     */
    def hasRole(role: Role): Boolean = roles.contains(role.name)

    /**
     * Gets all roles that are attached to this user.
     * @return All roles attached to this user.
     */
    def getAllRoles: Seq[Role] = roles.flatMap( Role.fromString(_) ).toSeq
}

object User extends ModelCompanion[User, String] {
	val dao = new SalatDAO[User, String](collection = Db.connect("users")) {}

	lazy val encryptor = new BasicPasswordEncryptor // Threadsafe

	def authenticate(username: String, password: String) = dao.findOneById(username).filter( user => encryptor.checkPassword(password, user.password))
	
	def find(username: String) = dao.findOneById(username)
	
    def findRoleOwner(username: String) = dao.findOneById(username).get.asInstanceOf[RoleOwner]

	def add(user:User) = dao.insert(user)

	def list:Set[User] = dao.find(MongoDBObject.empty).toSet
}