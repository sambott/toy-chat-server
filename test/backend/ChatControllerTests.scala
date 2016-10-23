package backend

import common.ApplicationSpec
import models.ChatMessages.SentMessage
import play.api.test.Helpers._
import play.api.test._

/**
  * Add your spec here.
  * You can mock out a whole application including requests, plugins etc.
  * For more information, consult the wiki.
  */
class ChatControllerTests extends ApplicationSpec {

  def getRooms = route(app, FakeRequest(GET, "/rooms")).get
  def getRoomMsgs(room: String) = route(app, FakeRequest(GET, s"/rooms/$room")).get
  def postMsg(room: String, msg: SentMessage) =
    route(app, FakeRequest(POST, s"/rooms/$room").withBody(msg)).get
  def roomWebSocket(room: String) =
    route(app, FakeRequest(GET, s"/rooms/$room/ws")).get

  "ChatController" should {

    "get a list of 0 active rooms if there are none" in {
      val room = getRooms
      status(room) mustBe OK
      contentType(room) mustBe Some("text/html")
      contentAsString(room) must include ("Your new application is ready.")
    }

  }

}
