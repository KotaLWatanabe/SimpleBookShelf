package controllers

import domain.{Book, ISBN}
import sttp.client3.{SimpleHttpClient, UriContext, basicRequest}

import scala.util.{Failure, Success, Try}
import scala.xml.{Elem, XML}

// https://iss.ndl.go.jp/information/api/riyou/#sec3
object KokkaiToshokanAccessorXmlToBookshelf {
  private val searchAPIURLWithTitle = (title: String) => uri"https://iss.ndl.go.jp/api/opensearch?title=$title"
  private val isbnTagPattern = """<dc:identifier xsi:type="dcndl:ISBN">(.+?)</dc:identifier>""".r
  private val isbnSubStr = (str: String) => str.substring(37, str.length - 16)

  private val client = SimpleHttpClient()

  private val bookTitle = "Akka実践バイブル"
  def searchBooks(title: String = bookTitle): Either[String, Seq[Book]] = {
    val request = basicRequest.get(searchAPIURLWithTitle(title))
    val response = client.send(request)

    response.body.fold(Left(_), { xmlStr =>
      val xml = Try(XML.loadString(xmlStr))
      xml match {
        case Success(res) => Right(xmlToBooks(res))
        case Failure(e) => Left(e.getMessage)
      }
    })
  }

  private def xmlToBooks(elem: Elem): Seq[Book] =
      for {
        item <- (elem \\ "item")
        title <- (item \ "title").headOption.map(_.text)
        isbn <- isbnTagPattern.findFirstIn(item.toString).map(isbnSubStr)
      } yield  Book(id = ISBN(isbn), title = title)
}
