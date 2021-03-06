package DAOs

import models.Subject
import DAOs.UserDao.users
import slick.jdbc.PostgresProfile.api._
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.Future

object SubjectDao {

  type Data = (Option[Long], Long, String, Int, Int, Int, Long)

  def constructSubject: Data => Subject = {
    case (id, userId, name, hours, minutes, seconds, totalSeconds) => Subject(userId, name, hours, minutes, seconds, totalSeconds)
  }

  def extractSubject: PartialFunction[Subject, Data] = {
    case Subject(userId, name, hours, minutes, seconds, totalSeconds) =>
      (None, userId, name, hours, minutes, seconds, totalSeconds)
  }

  class Subjects(tag: Tag) extends Table[Subject](tag, _tableName = "Subjects") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def userId = column[Long]("user_id")

    def name = column[String]("name")

    def hours = column[Int]("hours")

    def minutes = column[Int]("minutes")

    def seconds = column[Int]("seconds")

    def totalSeconds = column[Long]("total_seconds")

    def * = (id.?, userId, name, hours, minutes, seconds, totalSeconds) <> (constructSubject, extractSubject.lift)

    // A reified foreign key relation that can be navigated to create a join
    def user =
      foreignKey("user_fk", userId, users)(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)
  }

  lazy val subjects = TableQuery[Subjects]

  private lazy val db = Database.forConfig("postgres")

  def getSubject(name: String, userId: Long): Future[Option[Subject]] = {
    db.run(subjects.filter(_.userId === userId).filter(_.name === name).result.headOption)
  }

  def getAllSubjects(userId: Long): Future[Seq[Subject]] = {
    db.run(subjects.filter(_.userId === userId).result)
  }

  def createSubject(name: String, userId: Long): Future[Int] = {
    val createQuery =  subjects += Subject(userId, name, 0, 0, 0, 0)

    db.run(createQuery)
  }

  def updateSubject(subject: Subject): Future[Int] = {
    val updateSubjectQuery = subjects.filter(_.userId === subject.userId).filter(_.name === subject.name)
      .map(item => (item.hours, item.minutes, item.seconds, item.totalSeconds))
      .update(subject.hours, subject.minutes, subject.seconds, subject.totalSeconds)

    db.run(updateSubjectQuery)
  }
}
