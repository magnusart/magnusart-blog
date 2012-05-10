package models
import scala.util._
import com.mongodb.casbah.MongoConnection
import play.api.Play._
import play.Logger
import java.net.URI

object Db {
	// Get the HEROKU MongoLab URI or fallback to configuration
	lazy val mongoUri = current.configuration.getString("mongodb.uri").getOrElse("mongodb:///")

	def connect(collection: String) = {
		Logger.debug("Mongo URI: " + mongoUri)
		val uri = new URI(mongoUri)

		val dbHost = uri.getHost
		// Strip out leading slash
		val dbName = if(uri.getPath.startsWith("/")) uri.getPath.substring(1) else uri.getPath
		val dbPort = uri.getPort
		val conn = MongoConnection(dbHost, dbPort)(dbName)

		// Get user credentials
		val credentials = if(Option(uri.getUserInfo) isDefined) Option(uri .getUserInfo.split(":",2)) else None

		// If we have credentials try to authenticate
		if (credentials isDefined)  conn.authenticate(credentials.get.head, credentials.get.tail.head)

		conn(collection)
	}
}