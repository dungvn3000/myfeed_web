package curd

import scala.collection.mutable.ListBuffer
import play.api.data.{Field, Form}
import play.api.templates.Html
import _root_.views.html.helper.{FieldConstructor, select, checkbox, inputText}

/**
 * The Class FormBuilder.
 *
 * @author Nguyen Duc Dung
 * @since 6/7/13 12:16 PM
 *
 */
case class FormBuilderData[E](title: String, fields: List[Html], form: Form[E])


abstract class FormBuilder[E](title: String) {

  implicit def fieldConstructor = FieldConstructor(curd.views.html.text.f)

  val form: Form[E]

  val fields = new ListBuffer[(String, Field => Html)]()

  def textField(key: String, args: (Symbol, Any)*) = {
    val f: Field => Html = inputText(_, args ++ Seq(Symbol("ng-model") -> s"entry.$key"): _*)
    fields += key -> f
    key
  }

  def checkBoxField(key: String, args: (Symbol, Any)*) = {
    val f: Field => Html = checkbox(_, args ++ Seq(Symbol("ng-model") -> s"entry.$key"): _*)
    fields += key -> f
    key
  }

  def selectField(key: String, options: Seq[(String,String)], args: (Symbol, Any)*) = {
    val f: Field => Html = select(_, options, args ++ Seq(Symbol("ng-model") -> s"entry.$key"): _*)
    fields += key -> f
    key
  }

  def build(form: Form[E] = form): FormBuilderData[E] = FormBuilderData[E](
    title = title,
    fields = fields.map(field => {
      field._2(form(field._1))
    }).toList,
    form = form
  )

}

