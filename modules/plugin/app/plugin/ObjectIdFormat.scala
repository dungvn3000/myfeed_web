package plugin

import play.api.data.format.Formats._
import play.api.data.format.Formatter
import org.bson.types.ObjectId
import play.api.libs.json._
import play.api.libs.json.JsSuccess
import play.api.data.FormError
import play.api.libs.json.JsString
import scala.Some

/**
 * The Class Format.
 *
 * @author Nguyen Duc Dung
 * @since 12/31/12 2:04 PM
 *
 */
object ObjectIdFormat {

  /**
   * For a form
   * @return
   */
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


  /**
   * For json
   */
  implicit val objectIdFormat: Format[ObjectId] = new Format[ObjectId] {
    def reads(json: JsValue) = {
      json match {
        case jsString: JsString => {
          if (ObjectId.isValid(jsString.value)) JsSuccess(new ObjectId(jsString.value))
          else JsError("Invalid ObjectId")
        }
        case other => JsError("Can't parse json path as an ObjectId. Json content = " + other.toString())
      }
    }

    def writes(oId: ObjectId): JsValue = {
      JsString(oId.toString)
    }
  }

}
