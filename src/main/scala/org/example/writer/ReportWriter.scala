package org.example.writer

import org.example.Report

import java.nio.file.Path

trait ReportWriter[T <: Report] {

  def write(report: T, path: Path): Option[Path]

  def filename(report: T): String

}
