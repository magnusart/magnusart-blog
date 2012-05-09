package controllers

import play.api._
import play.api.mvc._
import com.codahale.jerkson.Json
import models._
import play.Logger
import org.scala_tools.time.TypeImports.DateTime
import play.api.data._
import play.api.data.Forms._
import com.edropple.velvetrope.actions.AccessControlled

object Application extends Controller with AccessControlled {
  
val loginForm = Form(
    tuple(
      "authentication.username" -> text,
      "authentication.password" -> text
      ) verifying ("authentication.failed", result => result match {
        case (username, password) => User.authenticate(username, password).isDefined
        })
      )

   /**
   * Handle login form submission.
   */
  def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.login(formWithErrors)),
      formLogin => Redirect(routes.Application.index).withSession("username" -> formLogin._1)
      )
   }

  /**
  * Login page.
  */
  def login = Action { implicit request =>
    Ok(views.html.login(loginForm))
  }

  /**
  * Logout and clean the session.
  */
  def logout = Action {
    Redirect(routes.Application.login).withNewSession.flashing("success" -> "authentication.loggedout")
  }


  def index = Action {
      Ok(views.html.index(Article.list))
  }
  
  def listArticles = Action {
  	val articles = Article.list
  	Ok(Json.generate(articles)).withHeaders("Content-Type" -> "application/json")
  }

  def getArticle(friendlyUrl:String) = Action {
  	  Article.get(friendlyUrl) map {
    		article:Article => Ok(views.html.article(article)) 
    	} getOrElse(NotFound)
  }


  case class ResponseJson(status:String, message:String = "No message")

  def upsertArticle(friendlyUrl:String) = Action { implicit request =>
    val json:String = request.body.asJson.map(_.toString).getOrElse("{}")
    Logger.debug("JSON: " + json)
    val updatedArticle:Option[Article] = Option(Json.parse[Article](json))
    updatedArticle map { article:Article =>
      Article.upsert(friendlyUrl, article)
      Ok(Json.generate(ResponseJson("success"))).withHeaders("Content-Type" -> "application/json")
    } getOrElse(NotFound)
  }

}