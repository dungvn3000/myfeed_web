package controllers

import play.api.mvc.{AnyContent, Action, Controller}

/**
 * The Class RestFullSupport.
 *
 * @author Nguyen Duc Dung
 * @since 6/5/13 8:19 AM
 *
 */
trait RestFullController[Id] extends Controller {

  /**
   * GET /entity
   * return a list of all records
   * @return
   */
  def all(): Action[AnyContent] = TODO

  /**
   * GET /entity/new
   * return a form for creating a new record
   * @return
   */
  def add(): Action[AnyContent] = TODO

  /**
   * POST /entity
   * submit fields for creating a new record
   * @return
   */
  def create(): Action[AnyContent] = TODO

  /**
   * GET /entity/1
   * return the first record
   * @param id
   * @return
   */
  def show(id: Id): Action[AnyContent] = TODO

  /**
   * GET /entity/1/edit
   * return a form to edit the first record
   * @param id
   * @return
   */
  def edit(id: Id): Action[AnyContent] = TODO

  /**
   * PUT /entity/1
   * submit fields for updating the first record
   * @param id
   * @return
   */
  def update(id: Id): Action[AnyContent] = TODO

  /**
   * DELETE /entity/1
   * destroy the first record
   * @param id
   * @return
   */
  def delete(id: Id): Action[AnyContent] = TODO

}
