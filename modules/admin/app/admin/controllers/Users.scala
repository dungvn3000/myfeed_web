package admin.controllers

import org.bson.types.ObjectId
import dao.UserDao
import curd.{TableBuilder, FormBuilder}
import play.api.data.Form
import play.api.data.Forms._
import vn.myfeed.model.User
import model.Role
import plugin.ObjectIdFormat._
import play.api.i18n.Messages

/**
 * The Class Users.
 *
 * @author Nguyen Duc Dung
 * @since 6/7/13 3:46 PM
 *
 */
object Users extends BaseController[User, ObjectId] {

  val pageTitle = Messages("user.title")

  val dao = UserDao

  lazy val formBuilder = new FormBuilder[User]("User Form") {
    val form = Form(
      mapping(
        textField("_id", 'hidden -> true) -> of[ObjectId],
        textField("username", '_label -> "Username *") -> text(minLength = 6, maxLength = 50),
        textField("password", '_label -> "Password *") -> text(minLength = 6, maxLength = 50),
        textField("email", '_label -> "Email *") -> email,
        selectField("role", Role.asSelectValue(), '_label -> "Role") -> text
      )(User.apply)(User.unapply)
    )
  }

  lazy val tableBuilder = new TableBuilder[User] {
    column("username", "Username")
    column("password", "Password")
    column("email", "Email")
    column("role", "Role")
  }

}
