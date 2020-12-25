// @GENERATOR:play-routes-compiler
// @SOURCE:/home/george/Projects/time-tracking-application/conf/routes
// @DATE:Fri Dec 25 18:46:04 SAMT 2020


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
