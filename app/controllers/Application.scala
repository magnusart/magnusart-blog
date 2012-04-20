package controllers

import play.api._
import play.api.mvc._
import com.codahale.jerkson.Json._
import models._

object Application extends Controller {
  
  def index = Action {
    Ok(views.html.index("Magnusart"))
  }
  
  def listArticles = Action {
  	val articles = Article.list
  	Ok(generate(articles))
  }

}