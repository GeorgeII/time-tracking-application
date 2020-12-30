package DAOs

import models.User
import slick.jdbc.PostgresProfile.api._
import slick.jdbc.JdbcBackend.Database

import java.util.UUID
import scala.concurrent.Future

object UserDao {

  type Data = (Option[Long], String, String, UUID)

  def constructUser: Data => User = {
    case (id, nickname, password, identifier) => User(nickname, password, identifier)
  }

  def extractUser: PartialFunction[User, Data] = {
    case User(nickname, password, identifier) =>
      (None, nickname, password, identifier)
  }

  class Users(tag: Tag) extends Table[User](tag, _tableName = "Users") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column

    def nickname = column[String]("nickname", O.Unique)

    def password = column[String]("password")

    def identifier = column[UUID]("identifier")

    // Every table needs a * projection with the same type as the table's type parameter
    def * = (id.?, nickname, password, identifier) <> (constructUser, extractUser.lift)
  }

  lazy val users = TableQuery[Users]

  private lazy val db = Database.forConfig("postgres")

  def getUser(nickname: String): Future[Option[User]] = {
    db.run(users.filter(_.nickname === nickname).result.headOption)
  }

  def createUser(user: User): Future[Int] = {
    val query =  users += user
    db.run(query)
  }

  def getId(nickname: String): Future[Option[Long]] = {
    db.run(users.filter(_.nickname === nickname).map(_.id).result.headOption)
  }

  def getIdentifier(nickname: String): Future[Option[UUID]] = {
    db.run(users.filter(_.nickname === nickname).map(_.identifier).result.headOption)
  }
}
