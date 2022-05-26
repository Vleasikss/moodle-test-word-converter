package org.example.collector

trait RawHtmlCollector[T] {

  def collect(rawHtml: String): T

}
