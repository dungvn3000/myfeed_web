package dao

import com.novus.salat.dao.SalatDAO
import vn.myfeed.model.UserFeed
import org.bson.types.ObjectId
import se.radley.plugin.salat._
import play.api.Play.current
import com.mongodb.casbah.commons.MongoDBObject

/**
 * The Class FeedDao.
 *
 * @author Nguyen Duc Dung
 * @since 5/1/13 4:59 AM
 *
 */
object UserFeedDao extends SalatDAO[UserFeed, ObjectId](mongoCollection("userFeed")) {

  def getUserFeed(userId: ObjectId) = find(MongoDBObject(
    "userId" -> userId
  )).toList

}
