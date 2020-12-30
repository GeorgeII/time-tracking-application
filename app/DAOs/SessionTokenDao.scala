package DAOs

import models.SessionToken
import DAOs.UserDao.users
import slick.jdbc.PostgresProfile.api._
import slick.jdbc.JdbcBackend.Database

import java.time.LocalDateTime
import java.util.UUID
import scala.concurrent.Future

object SessionTokenDao {

  type Data = (Option[Long], Long, UUID, LocalDateTime)

  def constructSessionToken: Data => SessionToken = {
    case (id, userId, token, expirationDate) => SessionToken(userId, token, expirationDate)
  }

  def extractSessionToken: PartialFunction[SessionToken, Data] = {
    case SessionToken(userId, token, expirationDate) =>
      (None, userId, token, expirationDate)
  }

  class SessionTokens(tag: Tag) extends Table[SessionToken](tag, _tableName = "Session_tokens") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def userId = column[Long]("user_id")

    def token = column[UUID]("token")

    def expirationDate = column[LocalDateTime]("expiration_date")

    def * = (id.?, userId, token, expirationDate) <> (constructSessionToken, extractSessionToken.lift)

    // A reified foreign key relation that can be navigated to create a join
    def user =
      foreignKey("user_fk", userId, users)(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)
  }

  lazy val tokens = TableQuery[SessionTokens]

  private lazy val db = Database.forConfig("postgres")

  def getByToken(token: UUID, userId: Long): Future[Option[SessionToken]] = {
    db.run(tokens.filter(_.userId === userId).filter(_.token === token).result.headOption)
  }

  def createSessionToken(token: SessionToken): Future[Int] = {
    val query = tokens += token
    db.run(query)
  }
}
