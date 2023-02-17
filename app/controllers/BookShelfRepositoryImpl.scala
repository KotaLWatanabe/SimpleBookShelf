package controllers

import domain.{AppError, BookShelfEntityEvent, BookShelfRepository, EntityId}

import scala.concurrent.{ExecutionContext, Future}

class BookShelfRepositoryImpl(implicit val ec: ExecutionContext) extends BookShelfRepository {

  def addBookShelfEvent(event: BookShelfEntityEvent): Future[Either[AppError, Unit]] = ???

  def obtainBookShelfEvents(id: EntityId): Future[Either[AppError, IndexedSeq[BookShelfEntityEvent]]] = ???
}
