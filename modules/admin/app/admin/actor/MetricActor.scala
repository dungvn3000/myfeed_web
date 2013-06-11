package admin.actor

import akka.actor.Actor
import com.codahale.metrics.MetricRegistry
import play.api.libs.json._
import play.api.libs.iteratee.{Enumerator, Concurrent}
import java.lang.management.ManagementFactory

/**
 * The Class MetricActor.
 *
 * @author Nguyen Duc Dung
 * @since 6/10/13 5:49 PM
 *
 */
class MetricActor(metric: MetricRegistry) extends Actor {

  val (enumerator, channel) = Concurrent.broadcast[JsValue]
  val osStats = ManagementFactory.getOperatingSystemMXBean
  val threadStats = ManagementFactory.getThreadMXBean
  val memoryStats = ManagementFactory.getMemoryMXBean
  val cpuStats = new CPU

  def receive = {
    case Push => {
      val meter = metric.getMeters.get("AccessLog$.requests")
      if (meter != null) {
        val cpu = ((cpuStats.getCpuUsage * 1000).round / 10.0).toInt
        val ram = memoryStats.getHeapMemoryUsage.getUsed / 1024 / 1024
        val data = Json.obj(
          "request" -> JsNumber(meter.getMeanRate),
          "cpu" -> JsNumber(cpu),
          "ram" -> JsNumber(ram)
        )
        channel push data
      }
    }
    case Join => {
      sender ! Connected(enumerator)
    }
  }
}

case object Join
case object Push
case class Connected(enumerator: Enumerator[JsValue])