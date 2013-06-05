package admin.controllers

import play.api.Play
import play.api.Play.current

/**
 * The Class Assets.
 *
 * @author Nguyen Duc Dung
 * @since 6/5/13 6:24 AM
 *
 */
object Assets extends controllers.AssetsBuilder {
  override def at(path: String, file: String) = {
    val resourceName = Option(path + "/" + file).map(name => if (name.startsWith("/")) name else ("/" + name)).get
    Play.resource(resourceName).map(url => println(url))
    super.at(path, file)
  }
}
