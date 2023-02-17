package controllers

import domain.{BookShelfService, EntityId}
import play.api.mvc._

import java.util.UUID
import javax.inject._
import scala.concurrent.ExecutionContext
import scala.language.postfixOps

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class BookShelfController @Inject()(
                                     val controllerComponents: ControllerComponents,
                                   ) extends BaseController {

  private implicit val ec: ExecutionContext = controllerComponents.executionContext
  private val bookShelfService: BookShelfService = new BookShelfService(new BookShelfRepositoryImpl())

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def list(id: EntityId = EntityId(UUID.fromString("admin"))): Action[AnyContent] = Action.async { implicit request =>

    bookShelfService.reproduceBookShelfById(id).map {
      case Right(entity) => Ok(views.html.index(entity.books))
      case Left(_) => NoContent
    }
  }
}
