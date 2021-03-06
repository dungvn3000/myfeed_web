package dao

import com.novus.salat.dao.SalatDAO
import vn.myfeed.model.Feed
import org.bson.types.ObjectId
import se.radley.plugin.salat._
import com.mongodb.casbah.commons.MongoDBObject
import play.api.Play.current

/**
 * The Class FeedDao.
 *
 * @author Nguyen Duc Dung
 * @since 5/1/13 4:59 AM
 *
 */
object FeedDao extends BaseDao[Feed, ObjectId] {

  def dao = new SalatDAO[Feed, ObjectId](mongoCollection("feed")) {}

  def findByUrl(url: String) = findOne(MongoDBObject("url" -> url))

}
