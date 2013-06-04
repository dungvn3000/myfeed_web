import sbt._
import sbt.Keys._
import play.Project._

object MyFeedBuild extends Build {

  val appName = "myfeed"
  val appVersion = "0.0.1"

  val appDependencies = Seq(
    "org.linkerz" %% "linkerz_model" % "0.1-SNAPSHOT",
    "se.radley" %% "play-plugins-salat" % "1.2",
    "joda-time" % "joda-time" % "2.1",
    "jp.t2v" %% "stackable-controller" % "0.2",
    "jp.t2v" %% "play2.auth" % "0.9",
    "jp.t2v" %% "play2.auth.test" % "0.9" % "test",
    "com.restfb" % "restfb" % "1.6.11",
    "com.github.seratch" %% "inputvalidator" % "[0.2,)",
    "com.github.seratch" %% "inputvalidator-play" % "[0.2,)",
    "commons-validator" % "commons-validator" % "1.4.0" exclude("commons-beanutils", "commons-beanutils")
  )

  val appResolvers = Seq(
    "teamon.eu repo" at "http://repo.teamon.eu",
    "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
    "Spray repo" at "http://repo.spray.io",
    "t2v.jp repo" at "http://www.t2v.jp/maven-repo/",
    "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository",
    Resolver.file("Local Repository", file(Path.userHome.absolutePath + "/.ivy2/local"))(Resolver.ivyStylePatterns)
  )

  val plugin = play.Project(appName + "_plugin", appVersion, appDependencies, path = file("modules/plugin")).settings(
    resolvers ++= appResolvers
  )

  val service = play.Project(appName + "_service", appVersion, appDependencies, path = file("modules/service")).settings(
    resolvers ++= appResolvers
  )

  val admin = play.Project(appName + "_admin", appVersion, appDependencies, path = file("modules/admin")).settings(
    resolvers ++= appResolvers
  ).dependsOn(plugin, service)

  val main = play.Project(appName, appVersion, appDependencies).settings(
    routesImport ++= Seq(
      "se.radley.plugin.salat.Binders._"
    ),
    templatesImport ++= Seq(
      "org.bson.types.ObjectId",
      "org.linkerz.model._",
      "dto._",
      "model._"
    ),
    resolvers ++= appResolvers
  ).dependsOn(admin)

}
