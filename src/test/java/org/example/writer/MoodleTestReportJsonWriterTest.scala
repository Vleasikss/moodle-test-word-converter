package org.example.writer

import org.example.MoodleAnswer
import org.example.MoodleTest
import org.example.MoodleTestGrade
import org.example.MoodleTestInfo
import org.example.MoodleTestReport
import org.example.TestUtils
import org.scalatest.BeforeAndAfterAll
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode

import java.nio.file.Paths

class MoodleTestReportJsonWriterTest extends AnyFlatSpec
  with should.Matchers
  with TestUtils
  with BeforeAndAfterAll {

  val actualJsonPath: String = /("/actual/json")
  val expectedJsonPath: String = /("/expected/json")

  it should "write moodle report to json correctly" in {
    val moodleReport = MoodleTestReport(
      "Єсипчук Дарина",
      "272284",
      "Модуль №2: Attempt review",
      List[MoodleTest](MoodleTest(
        1,
        MoodleTestInfo(
          "Непряме (опосередковане) валютне котирування – це:",
          "Виберіть одну відповідь:",
          List(
            MoodleAnswer(None, isSelected = false, "одиниця іноземної валюти дорівнює певній кількості національної валюти", "a."),
            MoodleAnswer(None, isSelected = true, "множинність валютних курсів", "b."),
          )
        ),
        MoodleTestGrade(1.0, 1.0),
      ))
    )

    val writer = new MoodleTestReportJsonWriter()
    val result = writer.write(moodleReport, actualJsonPath)
    result should not be empty

    val filename = result.get.toString.replace(actualJsonPath.toAbsolutePath + "/", "")
    s"${moodleReport.username.reduceExtraSymbols}-${moodleReport.title.reduceExtraSymbols}-${moodleReport.id}.json" should equal(filename)

    val actualJson: String = readFile(result.get)
    val expectedJson: String = readFile(Paths.get(expectedJsonPath, s"moodle-test-assignment-report-q${moodleReport.id}.json"))
    JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.LENIENT)

  }

}