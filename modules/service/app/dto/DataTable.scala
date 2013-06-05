package dto

import play.api.libs.json.JsValue

/**
 * This class using for table with json data,
 *
 * @author Nguyen Duc Dung
 * @since 6/6/13 12:58 AM
 *
 */
case class DataTable(field: String, value: String, sort: String, order: Int, page: Int, limit: Int, totalPage: Long, data: JsValue)