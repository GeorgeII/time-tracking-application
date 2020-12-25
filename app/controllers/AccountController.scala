package controllers

import play.api.mvc._

import javax.inject._

/**
 * This controller handles all the subjects that a user has. Also, it allows to create a new subject.
 */
@Singleton
class AccountController @Inject() (cc: ControllerComponents)
  extends AbstractController(cc) {

  def showSubjects = Action {
    Ok("showSubjects")
  }

  def createSubject = Action {
    Ok("createSubject")
  }
}
