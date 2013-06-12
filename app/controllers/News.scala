package controllers

import org.bson.types.ObjectId
import jp.t2v.lab.play2.auth.Auth
import auth.AuthConfigImpl
import model.NormalUser
import dao.UserNewsDao
import play.api.libs.json.Json

/**
 * The Class News.
 *
 * @author Nguyen Duc Dung
 * @since 6/12/13 12:17 PM
 *
 */
object News extends RestFullController[ObjectId] with Auth with AuthConfigImpl {

  /**
   * GET /entity/1
   * return the first record
   * @param id
   * @return
   */
  override def get(id: ObjectId) = authorizedAction(NormalUser)(implicit user => implicit request => {
    UserNewsDao.findNewsByUserNewsId(id).map(news => Ok(Json.toJson(news))).getOrElse(NotFound)
  })

}
