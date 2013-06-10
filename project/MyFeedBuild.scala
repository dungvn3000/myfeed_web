import com.typesafe.sbtidea.SbtIdeaPlugin
import sbt._
import play.Project._
import sbt.Project._
import Keys._

object MyFeedBuild extends Build {

  val appOrganization = "vn.myfeed"
  val appName = "myfeed_web"
  val appVersion = "0.1.0-SNAPSHOT"
  val appScalaVersion = "2.10.0"

  val appDependencies = Seq(
    "com.codahale.metrics" % "metrics-core" % "3.0.0-RC1",
    "vn.myfeed" %% "model" % "0.1.0-SNAPSHOT",
    "se.radley" %% "play-plugins-salat" % "1.2",
    "jp.t2v" %% "stackable-controller" % "0.2",
    "jp.t2v" %% "play2.auth" % "0.9",
    "jp.t2v" %% "play2.auth.test" % "0.9" % "test",
    "com.restfb" % "restfb" % "1.6.11",
    "joda-time" % "joda-time" % "2.1",
    "commons-validator" % "commons-validator" % "1.4.0" exclude("commons-beanutils", "commons-beanutils")
  )

  val appResolvers = Seq(
    "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
    "t2v.jp repo" at "http://www.t2v.jp/maven-repo/",
    "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository",
    Resolver.file("Local Repository", file(Path.userHome.absolutePath + "/.ivy2/local"))(Resolver.ivyStylePatterns)
  )

  lazy val playProjectSetting = Seq(
    routesImport ++= Seq(
      "se.radley.plugin.salat.Binders._"
    ),
    templatesImport ++= Seq(
      "org.bson.types.ObjectId"
    ),
    resolvers ++= appResolvers
  )

  lazy val scalaProjectSetting = Defaults.defaultSettings ++ Seq(
    version := appVersion,
    organization := appOrganization,
    scalaVersion := appScalaVersion,
    resolvers ++= appResolvers
  ) ++ SbtIdeaPlugin.ideaSettings

  val plugin = play.Project("plugin", appVersion, appDependencies, path = file("modules/plugin")).settings(playProjectSetting: _*)

  val service = play.Project("service", appVersion, appDependencies, path = file("modules/service")).settings(playProjectSetting: _*)
    .dependsOn(plugin)

  val admin = play.Project("admin", appVersion, appDependencies, path = file("modules/admin")).settings(playProjectSetting ++ Seq(
    templatesImport ++= Seq(
      "vn.myfeed.model._",
      "dto._",
      "model._"
    )): _*).dependsOn(plugin, service)

  //Add on project
  val chrome = Project("chrome", file("modules/chrome"), settings = scalaProjectSetting)
  //Add on project
  val firefox = Project("firefox", file("modules/firefox"), settings = scalaProjectSetting)

  val main = play.Project(appName, appVersion, appDependencies).settings(playProjectSetting ++ Seq(
    templatesImport ++= Seq(
      "vn.myfeed.model._",
      "dto._",
      "model._"
    )): _*).dependsOn(admin).aggregate(admin)

}
