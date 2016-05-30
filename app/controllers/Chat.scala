package controllers

import javax.inject.Inject

import actors.ChatRoomActor
import akka.actor.ActorSystem
import akka.stream.Materializer
import models.ChatMessages
import models.ChatMessages.ChatMessagePersistence
import play.api.mvc._
import play.api.libs.streams._
import play.api.libs.json.Json
import play.api.libs.json._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

/**
  * This file is subject to the terms and conditions defined in
  * file 'LICENSE.txt', which is part of this source code package.
  *
  * Sam Bott, 10/04/2016.
  */


class Chat(msgPersistence: ChatMessagePersistence, system: ActorSystem, materializer: Materializer) extends Controller {
  import ChatMessages._

  implicit def actorSystem = system
  implicit def materialiser = materializer

  def ws(room: String) = {
    WebSocket.accept[SentMessage, ReceivedMessage] { request =>
      ActorFlow.actorRef(socketRef => ChatRoomActor.props(msgPersistence, socketRef, room))
    }
  }

  def postMessage(room: String) = Action(parse.json) { request =>
    request.body.validate[SentMessage].map{ in =>
      ChatRoomActor.sendMessage(msgPersistence, room, in)
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
    for {
      msgs <- msgPersistence.getMessages(room, max)
      msgsJson = Json.toJson(msgs.reverse)
    } yield Ok(msgsJson)
  }

  def getActiveRooms(minutes: Int) = Action.async {
    require(minutes > 0)
    require(minutes < 120)
    for {
      rooms <- msgPersistence.getActiveRooms(minutes)
      roomJson = Json.toJson(rooms)
    } yield Ok(roomJson)
  }
}
