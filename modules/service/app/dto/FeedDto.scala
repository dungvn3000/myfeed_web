package dto

import play.api.libs.json._
import play.api.libs.functional.syntax._
import org.linkerz.model.Feed

/**
 * The Class FeedDto.
 *
 * @author Nguyen Duc Dung
 * @since 5/12/13 2:43 AM
 *
 */
case class FeedDto(id: String, name: String, url: String, unread: Long, recommend: Long)

object FeedDto {

  def apply(feed: Feed, unread: Long, recommend: Long) = new FeedDto(feed.id, feed.name, feed.url, unread, recommend)

  implicit val jsonWrite = (
    (__ \ "id").write[String] ~
      (__ \ "name").write[String] ~
      (__ \ "url").write[String] ~
      (__ \ "unread").write[Long] ~
      (__ \ "recommend").write[Long]
    )(unlift(FeedDto.unapply))

}
