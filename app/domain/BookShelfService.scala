package domain

import scala.concurrent.{ExecutionContext, Future}

class BookShelfService(val repository: BookShelfRepository)(implicit val ec: ExecutionContext) {

  def reproduceBookShelfById(id: EntityId): Future[Either[AppError, BookShelfEntity]] =
    repository
      .obtainBookShelfEvents(id)
      .map(_.map(events => BookShelfEntity.reproduce(events)(id)))

}
