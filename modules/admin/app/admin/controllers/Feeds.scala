package admin.controllers

import controllers.RestFullController
import org.bson.types.ObjectId
import model.Administrator
import jp.t2v.lab.play2.auth.Auth
import auth.AuthConfigImpl
import vn.myfeed.model.Feed
import play.api.libs.json._
import plugin.String2Int
import dao.FeedDao
import dto.{FormErrorDto, DataTable}
import form.Forms
import curd.TableBuilder
import Forms._
import plugin.ObjectIdFormat._

/**
 * The Class Feeds.
 *
 * @author Nguyen Duc Dung
 * @since 6/5/13 8:09 AM
 *
 */
object Feeds extends RestFullController[ObjectId] with Auth with AuthConfigImpl {

  implicit val feedWriter: Writes[Feed] = Json.writes[Feed]
  implicit val dataWriter: Writes[DataTable] = Json.writes[DataTable]

  object FeedTable extends TableBuilder {
    column("name", "Name")
    column("url", "Url")
    column("topic", "Topic")
  }

  lazy val routes = Map(
    "index" -> Ok(curd.views.html.index()),
    "list" -> Ok(curd.views.html.list(FeedTable)),
    "detail" -> Ok(curd.views.html.detail(feedFormBuilder.build()))
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
    val totalPage = FeedDao.totalPage(field, value, limit)
    val data = DataTable(
      field = field,
      value = value,
      sort = sort,
      order = order,
      page = page,
      limit = limit,
      totalPage = totalPage,
      data = Json.toJson(FeedDao.query(field, value, sort, order, page, limit))
    )
    Ok(Json.toJson(data))
  })


  /**
   * GET /entity/1
   * return the first record
   * @param id
   * @return
   */
  override def get(id: ObjectId) = authorizedAction(Administrator)(implicit user => implicit request => {
    Ok(Json.toJson(FeedDao.findOneById(id)))
  })

  /**
   * PUT /entity/1
   * submit fields for updating the first record
   * @param id
   * @return
   */
  override def update(id: ObjectId) = authorizedAction(Administrator)(implicit user => implicit request => {
    feedFormBuilder.form.bindFromRequest.fold(
      fromError => {
        val error = fromError.errors.map(FormErrorDto(_))
        BadRequest(Json.toJson(error))
      },
      data => {
        FeedDao.save(data.copy(_id = id))
        Ok
      }
    )
  })

  /**
   * POST /entity
   * submit fields for creating a new record
   * @return
   */
  override def create = authorizedAction(Administrator)(implicit user => implicit request => {
    feedFormBuilder.form.bindFromRequest.fold(
      fromError => {
        val error = fromError.errors.map(FormErrorDto(_))
        BadRequest(Json.toJson(error))
      },
      data => {
        FeedDao.insert(data.copy(_id = new ObjectId()))
        Ok
      }
    )
  })

  /**
   * DELETE /entity/1
   * destroy the first record
   * @param id
   * @return
   */
  override def delete(id: ObjectId) = authorizedAction(Administrator)(implicit user => implicit request => {
    FeedDao.removeById(id)
    Ok
  })
}
