package controllers

import _root_.validation.Constraint
import play.api.mvc.Controller
import jp.t2v.lab.play2.auth.Auth
import auth.AuthConfigImpl
import dao.{UserNewsDao, UserFeedDao, FeedDao}
import dto.{FormErrorDto, FeedDto}
import play.api.libs.json.Json
import play.api.i18n.Messages
import org.bson.types.ObjectId
import play.api.data._
import play.api.data.Forms._
import Constraint._
import org.linkerz.model.{UserFeed, Feed}
import model.NormalUser

/**
 * The Class Feeds.
 *
 * @author Nguyen Duc Dung
 * @since 5/12/13 2:38 AM
 *
 */
object Feeds extends Controller with Auth with AuthConfigImpl {

  lazy val routes = Map(
    "feeds" -> Ok(views.html.partials.feeds()),
    "list" -> Ok(views.html.partials.feedList()),
    "detail" -> Ok(views.html.partials.feedDetail()),
    "add" -> Ok(views.html.partials.feedAdd())
  )

  def partials(view: String) = authorizedAction(NormalUser)(implicit user => implicit request => {
    routes(view)
  })

  def title = Messages("application.title")

  def navBar(implicit user: User) = views.html.partials.navbar(user)

  def index = authorizedAction(NormalUser)(implicit user => implicit request => {
    Ok(views.html.home(title, navBar))
  })

  def all = authorizedAction(NormalUser)(implicit user => implicit request => {
    val feeds = UserFeedDao.getUserFeed(user._id).map(userFeed => {
      val feed = FeedDao.findOneById(userFeed.feedId).getOrElse(throw new Exception("Some things go wrong"))
      val unread = UserNewsDao.countUnreadByFeed(userFeed.feedId, user._id)
      val recommend = UserNewsDao.countRecommendFeed(userFeed.feedId, user._id)
      FeedDto(feed, unread, recommend)
    })
    Ok(Json.toJson(feeds))
  })

  def getNews(feedId: ObjectId, recommend: Boolean, page: Int, limit: Int) = authorizedAction(NormalUser)(implicit user => implicit request => {
    val news = UserNewsDao.findLinksByFeedId(feedId, recommend, user._id, page, limit)
    Ok(Json.toJson(news))
  })

  def mark = authorizedAction(NormalUser)(implicit user => implicit request => {
    Form(tuple(
      "newsId" -> nonEmptyText,
      "clicked" -> boolean
    )).bindFromRequest.fold(
      error => BadRequest("newsId can't be empty"),
      data => {
        UserNewsDao.mark(data._1, data._2)
        Ok
      }
    )
  })

  lazy val feedForm = Form(
    tuple(
      "id" -> ignored[ObjectId](new ObjectId),
      "name" -> nonEmptyText,
      "url" -> nonEmptyText.verifying(urlConstraint)
    )
  )

  def add = authorizedAction(NormalUser)(implicit user => implicit request => {
    feedForm.bindFromRequest.fold(
      fromError => {
        val error = fromError.errors.map(FormErrorDto(_))
        BadRequest(Json.toJson(error))
      },
      formData => formData match {
        case (id, name, url) => {
          val feed = FeedDao.findByUrl(url).getOrElse {
            val newFeed = Feed(url = url, name = name)
            FeedDao.insert(newFeed)
            newFeed
          }
          val userFeed = UserFeed(
            feedId = feed._id,
            name = name,
            userId = user._id
          )
          UserFeedDao.save(userFeed)
          Ok
        }
      }
    )
  })

}
