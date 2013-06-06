package form

import play.api.data.Form
import play.api.data.Forms._
import validation.Constraint._
import vn.myfeed.model.Feed
import org.bson.types.ObjectId
import plugin.ObjectIdFormat._

/**
 * The Class FeedForm.
 *
 * @author Nguyen Duc Dung
 * @since 6/6/13 3:46 AM
 *
 */
object Forms {

  lazy val feedForm = Form(
    mapping(
      "id" -> ignored[ObjectId](new ObjectId()),
      "name" -> nonEmptyText,
      "url" -> nonEmptyText.verifying(urlConstraint),
      "topic" -> optional(text)
    )(Feed.apply)(Feed.unapply)
  )

}
