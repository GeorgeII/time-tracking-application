package controllers

import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{Lang, Messages}

import javax.inject._
import play.api.mvc._

/**
 * This class is used as a layer to make possible Play Forms (POST-request input forms on the client-side, but written
 * in Scala on the server-side)
 */
case class UserData(nickname: String, password: String)

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents)(implicit assetsFinder: AssetsFinder)
  extends AbstractController(cc) {

  private val userForm = Form(
    mapping(
      "Nickname" -> nonEmptyText,
      "Password"  -> nonEmptyText
    )(UserData.apply)(UserData.unapply)
  )

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action { implicit request =>
    implicit val messages: Messages = messagesApi.preferred(Seq(Lang.defaultLang))
    Ok(views.html.index(userForm))
  }

  def authorize = Action { implicit request =>
    userForm.bindFromRequest.fold(
      formWithErrors => {
        // binding failure, you retrieve the form containing errors:
        BadRequest(views.html.error("Authorization got a problem. You must've filled the form with errors!"))
      },
      userData => {
        /* binding success, you get the actual value. */
        val identifier = services.UserManager.find(userData.nickname, userData.password)
        val sessionToken = services.SessionTokenManager.create(userData.nickname)
        if (identifier.isDefined && sessionToken.isDefined) {
          Redirect(routes.AccountController.personalPage())
            .withCookies(Cookie("sessionToken", sessionToken.get.toString), Cookie("user", userData.nickname))
            .bakeCookies()
        } else BadRequest(views.html.error("Authorization got a problem. Nickname or password is wrong!"))
      }
    )
  }

  /**
   * Uses input from Sign Up section on the main page to create a new user.
   * @return BadRequest if nickname or password are empty or redirects to user's homepage otherwise.
   */
  def createUser = Action { implicit request =>
    userForm.bindFromRequest.fold(
      formWithErrors => {
        // binding failure, you retrieve the form containing errors:
        BadRequest(views.html.error("Creating a user got a problem. You must've filled the forms with errors!"))
      },
      userData => {
        /* binding success, you get the actual value. */
        val identifier = services.UserManager.create(userData.nickname, userData.password)
        val sessionToken = services.SessionTokenManager.create(userData.nickname)
        if (identifier.isDefined)
          Redirect(routes.AccountController.personalPage())
            .withCookies(Cookie("sessionToken", sessionToken.get.toString), Cookie("user", userData.nickname))
            .bakeCookies()
        else BadRequest(views.html.error("Authorization got a problem. Such username already exists or there's a database problem on the server!"))
      }
    )
  }
}
