package services

import models.SessionToken

import java.time.LocalDateTime
import java.util.UUID
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}

object SessionTokenManager {

  def isSessionTokenValid(token: UUID, nickname: String): Boolean = {
    val userId = services.UserManager.getId(nickname)
    // no such user in the DB
    if (userId.isEmpty)
      return false

    isSessionTokenValid(token, userId.get)
  }

  private def isSessionTokenValid(token: UUID, userId: Long): Boolean = {
    val selectFromDatabaseByNickname = DAOs.SessionTokenDao.getByToken(token, userId)
    val sessionToken = Try(Await.result(selectFromDatabaseByNickname, 3.second)) match {
      case Success(res) => res
      case Failure(e) =>
        e.printStackTrace()
        None
    }

    // error during db reading or such a token does not exist
    if (sessionToken.isEmpty) {
      return false
    }

    // if a token has not been expired yet
    if (sessionToken.get.expirationDate.isAfter(LocalDateTime.now()))
      true
    else
      false
  }

  def create(nickname: String): Option[UUID] = {
    val userId = services.UserManager.getId(nickname)
    // no such user in the DB
    if (userId.isEmpty)
      return None

    val token = UUID.randomUUID()
    val sessionToken = SessionToken(userId.get, token, LocalDateTime.now().plusHours(6))

    val insertionIntoDb = DAOs.SessionTokenDao.createSessionToken(sessionToken)
    val idInt = Try(Await.result(insertionIntoDb, 3.second)) match {
      case Success(res) => res
      case Failure(e) =>
        e.printStackTrace()
        -1
    }

    // if the sessionToken was not created in the database
    if (idInt == -1)
      None
    else
      Option(token)
  }
}
