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
  def query: Action[_] = ???

  /**
   * POST /entity
   * submit fields for creating a new record
   * @return
   */
  def create: Action[_] = ???

  /**
   * GET /entity/1
   * return the first record
   * @param id
   * @return
   */
  def get(id: Id): Action[_] = ???

  /**
   * PUT /entity/1
   * submit fields for updating the first record
   * @param id
   * @return
   */
  def update(id: Id): Action[_] = ???

  /**
   * DELETE /entity/1
   * destroy the first record
   * @param id
   * @return
   */
  def delete(id: Id): Action[_] = ???

}
