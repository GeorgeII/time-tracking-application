package services

import DAOs.{SubjectDao, UserDao}
import slick.jdbc.PostgresProfile.api._
import slick.dbio.DBIO
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.meta.MTable

import javax.inject.Singleton
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Creates all required tables. Used in the eager loading to run on the startup stage but can be used differently.
 */
@Singleton
class DatabaseTableCreator{

  val db = Database.forConfig("postgres")
  val testDb = Database.forConfig("test-db-postgres")

  val tables = List(UserDao.users, SubjectDao.subjects)
  val databases = List(db, testDb)

  databases.foreach(createInParticularDatabase)

  def createInParticularDatabase(db: Database): Unit = {
    val existing = db.run(MTable.getTables)
    val f = existing.flatMap(v => {
      val names = v.map(mt => mt.name.name)
      val createIfNotExist = tables.filter(table =>
        (!names.contains(table.baseTableRow.tableName))).map(_.schema.create)
      db.run(DBIO.sequence(createIfNotExist))
    })
    Await.result(f, Duration.Inf)
  }
}
