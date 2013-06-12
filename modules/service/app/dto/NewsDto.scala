package dto

import vn.myfeed.model.{UserNews, News}
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
                    text: String,
                    url: String,
                    read: Boolean = false,
                    recommend: Boolean = false
                    )

object NewsDto {

  def apply(userNews: UserNews, news: News) = new NewsDto(
    id = userNews.id,
    feedId = news.feedId.toString,
    title = news.title,
    description = news.description.getOrElse(""),
    text = news.text.getOrElse(""),
    url = news.url,
    read = userNews.read,
    recommend = userNews.recommend
  )

  implicit val jsonWrite = (
    (__ \ "id").write[String] ~
      (__ \ "feedId").write[String] ~
      (__ \ "title").write[String] ~
      (__ \ "description").write[String] ~
      (__ \ "text").write[String] ~
      (__ \ "url").write[String] ~
      (__ \ "read").write[Boolean] ~
      (__ \ "recommend").write[Boolean]
    )(unlift(NewsDto.unapply))
}
