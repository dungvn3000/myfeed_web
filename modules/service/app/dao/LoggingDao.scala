package dao

import com.novus.salat.dao.SalatDAO
import vn.myfeed.model.Logging
import org.bson.types.ObjectId
import se.radley.plugin.salat._
import play.api.Play.current


/**
 * The Class LoggingDao.
 *
 * @author Nguyen Duc Dung
 * @since 5/7/13 12:07 AM
 *
 */
object LoggingDao extends BaseDao[Logging, ObjectId] {
  def dao = new SalatDAO[Logging, ObjectId](mongoCollection("logging")){}
}
