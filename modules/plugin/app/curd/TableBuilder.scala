package curd

import scala.collection.mutable.ListBuffer

/**
 * The Class TableBuilder.
 *
 * @author Nguyen Duc Dung
 * @since 6/7/13 7:17 AM
 *
 */
case class Column(key: String, title: String, style: String = "width:100px")

abstract class TableBuilder[ObjectType <: AnyRef] {

  var cols = new ListBuffer[Column]

  def column(key: String, title: String)  {
    cols += Column(key, title)
  }

}