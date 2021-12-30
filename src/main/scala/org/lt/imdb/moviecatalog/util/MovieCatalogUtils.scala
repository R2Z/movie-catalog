package org.lt.imdb.moviecatalog.util

import scala.io.Source

object MovieCatalogUtils {

  private val RESOURCE_BASE_PATH = "src/test/resources/"

  def nonEmpty(str: String): Boolean = str != null && str.length > 0

  def readFile(filePath: String): String = {
    val path = s"$RESOURCE_BASE_PATH$filePath"
    val file = Source.fromFile(path)
    val data = file.getLines.mkString("\n")
    file.close
    data
  }

  def readFile[T](filePath: String, func: String => T): List[T] = Source.fromFile(s"$RESOURCE_BASE_PATH$filePath").getLines.toList.map(func(_))

}
