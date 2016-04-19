package controllers

import javax.inject.Inject

import actors.ChatRoomActor
import akka.actor.ActorSystem
import akka.stream.Materializer
import models.ChatMessages
import play.api.mvc._
import play.api.libs.streams._
import play.api.libs.json.Json
import play.api.libs.json._

import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
  * This file is subject to the terms and conditions defined in
  * file 'LICENSE.txt', which is part of this source code package.
  *
  * Sam Bott, 10/04/2016.
  */


class ChatController @Inject() (implicit system: ActorSystem, materializer: Materializer) extends Controller {
  import ChatMessages._

  def ws(room: String) = {
    WebSocket.accept[SentMessage, ReceivedMessage] { request =>
      ActorFlow.actorRef(socketRef => ChatRoomActor.props(socketRef, room))
    }
  }

  def postMessage(room: String) = Action(parse.json) { request =>
    request.body.validate[SentMessage].map{ in =>
      ChatRoomActor.sendMessage(room, in)
    }.fold(
      invalid = errors =>
        BadRequest(s"Could not deserialise fields: ${ errors map (_._1) mkString "," }")
      ,
      valid = _ => Ok
    )
  }

  def getLatest(room: String, max: Int) = Action.async {
    require(room.nonEmpty)
    require(room.length < 80)
    val persistence = new ChatMessagePersistence()
    for {
      msgs <- persistence.getMessages(room, max)
      msgsJson = Json.toJson(msgs)
    } yield Ok(msgsJson)
  }

  def getActiveRooms(minutes: Int) = Action.async {
    require(minutes > 0)
    require(minutes < 120)
    val persistence = new ChatMessagePersistence()
    for {
      rooms <- persistence.getActiveRooms(minutes)
      roomJson = Json.toJson(rooms)
    } yield Ok(roomJson)
  }
}
