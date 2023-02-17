package domain
import cats.conversions.all.autoWidenFunctor

import java.time.{ZoneId, ZonedDateTime}
import java.util.UUID
import scala.collection.mutable

case class EntityId(value: UUID)
object EntityId {
  def init(): EntityId = EntityId(UUID.randomUUID())
  def from: String => EntityId = UUID.fromString andThen EntityId
}

sealed trait BookShelfEntityEvent {
  val entityId: EntityId
  val createdAt: ZonedDateTime = ZonedDateTime.now(ZoneId.of("JST"))
}
case class BookRemovedEvent(entityId: EntityId, bookId: BookId) extends BookShelfEntityEvent
case class BookReplacedEvent(entityId: EntityId, bookId: BookId, book: BookReference) extends BookShelfEntityEvent
case class BookAddedEvent(entityId: EntityId, bookId: BookId, book: BookReference) extends BookShelfEntityEvent

class BookShelfEntity(private val id: EntityId, val books: Map[BookId, BookReference]) {
  import domain.BookShelfEntity._
  private def modifyEntity = create(id) _
  def remove: BookId => BookShelfEntity =  books.removed _ andThen modifyEntity
  def update: (BookId, BookReference) => BookShelfEntity = (books.updated _).curried andThen modifyEntity
}

object BookShelfEntity {
  def init(): BookShelfEntity =
    new BookShelfEntity(id = EntityId.init(), books = Map.empty)

  def initWithBooks: Map[BookId, BookReference] =>  BookShelfEntity =
    books => new BookShelfEntity(id = EntityId.init(), books = books)

  def create: EntityId => Map[BookId, BookReference] => BookShelfEntity =
    entityId => books => new BookShelfEntity(id = entityId, books = books)

  def reproduce: IndexedSeq[BookShelfEntityEvent] => EntityId => BookShelfEntity =
    events => create(_) {
      require(events.head.isInstanceOf[BookAddedEvent])
      var procMap = mutable.HashMap.empty[BookId, BookReference]
      for (event <- events) {
        event match {
          case BookRemovedEvent(_, id) => procMap.remove(id)
          case BookAddedEvent(_, id, book) => procMap.put(id, book)
          case BookReplacedEvent(_, id, book) => procMap.update(id, book)
        }
      }
      Map.from(procMap)
    }
}