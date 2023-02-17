package domain

import scala.concurrent.Future

trait BookShelfRepository {

  def addBookShelfEvent(event: BookShelfEntityEvent): Future[Either[AppError, Unit]]

  def obtainBookShelfEvents(id: EntityId): Future[Either[AppError, IndexedSeq[BookShelfEntityEvent]]]
}
