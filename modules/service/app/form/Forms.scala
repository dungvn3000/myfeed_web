package form

import play.api.data.Form
import play.api.data.Forms._
import validation.Constraint._
import vn.myfeed.model.Feed
import org.bson.types.ObjectId
import plugin.ObjectIdFormat._
import curd.FormBuilder

/**
 * The Class FeedForm.
 *
 * @author Nguyen Duc Dung
 * @since 6/6/13 3:46 AM
 *
 */
object Forms {

  val feedFormBuilder: FormBuilder[Feed] = new FormBuilder[Feed]("Feed Form") {
    val form = Form(
      mapping(
        "id" -> ignored[ObjectId](new ObjectId()),
        textField("name", '_label -> "Name *") -> nonEmptyText,
        textField("url", '_label -> "Url *") -> nonEmptyText.verifying(urlConstraint),
        textField("topic", '_label -> "Topic") -> optional(text)
      )(Feed.apply)(Feed.unapply)
    )
  }

}
