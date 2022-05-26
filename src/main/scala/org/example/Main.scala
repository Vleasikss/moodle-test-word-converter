package org.example

import org.example.collector.RawMoodleTestHtmlReportCollector
import org.example.writer.MoodleTestReportWordWriter

import java.nio.file.Path
import java.nio.file.Paths
import scala.io.Source
import scala.language.implicitConversions
import scala.util.Using

object Main {

  implicit def toPath(somePath: String): Path = Paths.get(somePath)

  def main(args: Array[String]): Unit = {
    System.setProperty("file.encoding", "UTF-8")

    if (args.length != 2) {
      throw new RuntimeException(
        """
          |1. Html Input absolute path must be provided (for example folder1/folder2/file.html)
          |2. Html Output absolute path must be provided (for example folder3/folder4)
          |""".stripMargin)
    }

    val inputFileHtmlPath = args(0)
    val outputFileHtmlPath = args(1)


    val writer = new MoodleTestReportWordWriter()

    val input = Using(Source.fromFile(inputFileHtmlPath, "UTF-8"))(_.mkString).get
    val collector = new RawMoodleTestHtmlReportCollector()
    val report = collector.collect(input)

    writer.write(report, outputFileHtmlPath) match {
      case Some(path) => println(s"Successfully converted $inputFileHtmlPath into ${path.toAbsolutePath}")
      case _ => println(s"Unable to convert $inputFileHtmlPath")
    }

  }

}
