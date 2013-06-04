package dto

import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.data.FormError
import play.api.i18n.Messages

/**
 * The Class FormErrorDto.
 *
 * @author Nguyen Duc Dung
 * @since 5/17/13 9:22 AM
 *
 */
case class FormErrorDto(key: String, msg: String)

object FormErrorDto {

  def apply(formError: FormError) = new FormErrorDto(
    key = formError.key,
    msg = Messages(formError.message, formError.args)
  )

  implicit val jsonWrite = (
    (__ \ "key").write[String] ~
      (__ \ "msg").write[String]
    )(unlift(FormErrorDto.unapply))

}