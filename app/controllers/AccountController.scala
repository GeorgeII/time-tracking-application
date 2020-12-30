package controllers

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{Lang, Messages}
import play.api.libs.json.Json
import services.SessionTokenManager
import services.SubjectManager

import java.util.UUID
import javax.inject._

/**
 * This class is used as a layer to make possible Play Forms (POST-request input forms on the client-side, but written
 * in Scala on the server-side)
 */
case class SubjectData(name: String)

/**
 * This controller handles all the subjects that a user has. Also, it allows to create a new subject.
 */
@Singleton
class AccountController @Inject()(cc: ControllerComponents)(implicit assetsFinder: AssetsFinder)
  extends AbstractController(cc) {

  private val subjectForm = Form(
    mapping(
      "Enter your new subject here:" -> nonEmptyText
    )(SubjectData.apply)(SubjectData.unapply)
  )

  def personalPage = Action { implicit request =>
    implicit val messages: Messages = messagesApi.preferred(Seq(Lang.defaultLang))

    val sessionToken = request.cookies.get("sessionToken")
    val user = request.cookies.get("user")

    if (sessionToken.isDefined && user.isDefined &&
        SessionTokenManager.isSessionTokenValid(UUID.fromString(sessionToken.get.value), user.get.value)) {
      Ok(views.html.personalPage(user.get.value.capitalize, subjectForm))
    }
    else BadRequest(views.html.error("Authorization got a problem - your token is incorrect. Try to Sign In again."))
  }

  def subjects(user: String, sessionToken: String) = Action { implicit request =>
    if (!SessionTokenManager.isSessionTokenValid(UUID.fromString(sessionToken), user)) {
      BadRequest(views.html.error("Token is not valid. Try to Sign In again."))
    }

    val subjects = SubjectManager.getAllSubjects(user)
    val json = SubjectManager.convertToJson(subjects)
    Ok(json)
  }

  def createSubject = Action { implicit request =>
    val sessionToken = request.cookies.get("sessionToken")
    val user = request.cookies.get("user")

    if (!(sessionToken.isDefined &&
          user.isDefined &&
          SessionTokenManager.isSessionTokenValid(UUID.fromString(sessionToken.get.value), user.get.value))) {
      BadRequest(views.html.error("Authorization got a problem - your token is incorrect. Try to Sign In again."))
    }

    subjectForm.bindFromRequest.fold(
      formWithErrors => {
        // binding failure, you retrieve the form containing errors:
        BadRequest(views.html.error("There's something wrong with the new subject form parsing."))
      },
      SubjectData => {
        /* binding success, you get the actual value. */

        // if this subject already exists
        val checkIfExists = services.SubjectManager.getSubject(SubjectData.name, user.get.value)
        if (checkIfExists.isDefined)
          Ok(Json.obj("created" -> false))

        val id = services.SubjectManager.create(SubjectData.name, user.get.value)
        if (id.isDefined && id.get != -1)
          Ok(Json.obj("created" -> true))
        else Ok(Json.obj("created" -> false))
      }
    )
  }
}
