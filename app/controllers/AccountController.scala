package controllers

import play.api.mvc._

import javax.inject._

/**
 * This controller handles all the subjects that a user has. Also, it allows to create a new subject.
 */
@Singleton
class AccountController @Inject()(cc: ControllerComponents)(implicit assetsFinder: AssetsFinder)
  extends AbstractController(cc) {

  def personalPage = Action {
    Ok(views.html.personalPage("This message was written in AccountController.showPersonalPage method."))
  }

  def subjects = Action {
    Ok("showSubjects")
  }

  def createSubject = Action {
    Ok("createSubject")
  }
}
