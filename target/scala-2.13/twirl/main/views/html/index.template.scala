
package views.html

import _root_.play.twirl.api.TwirlFeatureImports._
import _root_.play.twirl.api.TwirlHelperImports._
import _root_.play.twirl.api.Html
import _root_.play.twirl.api.JavaScript
import _root_.play.twirl.api.Txt
import _root_.play.twirl.api.Xml
import models._
import controllers._
import play.api.i18n._
import views.html._
import play.api.templates.PlayMagic._
import play.api.mvc._
import play.api.data._

object index extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template1[AssetsFinder,play.twirl.api.HtmlFormat.Appendable] {

  /*
 * This template takes a two arguments, a String containing a
 * message to display and an AssetsFinder to locate static assets.
 */
  def apply/*6.2*/(implicit assetsFinder: AssetsFinder):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*7.1*/("""

"""),format.raw/*13.4*/("""
"""),_display_(/*14.2*/main("Welcome to Play")/*14.25*/ {_display_(Seq[Any](format.raw/*14.27*/("""

    """),format.raw/*19.8*/("""
    """),_display_(/*20.6*/welcome("Time Tracking System", style = "scala")),format.raw/*20.54*/("""

""")))}),format.raw/*22.2*/("""
"""))
      }
    }
  }

  def render(assetsFinder:AssetsFinder): play.twirl.api.HtmlFormat.Appendable = apply(assetsFinder)

  def f:((AssetsFinder) => play.twirl.api.HtmlFormat.Appendable) = (assetsFinder) => apply(assetsFinder)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  DATE: 2020-12-25T18:32:03.611388
                  SOURCE: /home/george/Projects/scala-sandbox/time-tracking-application/app/views/index.scala.html
                  HASH: 0423c02ae0f5af2257adabcdee831053fae30c0f
                  MATRIX: 866->138|997->176|1026->372|1054->374|1086->397|1126->399|1159->528|1191->534|1260->582|1293->585
                  LINES: 24->6|29->7|31->13|32->14|32->14|32->14|34->19|35->20|35->20|37->22
                  -- GENERATED --
              */
          