package admin.controllers

import vn.myfeed.model.{User, Logging}
import org.bson.types.ObjectId
import dao.{UserDao, LoggingDao}
import play.api.i18n.Messages
import curd.TableBuilder
import vn.myfeed.model.User

/**
 * The Class Logs.
 *
 * @author Nguyen Duc Dung
 * @since 6/7/13 9:01 PM
 *
 */
object Logs extends BaseController[User, ObjectId] {

  val pageTitle = Messages("log.title")

  val dao = UserDao

  lazy val tableBuilder = new TableBuilder[User] {
    column("url", "Url")
    column("message", "Message")
    column("createDate", "Date")
  }
}
