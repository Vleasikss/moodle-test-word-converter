package org.example.writer

import org.example.MoodleTestReport

abstract class MoodleTestReportWriter extends ReportWriter[MoodleTestReport] {

  def fileExtension: String

  override def filename(report: MoodleTestReport): String = {
    s"${report.username}-${report.title}-${report.id}.$fileExtension".replaceAll("[ :]", "-")
  }

}
