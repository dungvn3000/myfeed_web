package dao

import com.novus.salat.dao.SalatDAO
import vn.myfeed.model.UserNews
import org.bson.types.ObjectId
import se.radley.plugin.salat._
import play.api.Play.current
import com.mongodb.casbah.commons.MongoDBObject
import dto.NewsDto

/**
 * The Class FeedDao.
 *
 * @author Nguyen Duc Dung
 * @since 5/1/13 4:59 AM
 *
 */
object UserNewsDao extends SalatDAO[UserNews, ObjectId](mongoCollection("userNews")) {

  def findLinksByFeedId(feedId: ObjectId, recommend: Boolean, userId: ObjectId, page: Int = 1, itemDisplay: Int = 10) = {
    val skip = (page - 1) * itemDisplay
    val query = if (recommend) MongoDBObject(
      "feedId" -> feedId,
      "userId" -> userId,
      "recommend" -> recommend
    )
    else MongoDBObject(
      "feedId" -> feedId,
      "userId" -> userId
    )

    val userNews = find(query)
      .skip(skip)
      .sort(MongoDBObject("read" -> 1, "createdDate" -> -1))
      .limit(itemDisplay)
      .toList

    userNews.map(item => {
      val news = NewsDao.findOneById(item.newsId).getOrElse(throw new Exception("Can't find newsId"))
      NewsDto(news, item.read, item.recommend)
    })
  }

  def countUnreadByFeed(feedId: ObjectId, userId: ObjectId) = count(
    MongoDBObject(
      "feedId" -> feedId,
      "userId" -> userId,
      "read" -> false
    )
  )

  def countRecommendFeed(feedId: ObjectId, userId: ObjectId) = count(
    MongoDBObject(
      "feedId" -> feedId,
      "userId" -> userId,
      "read" -> false,
      "recommend" -> true
    )
  )

  def mark(newsId: String, clicked: Boolean) {
    findOne(MongoDBObject(
      "newsId" -> newsId
    )).map(userNews => {
      UserNewsDao.save(userNews.copy(read = true, clicked = clicked))
    })
  }

}
