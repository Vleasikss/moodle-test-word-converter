package org.example.writer

import com.spire.doc.Document
import com.spire.doc.FileFormat
import com.spire.doc.documents.BuiltinStyle
import com.spire.doc.documents.ParagraphStyle
import org.example.MoodleTestReport

import java.nio.file.Path
import java.nio.file.Paths


class MoodleTestReportWordWriter extends ReportWriter[MoodleTestReport] {

  override def write(report: MoodleTestReport, path: Path): Option[Path] = {
    try {

      implicit val document: Document = new Document()
      val paraStyle = paragraphStyle
      document.getStyles.add(paraStyle)

      val section = document.addSection()
      val heading = section.addParagraph()
      heading.appendText(report.title)
      heading.applyStyle(BuiltinStyle.Title)

      report.tests.foreach { test =>
        val (grade, testInfo) = test

        val subheading = section.addParagraph()
        subheading.appendText(s"${testInfo.testNumber}. ${testInfo.name}")
        subheading.appendText(s"\nGrade: ${grade.grade}/${grade.maxGrade}").appendGradeStyle
        subheading.appendText(s"\n${testInfo.prompt}")
        subheading.applyStyle(BuiltinStyle.Heading_3)

        val para = section.addParagraph()

        testInfo.answers.foreach { answer =>
          para
            .appendText(s"${answer.number} - ${answer.label}\n")
            .appendAnswerStyle(answer.isCorrect, answer.isSelected)
        }

        para.applyStyle(paraStyle.getName)
      }

      val filename = generateFilename(report.id, report.username, report.title)
      val absolutePath = Paths.get(path.toAbsolutePath.toString, filename).toAbsolutePath

      document.saveToFile(absolutePath.toString, FileFormat.Docx)
      Option(absolutePath)
    } catch {
      case e: Throwable =>
        e.printStackTrace()
        None
    }
  }

  private def generateFilename(reportId: String, username: String, title: String): String =
    s"$username-$title-$reportId.$fileExtension".replaceAll("[ :]", "-")

  override def fileExtension: String = "docx"

  private def paragraphStyle(implicit document: Document): ParagraphStyle = {
    val s = new ParagraphStyle(document)
    s.setName("paraStyle")
    s.getCharacterFormat.setFontName("Times New Roman")
    s.getCharacterFormat.setFontSize(11f)
    s
  }


}
