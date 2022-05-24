package org.example.writer

import java.nio.file.Path

trait RawHtmlFileWriter {

  def write(rawHtml: String, path: Path): Option[Path]

  def fileExtension: String

}
