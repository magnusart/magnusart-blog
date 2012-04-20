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
import models.MetaData._
import com.codahale.jerkson._

@JsonSnakeCase
case class Article(@Key("_id") friendlyUrl:String, 
	title:String, 
	body:String, 
	author:String, 
	publishDate:DateTime = getLocalCurrentTime, 
	metaData:MetaData = MetaData())

object Article {
	private object ArticleDAO extends SalatDAO[Article, String](collection = Db.connect("articles"))

	def list():List[Article] = ArticleDAO.find(MongoDBObject.empty).sort(orderBy = MongoDBObject("publishDate" -> 1)).toList

}