import com.yammer.metrics.reporting.ConsoleReporter
import com.yammer.metrics.scala.MetricsGroup
import java.util.concurrent.TimeUnit
import play.api.mvc.{Result, RequestHeader, Filter}

/**
 * The Class AccessLog.
 *
 * @author Nguyen Duc Dung
 * @since 6/10/13 3:24 PM
 *
 */
object AccessLog extends Filter {
  val metrics = new MetricsGroup(AccessLog.getClass)
  val meter = metrics.meter("requestMeter", "requests")
  val reporter = ConsoleReporter.enable(metrics.metricsRegistry, 30, TimeUnit.SECONDS)

  def apply(next: (RequestHeader) => Result)(request: RequestHeader) = {
    val result = next(request)
    meter.mark()
    result
  }
}
