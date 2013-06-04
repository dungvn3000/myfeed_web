package dto

import vn.myfeed.model.News
import play.api.libs.json._
import play.api.libs.functional.syntax._

/**
 * The Class LinkItem.
 *
 * @author Nguyen Duc Dung
 * @since 4/1/13 10:22 AM
 *
 */
case class NewsDto(
                    id: String,
                    feedId: String,
                    title: String,
                    description: String,
                    url: String,
                    read: Boolean = false,
                    recommend: Boolean = false
                    )

object NewsDto {

  def apply(news: News, read: Boolean, recommend: Boolean) = new NewsDto(
    id = news.id,
    feedId = news.feedId.toString,
    title = news.title,
    description = news.description.getOrElse(""),
    url = news.url,
    read = read,
    recommend = recommend
  )

  implicit val jsonWrite = (
    (__ \ "id").write[String] ~
      (__ \ "feedId").write[String] ~
      (__ \ "title").write[String] ~
      (__ \ "description").write[String] ~
      (__ \ "url").write[String] ~
      (__ \ "read").write[Boolean] ~
      (__ \ "recommend").write[Boolean]
    )(unlift(NewsDto.unapply))
}
