package org.example.collector

import org.example.MoodleAnswer
import org.example.MoodleTest
import org.example.MoodleTestGrade
import org.example.MoodleTestReport
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

import scala.collection.convert.ImplicitConversions.`collection AsScalaIterable`


class RawMoodleTestHtmlReportCollector extends RawHtmlCollector[MoodleTestReport] {

  //language=regexp
  private lazy val REDUCE_HTML_TAGS_REGEXP = "(<([^>]+)>)"

  override def collect(rawHtml: String): MoodleTestReport = {
    val parse = Jsoup.parse(rawHtml)
    val title = parse.titleHtmlReduced()
    val username = parse.getElementById("usermenu").htmlReduced()
    val contents: Elements = parse.getElementsByClass("content")
    val pageContent = parse.getElementById("page-content")

    val id: String = {
      val content = parse.getElementsByClass("content").head
      val name = content.getElementsByTag("input").head.attr("name")
      val pattern = "(q[0-9]+:)".r
      pattern.findFirstIn(name).get.replaceAll("[q:]", "")
    }

    val tests = contents
      .filterNot(_.getElementsByClass("qtext").isEmpty)
      .map { element =>
          val qtext: String = element.getElementsByClass("qtext").head.htmlReduced()
          val prompt: String = element.getElementsByClass("prompt").head.htmlReduced()

          val answers: List[MoodleAnswer] = element
            .getElementsByClass("answer")
            .flatMap(element => element.getElementsByClass("r0").toList ++ element.getElementsByClass("r1").toList)
            .map(element => {
              val isSelected = element.getElementsByTag("input").hasAttr("checked")

              val correctness = {
                val isCorrect = element.hasClass("correct") && isSelected
                val isIncorrect = element.hasClass("incorrect") && isSelected
                if (isIncorrect || isCorrect) Option(isCorrect) else Option.empty
              }

              val answerNumber = element.getElementsByClass("answernumber")
                .head
                .htmlReduced()

              val answerLabel = element.getElementsByAttributeValue("data-region", "answer-label")
                .head
                .getElementsByClass("flex-fill ml-1")
                .head
                .htmlReduced()

              MoodleAnswer(correctness, isSelected, answerLabel, answerNumber)
            })
            .toList

          val answerLabel = element.getElementsByAttributeValue("data-region", "answer-label")
            .head


          val answerId = ":[0-9]+_".r
            .findFirstIn(answerLabel.attr("id"))
            .get
            .replaceAll("[:_]", "")
            .toInt

          MoodleTest(qtext, prompt, answerId, answers)
      }
      .map { test =>
        val div = pageContent.getElementById(s"question-$id-${test.testNumber}")

        val grade = div.getElementsByClass("grade").head
        val pattern = """[-+]?\d*[,.]\d+|\d+""".r

        val values = pattern.findAllIn(grade.htmlReduced())
          .toList
          .map(_.replace(',', '.'))

        val testGrade = MoodleTestGrade(
          id = id,
          testNumber = test.testNumber,
          grade = values.head.toDouble,
          maxGrade = values(1).toDouble
        )
        (testGrade, test)
      }
      .toList

    MoodleTestReport(username, id, title, tests)
  }

  private implicit class ElementImplicits(element: Element) {
    def htmlReduced(): String = {
      element
        .html()
        .replaceAll(REDUCE_HTML_TAGS_REGEXP, "")
    }
  }

  private implicit class DocumentImplicits(elements: Document) {
    def titleHtmlReduced(): String = {
      elements.title()
        .replaceAll(REDUCE_HTML_TAGS_REGEXP, "")
    }
  }

}
