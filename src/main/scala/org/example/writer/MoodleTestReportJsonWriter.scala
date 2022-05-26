package org.example.writer

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectWriter
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.example.MoodleTestReport

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.UUID

class MoodleTestReportJsonWriter extends ReportWriter[MoodleTestReport] {

  private lazy val objectWriter: ObjectWriter = new ObjectMapper()
    .registerModule(DefaultScalaModule)
    .writerWithDefaultPrettyPrinter()

  def write(report: MoodleTestReport, path: Path): Option[Path] = {
    try {
      val json = objectWriter.writeValueAsString(report)
      val absolutePath = Paths.get(path.toAbsolutePath.toString, UUID.randomUUID().toString.substring(0, 6) + "." + fileExtension)
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
