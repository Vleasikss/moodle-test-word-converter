package org.example.collector

trait RawHtmlCollector[T] {

  def collect(html: String): T

}
