package org.example.writer

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectWriter
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.example.MoodleTestReport
import org.example.collector.RawHtmlCollector

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.UUID

class MoodleTestReportJsonWriter(converter: RawHtmlCollector[MoodleTestReport]) extends RawHtmlFileWriter {

  private lazy val objectWriter: ObjectWriter = new ObjectMapper()
    .registerModule(DefaultScalaModule)
    .writerWithDefaultPrettyPrinter()

  def write(html: String, path: Path): Option[Path] = {
    try {
      val collect = converter.collect(html)
      val json = objectWriter.writeValueAsString(collect)
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
