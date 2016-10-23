package backend

import common.ApplicationSpec
import play.api.test.Helpers._
import play.api.test._

/**
  * Add your spec here.
  * You can mock out a whole application including requests, plugins etc.
  * For more information, consult the wiki.
  */
class ApplicationControllerTests extends ApplicationSpec {


  "Routes" should {

    "send 404 on a bad request" in  {
      route(app, FakeRequest(GET, "/not/exist")).map(status(_)) mustBe Some(NOT_FOUND)
    }

  }

  "Application Controller" should {

    "serve a home page" in {
      val home = route(app, FakeRequest(GET, "/")).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("{{title}}")
    }

    "serve a reverse js router" in {
      val home = route(app, FakeRequest(GET, "/jsroutes.js")).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/javascript")
      contentAsString(home) must include ("var jsRoutes")
    }

  }

}
