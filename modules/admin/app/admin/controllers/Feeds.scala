package admin.controllers

import controllers.RestFullController
import org.bson.types.ObjectId
import model.Administrator
import jp.t2v.lab.play2.auth.Auth
import auth.AuthConfigImpl
import vn.myfeed.model.Feed
import play.api.libs.json._
import plugin.ObjectIdFormat._
import plugin.String2Int
import dao.FeedDao

/**
 * The Class Feeds.
 *
 * @author Nguyen Duc Dung
 * @since 6/5/13 8:09 AM
 *
 */
object Feeds extends RestFullController[ObjectId] with Auth with AuthConfigImpl {

  implicit val personReads: Writes[Feed] = Json.writes[Feed]

  lazy val routes = Map(
    "index" -> Ok(admin.views.html.partials.feed.index()),
    "list" -> Ok(admin.views.html.partials.feed.list()),
    "detail" -> Ok(admin.views.html.partials.feed.detail())
  )

  def partials(view: String) = authorizedAction(Administrator)(implicit user => implicit request => {
    routes(view)
  })

  /**
   * GET /entity
   * return a list of all records
   * @return
   */
  override def query = authorizedAction(Administrator)(implicit user => implicit request => {
    val field = request.getQueryString("f").getOrElse("name")
    val value = request.getQueryString("v").getOrElse("")
    val sort = request.getQueryString("s").getOrElse("_id")
    val order = request.getQueryString("o").collect {
      case String2Int(o) => o
    }.getOrElse(1)
    val page = request.getQueryString("p").collect {
      case String2Int(p) => p
    }.getOrElse(1)
    val limit = request.getQueryString("l").collect {
      case String2Int(l) => l
    }.getOrElse(10)
    Ok(Json.toJson(FeedDao.query(field, value, sort, order, page, limit)))
  })

  /**
   * GET /entity/1
   * return the first record
   * @param id
   * @return
   */
  override def show(id: ObjectId) = authorizedAction(Administrator)(implicit user => implicit request => {
    Ok("545454")
  })

}
