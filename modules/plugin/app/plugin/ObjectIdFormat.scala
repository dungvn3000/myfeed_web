package plugin

import play.api.data.format.Formats._
import play.api.data.format.Formatter
import play.api.data.FormError
import org.bson.types.ObjectId

/**
 * The Class Format.
 *
 * @author Nguyen Duc Dung
 * @since 12/31/12 2:04 PM
 *
 */
object ObjectIdFormat {

  implicit def objectIdFormatter: Formatter[ObjectId] = new Formatter[ObjectId] {

    override val format = Some("format.objectid", Nil)

    def unbind(key: String, value: ObjectId) = Map(key -> value.toString)

    def bind(key: String, data: Map[String, String]) = stringFormat.bind(key, data).right.flatMap(s =>
      scala.util.control.Exception.allCatch[ObjectId].either {
        if (!s.isEmpty) {
          new ObjectId(s)
        } else {
          new ObjectId()
        }
      }.left.map(e => Seq(FormError(key, "error.id", Nil)))
    )
  }

}
