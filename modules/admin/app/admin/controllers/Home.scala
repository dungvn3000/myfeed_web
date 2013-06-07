package admin.controllers

import play.api.mvc.Controller
import jp.t2v.lab.play2.auth.Auth
import auth.AuthConfigImpl
import model.Administrator

/**
 * The Class Home.
 *
 * @author Nguyen Duc Dung
 * @since 6/5/13 5:53 AM
 *
 */
object Home extends Controller with Auth with AuthConfigImpl {

  def navBar(implicit user: User) = admin.views.html.partials.navbar(user)

  def index = authorizedAction(Administrator)(implicit user => implicit request => {
    Ok(admin.views.html.home(navBar))
  })

}
