package dao

import se.radley.plugin.salat._
import play.api.Play.current
import com.mongodb.casbah.commons.MongoDBObject
import com.novus.salat.dao.SalatDAO
import org.bson.types.ObjectId
import org.linkerz.model.User

object UserDao extends SalatDAO[User, ObjectId](collection = mongoCollection("user")) {

  def findByUserName(username: String) = findOne(MongoDBObject("username" -> username))

  def findByEmail(email: String) = findOne(MongoDBObject("email" -> email))

  def login(username: String, password: String): Boolean = {
    val user = findByUserName(username)
    if (user.isDefined && user.get.password == password) true else false
  }

}