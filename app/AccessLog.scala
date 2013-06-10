import play.api.mvc.{Result, RequestHeader, Filter}

/**
 * The Class AccessLog.
 *
 * @author Nguyen Duc Dung
 * @since 6/10/13 3:24 PM
 *
 */
object AccessLog extends Filter {
  def apply(next: (RequestHeader) => Result)(request: RequestHeader) = {
    val result = next(request)
    result
  }
}
