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

    val writer = new MoodleTestReportWordWriter()
    val result = writer.write(moodleReport, actualWordPath)
    result should not be empty

    val filename = result.get.toString.replace(actualWordPath.toAbsolutePath + "/", "")
    s"${moodleReport.username.reduceExtraSymbols}-${moodleReport.title.reduceExtraSymbols}-${moodleReport.id}.docx" should equal(filename)

  }

}
