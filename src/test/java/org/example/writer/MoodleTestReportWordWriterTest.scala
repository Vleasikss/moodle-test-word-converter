package org.example.writer

import org.example.MoodleAnswer
import org.example.MoodleTest
import org.example.MoodleTestGrade
import org.example.MoodleTestReport
import org.example.TestUtils
import org.scalatest.BeforeAndAfterAll
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class MoodleTestReportWordWriterTest extends AnyFlatSpec
  with should.Matchers
  with TestUtils
  with BeforeAndAfterAll {

  val actualWordPath: String = /("/actual/word")

  it should "write moodle report to word correctly" in {
    val moodleReport = MoodleTestReport(
      "Єсипчук Дарина",
      "272284",
      "Модуль №2: Attempt review",
      List(
        (MoodleTestGrade("272284", 1, 1.0, 1.0), MoodleTest(
          "Непряме (опосередковане) валютне котирування – це:",
          "Виберіть одну відповідь:",
          1,
          List(
            MoodleAnswer(None, isSelected = false, "одиниця іноземної валюти дорівнює певній кількості національної валюти", "a."),
            MoodleAnswer(None, isSelected = true, "множинність валютних курсів", "b."),
          )
        )
        ))
    )

    val writer = new MoodleTestReportWordWriter()
    val result = writer.write(moodleReport, actualWordPath)
    result should not be empty

    val filename = result.get.toString.replace(actualWordPath.toAbsolutePath + "/", "")
    s"${moodleReport.username.reduceExtraSymbols}-${moodleReport.title.reduceExtraSymbols}-${moodleReport.id}.docx" should equal(filename)

  }

}
