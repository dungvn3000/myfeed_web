package admin.controllers

import org.bson.types.ObjectId
import dao.FeedDao
import curd.{FormBuilder, TableBuilder}
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import validation.Constraint._
import vn.myfeed.model.Feed
import plugin.ObjectIdFormat._
import scala.collection.mutable.ListBuffer
import com.mongodb.casbah.commons.MongoDBObject
import play.api.i18n.Messages

/**
 * The Class Feeds.
 *
 * @author Nguyen Duc Dung
 * @since 6/5/13 8:09 AM
 *
 */
object Feeds extends CurdController[Feed, ObjectId] {

  val pageTitle = Messages("feed.title")

  val dao = FeedDao

  lazy val formBuilder = new FormBuilder[Feed]("Feed Form") {
    val form = Form(
      mapping(
        textField("_id", 'hidden -> true) -> of[ObjectId],
        textField("name", '_label -> "Name *") -> nonEmptyText,
        textField("url", '_label -> "Url *") -> nonEmptyText.verifying(urlConstraint),
        textField("topic", '_label -> "Topic") -> optional(text)
      )(Feed.apply)(Feed.unapply)
    )
  }

  lazy val tableBuilder = new TableBuilder[Feed] {
    column("name", "Name")
    column("url", "Url")
    column("topic", "Topic")
  }

  override def customValidate(feed: Feed) = {
    var errors = new ListBuffer[FormError]

    if (!validateUrl(feed)) {
      errors += new FormError("url", "url.exist")
    }

    if (errors.isEmpty) None
    else Some(errors.toSeq)
  }

  /**
   * Checking whether the url is exist in the database or not.
   * @param feed
   * @return
   */
  private def validateUrl(feed: Feed): Boolean = dao.find(MongoDBObject(
    "url" -> feed.url,
    "_id" -> MongoDBObject("$ne" -> feed._id)
  )).isEmpty

}
