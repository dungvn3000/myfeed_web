package model

/**
 * The Class Permission.
 *
 * @author Nguyen Duc Dung
 * @since 11/6/12 10:13 AM
 *
 */
case class Permission(value: String)

object Administrator extends Permission("Administrator")

object NormalUser extends Permission("NormalUser")

object Role {
  def asSelectValue() = Seq(
    NormalUser.value -> NormalUser.value,
    Administrator.value -> Administrator.value
  )
}