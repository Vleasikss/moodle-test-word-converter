package org.example.writer

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectWriter
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.example.MoodleTestReport

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class MoodleTestReportJsonWriter extends MoodleTestReportWriter {

  private lazy val objectWriter: ObjectWriter = new ObjectMapper()
    .registerModule(DefaultScalaModule)
    .writerWithDefaultPrettyPrinter()

  def write(report: MoodleTestReport, path: Path): Option[Path] = {
    try {
      val json = objectWriter.writeValueAsString(report)
      val absolutePath = Paths.get(path.toAbsolutePath.toString, reportFilename(report))
      Files.writeString(absolutePath, json)
      Option(absolutePath)
    } catch {
      case e: Throwable =>
        e.printStackTrace()
        None
    }
  }

  override def fileExtension: String = "json"

}
