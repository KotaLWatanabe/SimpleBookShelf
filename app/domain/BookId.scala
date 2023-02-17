package domain

import scala.util.{Failure, Success, Try}

sealed trait BookId {
  def value: String
}

case class ISBN(private val str: String) extends BookId {
  import ISBN._
  require(isbnPattern.matches(str))

  override def value: String = str
}
object ISBN {
  private val isbnPattern = s"[0-9]{10}|[0-9]{13}".r
  def create(id: String): Either[DomainError, BookId] =
    Try(ISBN(id)) match {
      case Success(isbn) => Right(isbn)
      case Failure(e) => Left(IllegalArgumentDomainError(e, ISBN.getClass))
    }
}