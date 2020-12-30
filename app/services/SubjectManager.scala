package services

import models.Subject
import play.api.libs.json.{JsValue, Json, OWrites}
import services.UserManager

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success, Try}

object SubjectManager {

  def create(name: String, user: String): Option[Int] = {
    val exists = UserManager.isExist(user)
    if (!exists)
      return None

    val userId = UserManager.getId(user)
    if (userId.isEmpty)
      return None

    val insertionIntoDb = DAOs.SubjectDao.createSubject(name, userId.get)
    Try(Await.result(insertionIntoDb, 3.second)) match {
      case Success(res) => Option(res)
      case Failure(e) =>
        e.printStackTrace()
        None
    }
  }

  /**
   *  Returns a sequence of all subject the user has (empty Seq if the user does not have any).
   */
  def getAllSubjects(user: String): Seq[Subject] ={
    val userId = services.UserManager.getId(user)

    if (userId.isEmpty)
      return Seq()

    val selectFromDatabaseByUserId = DAOs.SubjectDao.getAllSubjects(userId.get)

    Try(Await.result(selectFromDatabaseByUserId, 3.second)) match {
      case Success(res) => res
      case Failure(e) =>
        e.printStackTrace()
        Seq()
    }
  }

  def convertToJson(subjects: Seq[Subject]): JsValue = {
    implicit val horseProfileWrites: OWrites[Subject] = Json.writes[Subject]
    Json.toJson(subjects)
  }
}
