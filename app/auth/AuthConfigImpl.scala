package auth

import play.api.mvc._
import play.api.mvc.Results._
import controllers.routes
import dao.UserDao
import scala.reflect._
import jp.t2v.lab.play2.auth.{CookieIdContainer, IdContainer, AuthConfig}
import play.api.mvc.AcceptExtractors
import model.{Administrator, Permission}

/**
 * The Class AuthConfigImpl.
 *
 * @author Nguyen Duc Dung
 * @since 11/6/12 10:10 AM
 *
 */
trait AuthConfigImpl extends AuthConfig with Rendering with AcceptExtractors {

  type Id = String

  type User = vn.myfeed.model.User

  type Authority = Permission

  def sessionTimeoutInSeconds = 3600

  def resolveUser(id: Id) = UserDao.findByUserName(id)

  implicit val idTag: ClassTag[Id] = classTag[Id]

  def loginSucceeded(request: RequestHeader) = Redirect(routes.Feeds.index())

  def logoutSucceeded(request: RequestHeader) = Redirect(routes.Feeds.index())

  def authenticationFailed(request: RequestHeader) = {
    render {
      case Accepts.Json() => Forbidden("Please login")
      case _ => Redirect(routes.Login.login())
    }(request)
  }

  def authorizationFailed(request: RequestHeader) = Forbidden("Please login")

  def authorize(user: User, authority: Authority) = {
    val login = UserDao.login(user.username, user.password)
    login && user.role == authority.value || login && user.role == Administrator.value
  }

  override lazy val idContainer: IdContainer[Id] = new CookieIdContainer[Id]
}