package controllers

import play.api.mvc.{Action, Controller}
import jp.t2v.lab.play2.auth.LoginLogout
import auth.AuthConfigImpl
import play.api.libs.ws.WS
import util.matching.Regex
import concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import play.api.{Play, PlayException}
import play.api.data.Form
import play.api.data.Forms._
import play.api.Play.current
import org.apache.commons.lang.StringUtils
import com.restfb.DefaultFacebookClient
import dao.UserDao

/**
 * The Class FacebookController.
 *
 * @author Nguyen Duc Dung
 * @since 2/26/13 3:26 PM
 *
 */
object Facebook extends Controller with LoginLogout with AuthConfigImpl {

  val app_id = "143358922499264"
  val app_secret = "7c3dce86f87a0786adc0e996e89288ec"
  val redirect_url = Play.configuration.getString("application.redirect.url").getOrElse(throw new Exception("Please add application.redirect.url to application.conf"))

  val signUpForm = Form {
    tuple(
      "username" -> text,
      "password" -> text,
      "rePassword" -> text,
      "email" -> text
    )
  }

  def login = Action(implicit request => {
    val url = s"https://www.facebook.com/dialog/oauth?client_id=$app_id&redirect_uri=$redirect_url&scope=email"
    Redirect(url)
  })

  def auth(code: String) = Action(implicit request => {
    if (!StringUtils.isBlank(code)) {
      val accessTokenUrl = s"https://graph.facebook.com/oauth/access_token?client_id=$app_id&client_secret=$app_secret&code=$code&redirect_uri=$redirect_url"
      Async {
        WS.url(accessTokenUrl).get().map(response => {
          val regex = new Regex("access_token=(.*)&expires=(.*)")
          response.body match {
            case regex(accessToken, expires) => {
              val facebookClient = new DefaultFacebookClient(accessToken)
              val fbUser = facebookClient.fetchObject("me", classOf[com.restfb.types.User])
              UserDao.findByUserName(fbUser.getUsername).map(user => gotoLoginSucceeded(user.username)).getOrElse({
                val bindForm = signUpForm.bind(Map(
                  "username" -> fbUser.getUsername,
                  "email" -> fbUser.getEmail
                )).discardingErrors
                Ok(views.html.signup(bindForm))
              })
            }
            case _ => {
              throw new PlayException("Login error", "Some thing goes wrong please try again")
            }
          }
        })
      }
    } else {
      Redirect(routes.Feeds.index())
    }
  })
}
