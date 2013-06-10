package admin.controllers

import play.api.mvc.{WebSocket, Controller}
import jp.t2v.lab.play2.auth.Auth
import auth.AuthConfigImpl
import model.Administrator
import play.api.Play
import play.api.i18n.Messages
import play.api.libs.iteratee._
import play.api.libs.concurrent.Akka
import play.api.Play.current
import admin.actor.Join
import akka.util.Timeout
import akka.pattern.ask
import play.api.libs.json.JsValue
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import admin.actor.Connected
import play.api.libs.json.JsObject
import play.api.libs.json.JsString

/**
 * The Class Maintenance.
 *
 * @author Nguyen Duc Dung
 * @since 6/5/13 6:34 AM
 *
 */
object Maintenance extends Controller with Auth with AuthConfigImpl {

  lazy val routes = Map(
    "index" -> Ok(admin.views.html.partials.maintenance.index(Messages("maintenance.title")))
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

  def metric = WebSocket.async[JsValue](implicit request => {
    //check user
    authorized(Administrator)
    implicit val timeout = Timeout(1 second)
    val actor = Akka.system.actorFor("akka://application/user/metricActor")
    (actor ? Join).map {
      case Connected(channel) => {
        // Just consume and ignore the input
        val in = Iteratee.ignore[JsValue]
        (in, channel)
      }
      case _ => {
        // Connection error
        // A finished Iteratee sending EOF
        val iteratee = Done[JsValue,Unit]((),Input.EOF)
        // Send an error and close the socket
        val enumerator =  Enumerator[JsValue](JsObject(Seq("error" -> JsString("error")))).andThen(Enumerator.enumInput(Input.EOF))
        (iteratee,enumerator)
      }
    }
  })

}
