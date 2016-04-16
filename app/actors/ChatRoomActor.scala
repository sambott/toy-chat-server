package actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.event.LoggingReceive

/**
  * This file is subject to the terms and conditions defined in
  * file 'LICENSE.txt', which is part of this source code package.
  *
  * Sam Bott, 16/04/2016.
  */

class ChatRoomActor(receiver: ActorRef, room: String) extends Actor with ActorLogging {
  override def receive: Receive = LoggingReceive {
    case _ => // ignore
  }
  receiver ! room
}

object ChatRoomActor {
  def props(receiver: ActorRef, room: String) = Props(classOf[ChatRoomActor], receiver, room)
}
