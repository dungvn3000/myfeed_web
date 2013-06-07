package admin.controllers

import controllers.RestFullController
import jp.t2v.lab.play2.auth.Auth
import auth.AuthConfigImpl
import play.api.libs.json._
import model.Administrator
import plugin.String2Int
import dao.BaseDao
import curd.TableBuilder
import dto.DataTable
import play.api.mvc.{Result, AnyContent, Action}

/**
 * The Class BaseController.
 *
 * @author Nguyen Duc Dung
 * @since 6/7/13 2:16 PM
 *
 */
trait BaseController[ObjectType <: AnyRef, ID <: Any] extends RestFullController[ID] with Auth with AuthConfigImpl {

  implicit val dataWriter: Writes[DataTable] = Json.writes[DataTable]

  val dao: BaseDao[ObjectType, ID]

  val tableBuilder: TableBuilder[ObjectType]

  val pageTitle: String

  protected def routes(view: String): Result = view match {
    case "index" => Ok(curd.views.html.index(pageTitle))
    case "list" => Ok(curd.views.html.list(tableBuilder))
    case _ => NotFound
  }

  def partials(view: String) = authorizedAction(Administrator)(implicit user => implicit request => {
    routes(view)
  })

  /**
   * GET /entity
   * return a list of all records
   * @return
   */
  override def query = authorizedAction(Administrator)(implicit user => implicit request => {
    val field = request.getQueryString("f").getOrElse(tableBuilder.cols(0).key)
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
    val totalPage = dao.totalPage(field, value, limit)
    val data = DataTable(
      field = field,
      value = value,
      sort = sort,
      order = order,
      page = page,
      limit = limit,
      totalPage = totalPage,
      data = Json.parse(dao.toCompactJSONArray(dao.query(field, value, sort, order, page, limit)))
    )
    Ok(Json.toJson(data))
  })

  /**
   * GET /entity/1
   * return the first record
   * @param id
   * @return
   */
  override def get(id: ID) = authorizedAction(Administrator)(implicit user => implicit request => {
    dao.findOneById(id).map(entity => {
      Ok(dao.toCompactJson(entity))
    }).getOrElse(NotFound)
  })


  /**
   * DELETE /entity/1
   * destroy the first record
   * @param id
   * @return
   */
  override def delete(id: ID) = authorizedAction(Administrator)(implicit user => implicit request => {
    dao.removeById(id)
    Ok
  })


}
