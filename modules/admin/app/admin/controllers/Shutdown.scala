package admin.controllers

import play.api.mvc.Controller
import jp.t2v.lab.play2.auth.Auth
import auth.AuthConfigImpl
import model.Administrator
import play.api.Play

/**
 * The Class Shutdown.
 *
 * @author Nguyen Duc Dung
 * @since 6/5/13 6:34 AM
 *
 */
object Shutdown extends Controller with Auth with AuthConfigImpl {

  /**
   * This method is using for shutdown application for maintenance
   * @return
   */
  def index = authorizedAction(Administrator)(implicit user => implicit request => {
    Play.stop()
    System.exit(0)
    Ok("Goodbye!")
  })

}
