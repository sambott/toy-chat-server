import actors.ClusteredAkkaConfig
import akka.actor.ActorSystem
import play.api.ApplicationLoader.Context
import play.api._
import play.api.cache.EhCacheComponents
import play.api.db.evolutions.{DynamicEvolutions, EvolutionsComponents}
import play.api.db.slick.evolutions.SlickEvolutionsComponents
import play.api.db.slick.{DbName, SlickComponents}
import play.api.i18n.I18nComponents
import play.api.mvc.EssentialFilter
import play.api.routing.Router
import play.filters.cors.CORSFilter
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

  val clusteredSystem = ActorSystem.create("clustered", ClusteredAkkaConfig.config)

  lazy val dbConfig = api.dbConfig[JdbcProfile](DbName("default"))

  lazy val chatController = new controllers.Chat(dbConfig, clusteredSystem, materializer)
  lazy val applicationController = new controllers.Application(defaultCacheApi)
  lazy val assets = new controllers.Assets(httpErrorHandler)

  // Routes is a generated class
  override def router: Router = new Routes(httpErrorHandler, applicationController, chatController, assets)

  val gzipFilter = new GzipFilter(shouldGzip =
  (request, response) => {
      val contentType = response.header.headers.get("Content-Type")
      contentType.exists(_.startsWith("text/html")) || request.path.endsWith("jsroutes.js")
    })

  val corsFilter = CORSFilter()

  override lazy val httpFilters: Seq[EssentialFilter] = Seq(gzipFilter, corsFilter)

  override lazy val dynamicEvolutions: DynamicEvolutions = new DynamicEvolutions

  def onStart() = {
    applicationEvolutions
  }

  onStart()
}
