package services

import models.Subject
import play.api.libs.json.{JsValue, Json, OWrites}
import services.UserManager

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success, Try}

object SubjectManager {

  def create(name: String, user: String): Option[Int] = {
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

  def getSubject(name: String, user: String): Option[Subject] = {
    val userId = services.UserManager.getId(user)
    if (userId.isEmpty)
      return None

    val selectFromDatabaseByUserId = DAOs.SubjectDao.getSubject(name, userId.get)

    Try(Await.result(selectFromDatabaseByUserId, 3.second)) match {
      case Success(res) => res
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

  def updateTime(name: String, user: String, values: (Int, Int, Int, Long)): Option[Int] = {
    val userId = UserManager.getId(user)
    if (userId.isEmpty)
      return None

    val previousSubjectQuery = DAOs.SubjectDao.getSubject(name, userId.get)
    val previousSubject = Try(Await.result(previousSubjectQuery, 3.second)) match {
      case Success(res) => res
      case Failure(e) =>
        e.printStackTrace()
        None
    }

    if (previousSubject.isEmpty)
      return None

    val totalSeconds = previousSubject.get.totalSeconds + values._4
    val hours = (totalSeconds / (60 * 60)).toInt
    val minutes = (totalSeconds % (60 * 60) / 60).toInt
    val seconds = (totalSeconds % 60).toInt

    val subject = Subject(userId.get, name, hours, minutes, seconds, totalSeconds)
    val updateInDatabase = DAOs.SubjectDao.updateSubject(subject)

    Try(Await.result(updateInDatabase, 3.second)) match {
      case Success(res) => Option(res)
      case Failure(e) =>
        e.printStackTrace()
        None
    }
  }

  def isExist(name: String, user: String): Boolean = {
    val userId = services.UserManager.getId(user)

    if (userId.isEmpty)
      return false

    val selectFromDatabaseByNickname = DAOs.SubjectDao.getSubject(name, userId.get)
    val result = Try(Await.result(selectFromDatabaseByNickname, 3.second)) match {
      case Success(res) => res
      case Failure(e) =>
        e.printStackTrace()
        None
    }

    result match {
      case Some(x) => true
      case None => false
    }
  }

  def convertToJson(subjects: Seq[Subject]): JsValue = {
    implicit val subjectWrites: OWrites[Subject] = Json.writes[Subject]
    Json.toJson(subjects)
  }

  /**
   * Sorts in descending order.
   */
  def sortByTime(subjects: Seq[Subject]): Seq[Subject] = {
    subjects.sortBy(-_.totalSeconds)
  }
}
