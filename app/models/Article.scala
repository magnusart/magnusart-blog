package models
import com.novus.salat._
import salatctx.salatctx._
import com.novus.salat.annotations._
import com.novus.salat.dao._
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoConnection
import com.mongodb.casbah.commons.MongoDBObject
import org.scala_tools.time.Imports._
import play.Logger
import models._
import com.codahale.jerkson._
import models.MetaData._

@JsonSnakeCase
case class Article(title:String, 
	@Key("_id") friendlyUrl:String, 
	body:String, 
	author:String, 
	publishDate:String = getLocalCurrentTime.toString) {

	import org.scala_tools.time.Imports.DateTimeFormat._
	val formattedPublishDate = new DateTime(publishDate).toString(forPattern(datePattern))
}

object Article {
	private object ArticleDAO extends SalatDAO[Article, String](collection = Db.connect("articles"))

	def list():List[Article] = ArticleDAO.find(MongoDBObject.empty).sort(orderBy = MongoDBObject("publishDate" -> 1)).toList
	def get(friendlyUrl:String) = ArticleDAO.findOneByID(friendlyUrl)
	def upsert(friendlyUrl:String, article:Article) = ArticleDAO.update(MongoDBObject("_id" -> friendlyUrl), article, true, false, new WriteConcern())			

}