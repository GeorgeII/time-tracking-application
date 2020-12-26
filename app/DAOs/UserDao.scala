package DAOs

import models.User
import slick.jdbc.PostgresProfile.api._
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.{Future, ExecutionContext}

object UserDao {

  class Users(tag: Tag) extends Table[User](tag, _schemaName = Option("public"), _tableName = "Users") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column

    def nickname = column[String]("nickname")

    def password = column[String]("password")

    // Every table needs a * projection with the same type as the table's type parameter
    def * = (nickname, password) <> (User.tupled, User.unapply)
  }

  lazy val users = TableQuery[Users]

  private lazy val db = Database.forConfig("postgres")

  def getUser(nickname: String): Future[Option[User]] = {
    db.run(users.filter(_.nickname === nickname).result.headOption)
  }

  def createUser(nickname: String, password: String): Future[Int] = {
    val query =  users += User(nickname, password)
    db.run(query)
  }
}
