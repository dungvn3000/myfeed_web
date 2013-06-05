package admin.controllers

import play.api.mvc.Controller
import jp.t2v.lab.play2.auth.Auth
import auth.AuthConfigImpl
import model.Administrator
import play.api.Play

/**
 * The Class Maintenance.
 *
 * @author Nguyen Duc Dung
 * @since 6/5/13 6:34 AM
 *
 */
object Maintenance extends Controller with Auth with AuthConfigImpl {

  lazy val routes = Map(
    "index" -> Ok(admin.views.html.partials.maintenance.index())
  )

  def partials(view: String) = authorizedAction(Administrator)(implicit user => implicit request => {
    routes(view)
  })

  /**
   * This method is using for shutdown application for maintenance
   * @return
   */
  def shutdown = authorizedAction(Administrator)(implicit user => implicit request => {
    Play.stop()
    System.exit(0)
    Ok("Goodbye!")
  })

}
