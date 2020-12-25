
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

object welcome extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template2[String,String,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/(message: String, style: String = "scala"):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*1.44*/(""" 

"""),_display_(/*3.2*/defining(play.core.PlayVersion.current)/*3.41*/ { version =>_display_(Seq[Any](format.raw/*3.54*/("""

"""),format.raw/*5.1*/("""<section id="top">
  <div class="wrapper">
    <h1><a href="/">Time Tracking System</a></h1>
  </div>
</section>

<div id="content" class="wrapper doc">
<article>

  <h1>Welcome to Time Tracking System application</h1>

  <p>
    This application tracks your activity on a particular subject and accumulates the amount of time you have spent on it.
  </p>

  <h2>How to use it?</h2>

    <p>
        Basically, you have to do the following steps:
    </p>

    <p>
        1. Create a subject (or choose one that you've already created before). For instance, let's name it "Programming in Scala".
    </p>

    <p>
        2. Every time you start coding/watching a lecture/reading an article about Scala, you need to click on the "Start
        session" button. You'll see that the timer has been started.
    </p>

    <p>
        3. After you've finished your activity, click on the "End session" button. The timer stops and the time you've spent
        on this session will be added to the subject "Programming in Scala".
    </p>

    <p>
        4. You can look at your overall statistics. Just press "Show statistics" button.
    </p>

    <h2>You have to be authenticated in the system so that you can use this application.</h2>

  <blockquote>
    <p>
      You’re using Play """),_display_(/*48.26*/version),format.raw/*48.33*/("""
    """),format.raw/*49.5*/("""</p>
  </blockquote>

  <h2>Why do you see this page?</h2>

    <p>
      The <code>conf/routes</code> file defines a route that tells Play to invoke the <code>HomeController.index</code> action
      whenever a browser requests the <code>/</code> URI using the GET method:
    </p>

    <pre><code># Home page
GET     /               controllers.HomeController.index</code></pre>

    <p>
      Play has invoked the <code>controllers.HomeController.index</code> method to obtain the <code>Action</code> to execute:
    </p>

    <pre><code>def index = Action """),format.raw/*66.35*/("""{"""),format.raw/*66.36*/("""
  """),format.raw/*67.3*/("""Ok(views.html.index("Your new application is ready."))
"""),format.raw/*68.1*/("""}"""),format.raw/*68.2*/("""</code></pre>

    <p>
      An action is a function that handles the incoming HTTP request, and returns the HTTP result to send back to the web client.
      Here we send a <code>200 OK</code> response, using a template to fill its content.
    </p>

    <p>
      The template is defined in the <code>app/views/index.scala.html</code> file and compiled as a Scala function.
    </p>

    <pre><code>@(message: String)(implicit assetsFinder: AssetsFinder)

@main("Welcome to Play") """),format.raw/*81.27*/("""{"""),format.raw/*81.28*/("""

    """),format.raw/*83.5*/("""@welcome(message, style = "scala")

"""),format.raw/*85.1*/("""}"""),format.raw/*85.2*/("""</code></pre>

    <p>
      The first line of the template defines the function signature. Here it just takes a single <code>String</code> parameter.
      This template then calls another function defined in <code>app/views/main.scala.html</code>, which displays the HTML
      layout, and another function that displays this welcome message. You can freely add any HTML fragment mixed with Scala
      code in this file.
    </p>

    <p>You can read more about <a href="https://www.playframework.com/documentation/"""),_display_(/*94.86*/version),format.raw/*94.93*/("""/ScalaTemplates">Twirl</a>, the template language used by Play, and how Play handles <a href="https://www.playframework.com/documentation/"""),_display_(/*94.232*/version),format.raw/*94.239*/("""/ScalaActions">actions</a>.</p>

    <h2>Async Controller</h2>

    Now that you've seen how Play renders a page, take a look at <code>AsyncController.scala</code>, which shows how to do asynchronous programming when handling a request.  The code is almost exactly the same as <code>HomeController.scala</code>, but instead of returning <code>Result</code>, the action returns <code>Future[String]</code> to Play.  When the execution completes, Play can use a thread to render the result without blocking the thread in the mean time.

    <p>
        <a href=""""),_display_(/*101.19*/routes/*101.25*/.AsyncController.message),format.raw/*101.49*/("""">Click here for the AsyncController action!</a>
    </p>

    <p>
        You can read more about <a href="https://www.playframework.com/documentation/"""),_display_(/*105.87*/version),format.raw/*105.94*/("""/ScalaAsync">asynchronous actions</a> in the documentation.
    </p>

    <h2>Count Controller</h2>

    <p>
        Both the HomeController and AsyncController are very simple, and typically controllers present the results of the interaction of several services.  As an example, see the <code>CountController</code>, which shows how to inject a component into a controller and use the component when handling requests.  The count controller increments every time you refresh the page, so keep refreshing to see the numbers go up.
    </p>

    <p>
        <a href=""""),_display_(/*115.19*/routes/*115.25*/.CountController.count),format.raw/*115.47*/("""">Click here for the CountController action!</a>
    </p>

    <p>
        You can read more about <a href="https://www.playframework.com/documentation/"""),_display_(/*119.87*/version),format.raw/*119.94*/("""/ScalaDependencyInjection">dependency injection</a> in the documentation.
    </p>

    <h2>Need more info on the console?</h2>

  <p>
    For more information on the various commands you can run on Play, i.e. running tests and packaging applications for production, see <a href="https://playframework.com/documentation/"""),_display_(/*125.187*/version),format.raw/*125.194*/("""/PlayConsole">Using the Play console</a>.
  </p>  

  <h2>Need to set up an IDE?</h2>

  <p>
      You can start hacking your application right now using any text editor. Any changes will be automatically reloaded at each page refresh, 
      including modifications made to Scala source files.
  </p>

  <p>
      If you want to set-up your application in <strong>IntelliJ IDEA</strong> or any other Java IDE, check the 
      <a href="https://www.playframework.com/documentation/"""),_display_(/*137.61*/version),format.raw/*137.68*/("""/IDE">Setting up your preferred IDE</a> page.
  </p>

  <h2>Need more documentation?</h2>

  <p>
    Play documentation is available at <a href="https://www.playframework.com/documentation/"""),_display_(/*143.94*/version),format.raw/*143.101*/("""">https://www.playframework.com/documentation</a>.
  </p>

  <p>
    Play comes with lots of example templates showcasing various bits of Play functionality at <a href="https://www.playframework.com/download#examples">https://www.playframework.com/download#examples</a>.
  </p>

  <h2>Need more help?</h2>

  <p>
    Play questions are asked and answered on Stackoverflow using the "playframework" tag: <a href="https://stackoverflow.com/questions/tagged/playframework">https://stackoverflow.com/questions/tagged/playframework</a>
  </p>

  <p>
    The <a href="https://discuss.playframework.com">Discuss Play Forum</a> is where Play users come to seek help,
    announce projects, and discuss issues and new features.
  </p>

  <p>
    Gitter is a real time chat channel, like IRC. The <a href="https://gitter.im/playframework/playframework">playframework/playframework</a>  channel is used by Play users to discuss the ins and outs of writing great Play applications.
  </p>
 
</article>
<!--
<aside>
  <h3>Browse</h3>
  <ul>
    <li><a href="https://playframework.com/documentation/"""),_display_(/*170.59*/version),format.raw/*170.66*/("""">Documentation</a></li>
    <li><a href="https://playframework.com/documentation/"""),_display_(/*171.59*/version),format.raw/*171.66*/("""/api/"""),_display_(/*171.72*/style),format.raw/*171.77*/("""/index.html">Browse the """),_display_(/*171.102*/{style.capitalize}),format.raw/*171.120*/(""" """),format.raw/*171.121*/("""API</a></li>
  </ul>
  <h3>Start here</h3>
  <ul>
    <li><a href="https://playframework.com/documentation/"""),_display_(/*175.59*/version),format.raw/*175.66*/("""/PlayConsole">Using the Play console</a></li>
    <li><a href="https://playframework.com/documentation/"""),_display_(/*176.59*/version),format.raw/*176.66*/("""/IDE">Setting up your preferred IDE</a></li>
    <li><a href="https://playframework.com/download#examples">Example Projects</a>
  </ul>
  <h3>Help here</h3>
  <ul>
    <li><a href="https://stackoverflow.com/questions/tagged/playframework">Stack Overflow</a></li>
    <li><a href="https://discuss.playframework.com">Discuss Play Forum</a> </li>
    <li><a href="https://gitter.im/playframework/playframework">Gitter Channel</a></li>
  </ul>
  
</aside>
-->

</div>
""")))}),format.raw/*190.2*/("""
"""))
      }
    }
  }

  def render(message:String,style:String): play.twirl.api.HtmlFormat.Appendable = apply(message,style)

  def f:((String,String) => play.twirl.api.HtmlFormat.Appendable) = (message,style) => apply(message,style)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  DATE: 2020-12-25T18:32:03.648104
                  SOURCE: /home/george/Projects/scala-sandbox/time-tracking-application/app/views/welcome.scala.html
                  HASH: 29a23b60467c9b08f930f994ca4b92a60bf0af44
                  MATRIX: 738->1|875->43|904->47|951->86|1001->99|1029->101|2342->1387|2370->1394|2402->1399|2990->1959|3019->1960|3049->1963|3131->2018|3159->2019|3670->2504|3699->2505|3732->2511|3795->2548|3823->2549|4369->3068|4397->3075|4564->3214|4593->3221|5182->3782|5198->3788|5244->3812|5425->3965|5454->3972|6049->4539|6065->4545|6109->4567|6290->4720|6319->4727|6669->5048|6699->5055|7209->5537|7238->5544|7456->5734|7486->5741|8600->6827|8629->6834|8740->6917|8769->6924|8803->6930|8830->6935|8884->6960|8925->6978|8956->6979|9092->7087|9121->7094|9253->7198|9282->7205|9778->7670
                  LINES: 21->1|26->1|28->3|28->3|28->3|30->5|73->48|73->48|74->49|91->66|91->66|92->67|93->68|93->68|106->81|106->81|108->83|110->85|110->85|119->94|119->94|119->94|119->94|126->101|126->101|126->101|130->105|130->105|140->115|140->115|140->115|144->119|144->119|150->125|150->125|162->137|162->137|168->143|168->143|195->170|195->170|196->171|196->171|196->171|196->171|196->171|196->171|196->171|200->175|200->175|201->176|201->176|215->190
                  -- GENERATED --
              */
          