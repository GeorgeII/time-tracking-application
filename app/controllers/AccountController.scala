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
      "Enter your new subject here:" -> text
    )(SubjectData.apply)(SubjectData.unapply)
  )

  def personalPage = Action { implicit request =>
    implicit val messages: Messages = messagesApi.preferred(Seq(Lang.defaultLang))

    val user = request.cookies.get("user")

    if (isAuthorizationOk) {
      Ok(views.html.personalPage(user.get.value.capitalize, subjectForm))
    } else {
      BadRequest(views.html.error("Authorization got a problem - your token is incorrect. Try to Sign In again."))
    }
  }

  def usernameAndToken = Action {implicit request =>
    if (isAuthorizationOk) {
      val sessionToken = request.cookies.get("sessionToken")
      val user = request.cookies.get("user")
      val response = Json.obj("user" -> user.get.value, "sessionToken" -> sessionToken.get.value)
      Ok(response)
    } else {
      BadRequest(views.html.error("Authorization got a problem - your token is incorrect. Try to Sign In again."))
    }
  }

  def subjects(user: String, sessionToken: String) = Action { implicit request =>
    if (!SessionTokenManager.isSessionTokenValid(UUID.fromString(sessionToken), user)) {
      BadRequest(views.html.error("Token is not valid. Try to Sign In again."))
    }
    else {
      val subjects = SubjectManager.getAllSubjects(user)
      val sortedSubjects = SubjectManager.sortByTime(subjects)
      val json = SubjectManager.convertToJson(sortedSubjects)
      Ok(json)
    }
  }

  def createSubject = Action { implicit request =>
    val user = request.cookies.get("user")

    if (!isAuthorizationOk) {
      BadRequest(views.html.error("Authorization got a problem - your token is incorrect. Try to Sign In again."))
    }
    else {

      subjectForm.bindFromRequest.fold(
        formWithErrors => {
          // binding failure, you retrieve the form containing errors:
          BadRequest(views.html.error("There's something wrong with the new subject form parsing."))
        },
        SubjectData => {
          /* binding success, you get the actual value. */

          val isExist = SubjectManager.isExist(SubjectData.name, user.get.value)
          if (isExist)
            BadRequest(views.html.error("This subject already exists."))
          else {
            val id = SubjectManager.create(SubjectData.name, user.get.value)

            id match {
              case Some(index) if index != -1 =>
                Redirect(routes.AccountController.personalPage())

              case _ => BadRequest(views.html.error("A new subject was not created."))
            }
          }
        }
      )
    }
  }

  def updateSubjectTime(subjectName: String, user: String, sessionToken: String) = Action { implicit request =>
    if (!SessionTokenManager.isSessionTokenValid(UUID.fromString(sessionToken), user)) {
      BadRequest(views.html.error("Token is not valid. Try to Sign In again."))
    }
    else {
      val jsonBody = request.body.asJson
      val valuesFromJson = ((jsonBody.get \ "hours").as[Int], (jsonBody.get \ "minutes").as[Int],
        (jsonBody.get \ "seconds").as[Int], (jsonBody.get \ "total-seconds").as[Long])

      val update = SubjectManager.updateTime(subjectName, user, valuesFromJson)
      update match {
        case Some(_) => Redirect(routes.AccountController.personalPage())
        case None => BadRequest(views.html.error("Your session time was not stored. Probably, it's a database false."))
      }
    }
  }

  private def isAuthorizationOk(implicit request: Request[AnyContent]): Boolean = {
    val sessionToken = request.cookies.get("sessionToken")
    val user = request.cookies.get("user")

    sessionToken.isDefined &&
      user.isDefined &&
      SessionTokenManager.isSessionTokenValid(UUID.fromString(sessionToken.get.value), user.get.value)
  }
}
