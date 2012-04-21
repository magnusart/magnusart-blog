package controllers

import play.api._
import play.api.mvc._
import com.codahale.jerkson.Json
import models._
import play.Logger
import org.scala_tools.time.TypeImports.DateTime

object Application extends Controller {
  
  def index = Action {
    Ok(views.html.index(Article.list))
  }
  
  def listArticles = Action {
  	val articles = Article.list
  	Ok(Json.generate(articles))
  }

  def getArticle(friendlyUrl:String) = Action {
  	Article.get(friendlyUrl) map {
  		article:Article => Ok(views.html.article(article, true)) 
  	} getOrElse(NotFound)
  }

  def upsertArticle(friendlyUrl:String) = Action { implicit request =>
    val json:String = request.body.asJson.map(_.toString).getOrElse("{}")
    Logger.debug("JSON: " + json)
    val updatedArticle:Option[Article] = Option(Json.parse[Article](json))
    updatedArticle map { article:Article =>
      Article.upsert(friendlyUrl, article)
      Ok("")
    } getOrElse(NotFound)
  }

}