package org.example.collector

import org.example.TestUtils
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import scala.io.Source
import scala.util.Using

class RawMoodleTestHtmlReportCollectorTest extends AnyFlatSpec
  with should.Matchers
  with TestUtils {

  it should "convert moodle test 272284 into report correctly" in {
    val collector = new RawMoodleTestHtmlReportCollector
    val path = /("/test-moodle-page/moodle-test-assignment-result-full-q272284.html")

    val rawHtml = Using(Source.fromFile(path, "UTF-8"))(_.mkString).get
    val collected = collector.collect(rawHtml)

    collected.tests.size should be(30)
    collected.title should be("Модуль №2: Attempt review")
    collected.id should be("272284")
    collected.username should be("Єсипчук Дарина")

  }

  it should "convert moodle test 251633 into report correctly" in {
    val collector = new RawMoodleTestHtmlReportCollector
    val path = /("/test-moodle-page/moodle-test-assignment-result-full-q251633.html")

    val rawHtml = Using(Source.fromFile(path, "UTF-8"))(_.mkString).get
    val collected = collector.collect(rawHtml)

    collected.tests.size should be(6)
    collected.title should be("Тест по темі 6: Attempt review")
    collected.id should be("251633")
    collected.username should be("Єсипчук Дарина")

  }

  it should "convert moodle test 227987 into report correctly" in {
    val collector = new RawMoodleTestHtmlReportCollector
    val path = /("/test-moodle-page/moodle-test-assignment-result-full-q227987.html")

    val rawHtml = Using(Source.fromFile(path, "UTF-8"))(_.mkString).get
    val collected = collector.collect(rawHtml)

    collected.tests.size should be(10)
    collected.title should be("Модуль 1: Attempt review")
    collected.id should be("227987")
    collected.username should be("Єсипчук Дарина")

  }

}
