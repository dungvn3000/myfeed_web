package actor

import akka.actor.Actor
import com.codahale.metrics.MetricRegistry
import collection.JavaConversions._

/**
 * The Class MetricActor.
 *
 * @author Nguyen Duc Dung
 * @since 6/10/13 5:49 PM
 *
 */
class MetricActor(metric: MetricRegistry) extends Actor {
  def receive = {
    case "print" => {
      metric.getMeters.values().foreach(meter => {
        println("count = " + meter.getCount)
        printf("mean rate = %2.2f requests/s", meter.getMeanRate)
        println()
        printf("1-minute rate = %2.2f requests/s", meter.getOneMinuteRate)
        println()
        println()
      })
    }
  }
}
