package actors

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.{Publish, Subscribe}
import models.ChatMessages
import models.ChatMessages.ChatMessagePersistence
import org.joda.time.DateTime
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

/**
  * This file is subject to the terms and conditions defined in
  * file 'LICENSE.txt', which is part of this source code package.
  *
  * Sam Bott, 16/04/2016.
  */

object ChatRoomActor {

  def props(receiver: ActorRef, room: String) = Props(classOf[ChatRoomActor], receiver, room)

  case class Message(user: String, msg: String, dateTime: DateTime)

  def roomTopic(room: String) = s"chatroom-$room"

  def sendMessage(dbConfig: DatabaseConfig[JdbcProfile], room: String, message: ChatMessages.SentMessage)(implicit system: ActorSystem) = {
    val mediator = DistributedPubSub(system).mediator
    val topic = roomTopic(room)
    mediator ! Publish(topic, ChatRoomActor.Message(message.user, message.message, DateTime.now()))
    val persistence = new ChatMessagePersistence(dbConfig)
    persistence.saveMessage(message.toReceivedMessage(room))
  }

}

class ChatRoomActor(dbConfig: DatabaseConfig[JdbcProfile], receiver: ActorRef, room: String) extends Actor with ActorLogging {
  import ChatRoomActor._

  implicit def system = context.system
  val mediator = DistributedPubSub(context.system).mediator
  val topic = roomTopic(room)
  mediator ! Subscribe(topic, self)

  def receive = {
    case msg: ChatMessages.SentMessage =>
      sendMessage(dbConfig, room, msg)

    case Message(from, text, dt) =>
      receiver ! ChatMessages.ReceivedMessage(from, text, dt, room)

    case _ => // ignore
  }

}
