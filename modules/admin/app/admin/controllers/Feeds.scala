package admin.controllers

import play.api.mvc.Action
import controllers.RestFullController
import org.bson.types.ObjectId
import model.{Administrator, NormalUser}
import jp.t2v.lab.play2.auth.Auth
import auth.AuthConfigImpl

/**
 * The Class Feeds.
 *
 * @author Nguyen Duc Dung
 * @since 6/5/13 8:09 AM
 *
 */
object Feeds extends RestFullController[ObjectId] with Auth with AuthConfigImpl {

  lazy val routes = Map(
    "feeds" -> Ok(admin.views.html.partials.feed()),
    "list" -> Ok(admin.views.html.partials.feedList())
  )

  def partials(view: String) = authorizedAction(Administrator)(implicit user => implicit request => {
    routes(view)
  })

}
