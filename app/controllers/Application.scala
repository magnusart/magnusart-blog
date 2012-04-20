package controllers

import play.api._
import play.api.mvc._
import com.codahale.jerkson.Json._
import models._

object Application extends Controller {
  
  def index = Action {
    Ok(views.html.index(Article.list))
  }
  
  def listArticles = Action {
  	val articles = Article.list
  	Ok(generate(articles))
  }

  def getArticle(friendlyName:String) = Action {
  	Article.get(friendlyName) map {
  		article:Article => Ok(views.html.article(article, true)) 
  	} getOrElse(NotFound)
  }

}