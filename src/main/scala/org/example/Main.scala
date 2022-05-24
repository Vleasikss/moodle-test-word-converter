package org.example

import org.example.collector.RawMoodleTestHtmlReportCollector
import org.example.writer.MoodleTestReportWordWriter

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import scala.language.implicitConversions

object Main {

  implicit def toPath(somePath: String): Path = Paths.get(somePath)

  def main(args: Array[String]): Unit = {

    if (args.length != 2) {
      throw new RuntimeException(
        """
          |1. Html Input path must be provided (for example folder1/folder2/file.html)
          |2. Html Output path must be provided (for example folder3/folder4)
          |""".stripMargin)
    }

    val inputFileHtmlPath = args(0)
    val outputFileHtmlPath = args(1)

    val writer = new MoodleTestReportWordWriter(new RawMoodleTestHtmlReportCollector)

    writer.write(Files.readString(inputFileHtmlPath), outputFileHtmlPath) match {
      case Some(path) => println(s"Successfully converted $inputFileHtmlPath into ${path.toAbsolutePath}")
      case _ => println(s"Unable to convert $inputFileHtmlPath")
    }

  }

}
