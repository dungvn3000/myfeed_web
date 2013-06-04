package controllers

import play.api.mvc._
import auth.AuthConfigImpl
import play.api.data._
import play.api.data.Forms._
import jp.t2v.lab.play2.auth.LoginLogout
import dao.UserDao
import concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import vn.myfeed.model.User
import model.NormalUser

/**
 * The Class LoginController.
 *
 * @author Nguyen Duc Dung
 * @since 11/5/12 11:04 PM
 *
 */
object Login extends Controller with LoginLogout with AuthConfigImpl {

  val loginForm = Form {
    tuple(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText
    ) verifying("login.failed", fields => fields match {
      case (username, password) => UserDao.login(username, password)
    })
  }

  val signUpForm = Form {
    tuple(
      "username" -> text(minLength = 6).verifying("username.exits", username => {
        UserDao.findByUserName(username).isEmpty
      }),
      "password" -> text(minLength = 6),
      "rePassword" -> text(minLength = 6),
      "email" -> email.verifying("email.exits", email => {
        UserDao.findByEmail(email).isEmpty
      })
    ) verifying("password.notMatch", fields => fields match {
      case (username, password, rePassword, email) => rePassword == password
    })
  }

  def login = Action {
    Ok(views.html.login(loginForm))
  }

  def signup = Action {
    Ok(views.html.signup(signUpForm))
  }

  def register = Action(implicit request => {
    val futureInt = scala.concurrent.Future {
      val bindForm = signUpForm.bindFromRequest
      bindForm.fold(
        error => BadRequest(views.html.signup(error)),
        form => form match {
          case (username, password, rePassWord, email) => {
            UserDao.insert(User(
              username = username,
              password = password,
              email = email,
              role = NormalUser.value
            ))
            gotoLoginSucceeded(username)
          }
        }
      )
    }
    Async {
      futureInt.map(i => i)
    }
  })

  def auth = Action(implicit request => {
    loginForm.bindFromRequest.fold(
      error => Ok(views.html.login(error)),
      form => {
        gotoLoginSucceeded(form._1)
      }
    )
  })

  def logout = Action(implicit request => {
    gotoLogoutSucceeded
  })


}
