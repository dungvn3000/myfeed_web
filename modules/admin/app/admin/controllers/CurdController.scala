package admin.controllers

import model.Administrator
import dto.FormErrorDto
import play.api.libs.json.Json
import curd.FormBuilder
import play.api.data.FormError
import dao.BaseDao
import play.api.mvc.Result

/**
 * The Class EditUpdateController.
 *
 * @author Nguyen Duc Dung
 * @since 6/7/13 8:50 PM
 *
 */
trait CurdController[ObjectType <: AnyRef, ID <: Any] extends BaseController[ObjectType, ID] {

  val formBuilder: FormBuilder[ObjectType]

  val dao: BaseDao[ObjectType, ID]

  override protected def routes(view: String): Result = view match {
    case "detail" => Ok(curd.views.html.detail(formBuilder.build()))
    case _ => super.routes(view)
  }

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
   * Custom validation
   * @param entity
   * @return
   */
  def customValidate(entity: ObjectType): Option[Seq[FormError]] = None
}
