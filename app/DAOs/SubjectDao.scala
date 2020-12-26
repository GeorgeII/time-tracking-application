package DAOs

import models.Subject
import DAOs.UserDao.users
import slick.jdbc.PostgresProfile.api._
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.{Future, ExecutionContext}

object SubjectDao {

  class Subjects(tag: Tag) extends Table[Subject](tag, _schemaName = Option("public"), _tableName = "Subjects") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def userId = column[Long]("user_id")

    def name = column[String]("name")

    def hours = column[Int]("hours")

    def minutes = column[Int]("minutes")

    def seconds = column[Int]("seconds")

    def * = (userId, name, hours, minutes, seconds) <> (Subject.tupled, Subject.unapply)

    // A reified foreign key relation that can be navigated to create a join
    def user =
      foreignKey("user_fk", userId, users)(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)
  }

  lazy val subjects = TableQuery[Subjects]

  private lazy val db = Database.forConfig("postgres")

  def getSubject(name: String, user: String): Future[Option[Subject]] = {
    db.run(subjects.filter(_.name === name).result.headOption)
  }

  def createSubject(name: String, userId: Long): Future[Int] = {
    val createQuery =  subjects += Subject(userId, name, 0, 0, 0)

    db.run(createQuery)
  }

  def updateSubject(subject: Subject, userId: Int): Future[Int] = {
    val updateSubjectQuery = subjects.filter(_.userId === subject.userId).filter(_.name === subject.name)
      .map(item => (item.hours, item.minutes, item.seconds)).update(subject.hours, subject.minutes, subject.seconds)

    db.run(updateSubjectQuery)
  }
}
