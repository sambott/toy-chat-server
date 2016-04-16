package controllers

import javax.inject.Inject

import actors.ChatRoomActor
import akka.actor.ActorSystem
import akka.stream.Materializer
import play.api.mvc._
import play.api.libs.streams._

/**
  * This file is subject to the terms and conditions defined in
  * file 'LICENSE.txt', which is part of this source code package.
  *
  * Sam Bott, 10/04/2016.
  */

class ChatController @Inject() (implicit system: ActorSystem, materializer: Materializer) extends Controller {

  def ws(room: String) = {
    WebSocket.accept[String, String] { request =>
      ActorFlow.actorRef(socketRef => ChatRoomActor.props(socketRef, room))
    }
  }

  def postMessage(room: String) = play.mvc.Results.TODO

  def getLatest(room: String, max: Int) = play.mvc.Results.TODO
  
}
