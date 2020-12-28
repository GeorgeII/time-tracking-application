package controllers

import javax.inject._
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents)(implicit assetsFinder: AssetsFinder)
  extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    Ok(views.html.index(assetsFinder))
  }

  def authorize = Action { implicit request =>
    val content = request.body.asJson.get
    val identifier = services.UserManager.find(content("nickname").toString, content("password").toString)

    // if there's no such user or password is wrong
    if (identifier.isEmpty) {
      Ok(views.html.error("This message was written in HomeController.authorize method."))
    }
    else {
      Ok(views.html.personalPage("This message was written in HomeController.authorize method."))
    }
  }

  /**
   * Uses input from Sign Up section on the main page to create a new user.
   * @return BadRequest if nickname or password are empty or redirects to user's homepage otherwise.
   */
  def createUser = Action { implicit request =>
    val content = request.body.asJson.get
    val identifier = services.UserManager.create(content("nickname").toString, content("password").toString)

    // if this user already exists or something's wrong with the database
    if (identifier.isEmpty) {
      Ok(views.html.error("This message was written in HomeController.createUser method."))
    }
    else {
      Ok(views.html.personalPage("This message was written in HomeController.createUser method."))
    }
  }
}
