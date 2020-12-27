package controllers

import javax.inject._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._


/**
 * This class is used to better integration with input forms on the main page
 */
case class UserInformation(nickname: String, password: String)

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents)(implicit assetsFinder: AssetsFinder)
  extends AbstractController(cc) {

  /**
   * Needed to reduce code duplication in authorizeInSystem and createUser methods.
   */
  private lazy val userInformationForm = Form(
    mapping(
      "nickname" -> text.verifying(nonEmpty),
      "password"  -> text.verifying(nonEmpty),
    )(UserInformation.apply)(UserInformation.unapply)
  )

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    val authorizeUrl = routes.HomeController.authorize()
    val createUserUrl = routes.HomeController.createUser()

    Ok(views.html.index(userInformationForm, authorizeUrl))
  }

  def authorize = Action { implicit request =>
    val userData = userInformationForm.bindFromRequest.get

    userInformationForm.bindFromRequest.fold(
      formWithErrors => {
        // binding failure, you retrieve the form containing errors:
        BadRequest(views.html.error("You entered an empty nickname or password"))
      },
      userData => {
        /* binding success, you get the actual value. */
        val identifier = services.UserManager.find(userData.nickname, userData.password)
        Redirect(routes.AccountController.personalPage())
      }
    )
  }

  /**
   * Uses input from Sign Up section on the main page to create a new user.
   * @return BadRequest if nickname or password are empty or redirects to user's homepage otherwise.
   */
  def createUser = Action { implicit request =>
    val userData = userInformationForm.bindFromRequest.get

    userInformationForm.bindFromRequest.fold(
      formWithErrors => {
        // binding failure, you retrieve the form containing errors:
        BadRequest(views.html.error("You entered an empty nickname or password"))
      },
      userData => {
        /* binding success, you get the actual value. */
        val id = services.UserManager.create(userData.nickname, userData.password)
        Redirect(routes.AccountController.personalPage())
      }
    )
  }
}
