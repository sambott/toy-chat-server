package frontend

import common.IntegrationSpec


class UITests extends IntegrationSpec {

  "Application" should {

    "hand over to angular" in {

      go to ("http://localhost:" + port)

      pageSource must include ("ng-view")
    }
  }
}
