import play.api.ApplicationLoader.Context
import play.api.cache.EhCacheComponents
import play.api.mvc.EssentialFilter
import play.api.routing.Router
import play.api._
import play.filters.cors.CORSFilter
import play.filters.gzip.GzipFilter
import router.Routes

class AppLoader extends ApplicationLoader {
  override def load(context: Context): Application = {
    LoggerConfigurator(context.environment.classLoader).foreach(_.configure(context.environment))
    new AppComponents(context).application
  }
}

class AppComponents(context: Context) extends BuiltInComponentsFromContext(context) with EhCacheComponents {

  lazy val chatController = new controllers.Chat(actorSystem, materializer)
  lazy val applicationController = new controllers.Application(defaultCacheApi)
  lazy val usersController = new controllers.Users(defaultCacheApi)
  lazy val assets = new controllers.Assets(httpErrorHandler)
  materializer

  // Routes is a generated class
  override def router: Router = new Routes(httpErrorHandler, applicationController, chatController, usersController, assets)

  val gzipFilter = new GzipFilter(shouldGzip =
    (request, response) => {
      val contentType = response.header.headers.get("Content-Type")
      contentType.exists(_.startsWith("text/html")) || request.path.endsWith("jsroutes.js")
    })

  val corsFilter = CORSFilter()

  override lazy val httpFilters: Seq[EssentialFilter] = Seq(gzipFilter, corsFilter)
}
