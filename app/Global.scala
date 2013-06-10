import admin.actor.MetricActor
import akka.actor.Props
import com.codahale.metrics.MetricRegistry
import com.mongodb.casbah.commons.conversions.scala.RegisterJodaTimeConversionHelpers
import com.typesafe.config.ConfigFactory
import java.io.File
import play.api._
import play.api.libs.concurrent.Akka
import play.api.mvc.{EssentialAction, WithFilters}
import scala.concurrent.duration._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.Play.current

/**
 * The Class Global.
 *
 * @author Nguyen Duc Dung
 * @since 11/2/12 12:23 AM
 *
 */
object Global extends WithFilters(AccessLog) {

  val devConfFilePath = "conf/dev.conf"
  val prodConfFilePath = "prod.conf"

  val metric = new MetricRegistry
  val requestMeter = metric.meter(MetricRegistry.name(AccessLog.getClass, "requests"))

  override def onStart(app: Application) {
    Logger.info("Starting Application")
    RegisterJodaTimeConversionHelpers()
    val myActor = Akka.system.actorOf(Props(new MetricActor(metric)), name = "metricActor")
    Akka.system.scheduler.schedule(10.seconds, 10.seconds, myActor, "print")
  }

  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
  }

  override def onLoadConfig(config: Configuration, path: File, classLoader: ClassLoader, mode: Mode.Mode) = if (mode == Mode.Prod) {
    val prodConfig = ConfigFactory.parseResources(classLoader, prodConfFilePath)
    config ++ Configuration(prodConfig)
  } else {
    val devConfig = ConfigFactory.parseFileAnySyntax(new File(path, devConfFilePath))
    config ++ Configuration(devConfig)
  }

  override def doFilter(a: EssentialAction) = {
    requestMeter.mark()
    super.doFilter(a)
  }
}
