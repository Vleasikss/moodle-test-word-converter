package org.example.writer

import com.spire.doc.Document
import com.spire.doc.FileFormat
import com.spire.doc.documents.BuiltinStyle
import com.spire.doc.documents.ParagraphStyle
import com.spire.doc.fields.TextRange
import org.example.MoodleTestReport

import java.awt.Color
import java.nio.file.Path
import java.nio.file.Paths


class MoodleTestReportWordWriter extends ReportWriter[MoodleTestReport] {

  private lazy val GREEN = new Color(0, 255, 0)
  private lazy val RED = new Color(255, 0, 0)
  private lazy val BLUE = new Color(0, 64, 250)

  override def fileExtension: String = "docx"

  override def write(report: MoodleTestReport, path: Path): Option[Path] = {
    try {

      implicit val document: Document = new Document()
      document.getStyles.add(paragraphStyle)

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

          para.applyStyle("paraStyle")
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

  private def paragraphStyle(implicit document: Document): ParagraphStyle = {
    val s = new ParagraphStyle(document)
    s.setName("paraStyle")
    s.getCharacterFormat.setFontName("Times New Roman")
    s.getCharacterFormat.setFontSize(11f)
    s
  }

  private implicit class TextRangeImplicits(range: TextRange) {
    def appendAnswerStyle(isCorrect: Option[Boolean], isSelected: Boolean)(implicit document: Document): TextRange = {
      val color: Option[Color] = isCorrect match {
        case Some(value) => if (value) Option(GREEN) else Option(RED)
        case _ => if (isSelected) Option(BLUE) else None
      }

      range.getCharacterFormat.setFontName("Times New Roman")
      range.getCharacterFormat.setFontSize(16f)
      color.foreach(range.getCharacterFormat.setTextColor)
      range
    }

    def appendGradeStyle(implicit document: Document): TextRange = {
      range.getCharacterFormat.setItalic(true)
      range.getCharacterFormat.setBold(false)
      range
    }

  }

}
