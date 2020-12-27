package services

import models.User

import java.util.UUID
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}

/**
 * A service for handling, i.e. retrieving, creating, etc., data from/to the database.
 */
object UserManager {

  /**
   * Looks for a user in the database.
   * @param nickname username
   * @param password MUST be represented as a salted hash. No plain-text password!!!
   * @return Unique identifier if the user was found, None otherwise.
   */
  def find(nickname: String, password: String): Option[UUID] = {
    val selectFromDatabaseByNickname = DAOs.UserDao.getUser(nickname)
    val result = Try(Await.result(selectFromDatabaseByNickname, 3.second)) match {
      case Success(res) => res
      case Failure(e) =>
        e.printStackTrace()
        None
    }

    if (result.isDefined) {
      Option(result.get.identifier)
    }
    else {
      None
    }
  }

  /**
   * Creates a new user in the database. Handles inner logic of a program. If this username already exist it does not
   * create a new user.
   * @param nickname username
   * @param password MUST be represented as a salted hash. No plain-text password!!!
   * @return None if this nickname already exists, id: Option[UUID] otherwise.
   */
  def create(nickname: String, password: String): Option[UUID] = {
    val result = find(nickname, password)

    // that means the nickname already exists
    if (result.isDefined) {
      return None
    }

    // otherwise:

    val identifier = UUID.randomUUID()
    val user = User(nickname, password, identifier)
    val insertionIntoDb = DAOs.UserDao.createUser(user)
    val idInt = Try(Await.result(insertionIntoDb, 3.second)) match {
      case Success(res) => res
      case Failure(e) =>
        e.printStackTrace()
        -1
    }

    // if the new user was not created in the database
    if (idInt == -1) {
      None
    }
    else {
      Option(identifier)
    }
  }
}
