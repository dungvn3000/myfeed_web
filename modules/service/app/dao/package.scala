import com.novus.salat.Context

/**
 * The Class package.
 *
 * @author Nguyen Duc Dung
 * @since 5/22/13 8:11 PM
 *
 */
package object dao {

  implicit val ctx = new Context {
    val name = "global"
  }

}
