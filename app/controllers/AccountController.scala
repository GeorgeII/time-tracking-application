package controllers

import play.api.mvc._
import services.SessionTokenManager
import services.SubjectManager

import java.util.UUID
import javax.inject._

/**
 * This controller handles all the subjects that a user has. Also, it allows to create a new subject.
 */
@Singleton
class AccountController @Inject()(cc: ControllerComponents)(implicit assetsFinder: AssetsFinder)
  extends AbstractController(cc) {

  def personalPage = Action { implicit request =>
    val sessionToken = request.cookies.get("sessionToken")
    val user = request.cookies.get("user")

    if (sessionToken.isDefined && user.isDefined &&
        SessionTokenManager.isSessionTokenValid(UUID.fromString(sessionToken.get.value), user.get.value)) {
      Ok(views.html.personalPage(user.get.value.capitalize))
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

  def createSubject = Action {
    Ok("createSubject")
  }
}
