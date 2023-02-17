package domain

case class Book(id: BookId, title: String)

case class BookReference(book: Book, tags: Seq[Tag], devices: Seq[Device])

sealed trait Device
object Device {
  case object Pdf extends Device
  case object Kindle extends Device
}

case class Tag(name: String)