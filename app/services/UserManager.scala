package services

import models.User

import java.security.SecureRandom
import java.util.{Base64, UUID}
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}

/**
 * A service for handling, i.e. retrieving, creating, etc., data from/to the database.
 */
object UserManager {

  private lazy val DEFAULT_ITERATIONS = 10000
  private lazy val random = new SecureRandom()

  /**
   * Looks for a user in the database.
   * @param nickname username
   * @param password takes a plain-text but MUST be transformed into a salted hash. No plain-text password can be saved into the db!!!
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

    // if we got this user and the password is correct
    if (result.isDefined && checkPassword(password, result.get.password)) {
      Option(result.get.identifier)
    }
    else {
      None
    }
  }

  def isExist(nickname: String): Boolean = {
    val selectFromDatabaseByNickname = DAOs.UserDao.getUser(nickname)
    val result = Try(Await.result(selectFromDatabaseByNickname, 3.second)) match {
      case Success(res) => res
      case Failure(e) =>
        e.printStackTrace()
        None
    }

    result match {
      case Some(x) => true
      case _ => false
    }
  }

  /**
   * Creates a new user in the database. Handles inner logic of a program. If this username already exist it does not
   * create a new user.
   * @param nickname username
   * @param password takes a plain-text but MUST be transformed into a salted hash. No plain-text password can be saved into the db!!!
   * @return None if this nickname already exists, id: Option[UUID] otherwise.
   */
  def create(nickname: String, password: String): Option[UUID] = {
    val hashedPassword = hashPassword(password)

    val exists = isExist(nickname)

    // that means the nickname already exists and cannot be created again.
    if (exists) {
      return None
    }

    // otherwise:

    val identifier = UUID.randomUUID()
    val user = User(nickname, hashedPassword, identifier)
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

  /**
   * Gets an id of a user. Primarily, this is required to join tables.
   */
  def getId(nickname: String): Option[Long] = {
    val selectFromDatabaseByNickname = DAOs.UserDao.getId(nickname)
    Try(Await.result(selectFromDatabaseByNickname, 3.second)) match {
      case Success(res) => res
      case Failure(e) =>
        e.printStackTrace()
        None
    }
  }

  /**
   * Identifier is a UUID that was generated for a user during the sign up stage.
   */
  def getIdentifier(nickname: String): Option[UUID] = {
    val selectFromDatabaseByNickname = DAOs.UserDao.getIdentifier(nickname)
    Try(Await.result(selectFromDatabaseByNickname, 3.second)) match {
      case Success(res) => res
      case Failure(e) =>
        e.printStackTrace()
        None
    }
  }

  /**
   * Generates a hash of a given password.
   * @param password plain-text
   * @return salted hash
   */
  private def hashPassword(password: String): String = {
    val salt = new Array[Byte](16)
    random.nextBytes(salt)
    val hash = pbkdf2(password, salt, DEFAULT_ITERATIONS)
    val salt64 = Base64.getEncoder.encodeToString(salt)
    val hash64 = Base64.getEncoder.encodeToString(hash)

    s"$DEFAULT_ITERATIONS:$hash64:$salt64"
  }

  private def pbkdf2(password: String, salt: Array[Byte], iterations: Int): Array[Byte] = {
    val keySpec = new PBEKeySpec(password.toCharArray, salt, iterations, 256)
    val keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
    keyFactory.generateSecret(keySpec).getEncoded
  }

  private def checkPassword(password: String, passwordHash: String): Boolean = {
    passwordHash.split(":") match {
      case Array(it, hash64, salt64) if it.forall(_.isDigit) =>
        val hash = Base64.getDecoder.decode(hash64)
        val salt = Base64.getDecoder.decode(salt64)

        val calculatedHash = pbkdf2(password, salt, it.toInt)
        calculatedHash.sameElements(hash)

      case _ => sys.error("Bad password hash")
    }
  }

}
