package org.example

import java.nio.file.Path
import java.util.Objects
import scala.io.Source
import scala.language.implicitConversions
import scala.util.Using

trait TestUtils {

  implicit def toPath(path: String): Path = Path.of(path)
  implicit def toStr(path: Path): String = path.toAbsolutePath.toString

  protected def /(path: String): String =
    Objects.requireNonNull(this.getClass.getResource(path)).getPath

  protected def readFile(path: String): String =
    Using(Source.fromFile(path, "UTF-8"))(_.mkString).get
}
