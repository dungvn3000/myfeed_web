package dao

import se.radley.plugin.salat._
import play.api.Play.current
import com.novus.salat.dao.SalatDAO
import org.linkerz.model.News
import org.bson.types.ObjectId
import com.mongodb.casbah.commons.MongoDBObject

/**
 * The Class LinkDao.
 *
 * @author Nguyen Duc Dung
 * @since 12/16/12 6:58 PM
 *
 */
object NewsDao extends SalatDAO[News, String](mongoCollection("news")) {

  def findLinksByFeedId(feedId: ObjectId, page: Int = 1, itemDisplay: Int = 10) = {
    val skip = (page - 1) * itemDisplay
    find(MongoDBObject("feedId" -> feedId))
      .skip(skip)
      .limit(itemDisplay)
      .sort(MongoDBObject("createdDate" -> -1))
      .toList
  }

  def countByFeed(feedId: ObjectId) = count(MongoDBObject("feedId" -> feedId))

}
