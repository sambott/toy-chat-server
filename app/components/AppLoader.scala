package components

import models.ChatMessages
import play.api.ApplicationLoader.Context
import play.api.cache.EhCacheComponents
import play.api.db.evolutions.{DynamicEvolutions, EvolutionsComponents}
import play.api.db.slick.evolutions.SlickEvolutionsComponents
import play.api.db.slick.{DbName, SlickComponents}
import play.api.i18n.I18nComponents
import play.api.mvc.EssentialFilter
import play.api.routing.Router
import play.api.{Application, ApplicationLoader, BuiltInComponentsFromContext, LoggerConfigurator}
import play.filters.cors.{CORSConfig, CORSFilter}
import play.filters.gzip.GzipFilter
import slick.driver.JdbcProfile

import router.Routes

class AppLoader extends ApplicationLoader {
  override def load(context: Context): Application = {
    LoggerConfigurator(context.environment.classLoader).foreach(_.configure(context.environment))
    new AppComponents(context).application
  }
}

class AppComponents(context: Context) extends BuiltInComponentsFromContext(context)
  with EhCacheComponents
  with I18nComponents
  with SlickComponents
  with EvolutionsComponents
  with SlickEvolutionsComponents {

  protected def dbConfig = api.dbConfig[JdbcProfile](DbName("default"))
  lazy val chatPersistence = new ChatMessages.SlickChatMessagePersistence(dbConfig)

  lazy val chatController = new controllers.Chat(chatPersistence, actorSystem, materializer)
  lazy val applicationController = new controllers.Application(defaultCacheApi)
  lazy val assets = new controllers.Assets(httpErrorHandler)

  // Routes is a generated class
  override def router: Router = new Routes(httpErrorHandler, applicationController, chatController, assets)

  val gzipFilter = new GzipFilter(shouldGzip =
  (request, response) => {
      val contentType = response.header.headers.get("Content-Type")
      contentType.exists(_.startsWith("text/html")) || request.path.endsWith("jsroutes.js")
    })

  val corsFilter = CORSFilter(CORSConfig.fromConfiguration(configuration))

  override lazy val httpFilters: Seq[EssentialFilter] = Seq(corsFilter, gzipFilter)

  override lazy val dynamicEvolutions: DynamicEvolutions = new DynamicEvolutions

  def onStart() = {
    applicationEvolutions
  }

  onStart()
}
