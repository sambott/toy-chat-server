package models

import org.joda.time.DateTime
import play.api.mvc.WebSocket.MessageFlowTransformer
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._
import com.github.tototoshi.slick.MySQLJodaSupport._
import play.api.Play
import play.api.db.slick.DatabaseConfigProvider

import scala.concurrent.Future

/**
  * This file is subject to the terms and conditions defined in
  * file 'LICENSE.txt', which is part of this source code package.
  *
  * Sam Bott, 17/04/2016.
  */

object ChatMessages {
  import play.api.libs.json._

  case class SentMessage(user: String, message: String) {
    def toReceivedMessage(room: String) = ReceivedMessage(user, message, DateTime.now(), room)
  }
  case class ReceivedMessage(user: String, message: String, dateTime: DateTime, room: String)

  implicit val sentMessageFormat = Json.format[SentMessage]
  implicit val receivedMessageFormat = Json.format[ReceivedMessage]
  implicit val chatMessageTransformer = MessageFlowTransformer.jsonMessageFlowTransformer[SentMessage, ReceivedMessage]

  class ReceivedMessageTableDef(tag: Tag) extends Table[ReceivedMessage](tag, "messages") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def room = column[String]("room")
    def datetime = column[DateTime]("datetime")
    def user = column[String]("user")
    def message = column[String]("message")

    def dt_idx = index("dt_idx", datetime)
    def room_idx = index("room_idx", room)

    override def * = (user, message, datetime, room) <> (ReceivedMessage.tupled, ReceivedMessage.unapply)

  }

  class ChatMessagePersistence {

    val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
    val messageDb = TableQuery[ReceivedMessageTableDef]

    def saveMessage(receivedMessage: ReceivedMessage): Unit = {
      dbConfig.db.run(messageDb += receivedMessage)
    }

    def getMessages(room: String, max: Int): Future[Seq[ReceivedMessage]] = {
      require(max > 0)
      dbConfig.db.run(messageDb.filter(_.room === room).sortBy(_.datetime.desc).take(max).result)
    }

  }
}
