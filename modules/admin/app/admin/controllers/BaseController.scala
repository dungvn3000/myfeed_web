package admin.controllers

import controllers.RestFullController
import jp.t2v.lab.play2.auth.Auth
import auth.AuthConfigImpl
import play.api.libs.json._
import dto.FormErrorDto
import model.Administrator
import plugin.String2Int
import dao.BaseDao
import curd.{TableBuilder, FormBuilder}
import dto.DataTable
import play.api.data.FormError

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

  val formBuilder: FormBuilder[ObjectType]

  val tableBuilder: TableBuilder[ObjectType]

  lazy val routes = Map(
    "index" -> Ok(curd.views.html.index()),
    "list" -> Ok(curd.views.html.list(tableBuilder)),
    "detail" -> Ok(curd.views.html.detail(formBuilder.build()))
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
   * PUT /entity/1
   * submit fields for updating the first record
   * @param id
   * @return
   */
  override def update(id: ID) = create

  /**
   * POST /entity
   * submit fields for creating a new record
   * @return
   */
  override def create = authorizedAction(Administrator)(implicit user => implicit request => {
    formBuilder.form.bindFromRequest.fold(
      fromError => {
        val error = fromError.errors.map(FormErrorDto(_))
        BadRequest(Json.toJson(error))
      },
      data => {
        customValidate(data) match {
          case None => {
            dao.save(data)
            Ok
          }
          case Some(errors) => {
            val error = errors.map(FormErrorDto(_))
            BadRequest(Json.toJson(error))
          }
        }
      }
    )
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

  /**
   * Custom validation
   * @param entity
   * @return
   */
  def customValidate(entity: ObjectType): Option[Seq[FormError]] = None

}
