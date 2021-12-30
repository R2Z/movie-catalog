package org.lt.imdb.moviecatalog.util

import org.json4s.{DefaultFormats, Formats, JValue}

object JsonUtils {

  val defaults: Formats = DefaultFormats

  def parse(jsonStr: String): JValue = {
    org.json4s.native.JsonMethods.parse(jsonStr)
  }

  def parseAndGet[T: Manifest](jsonStr: String)(implicit formats: Formats = defaults): T = {
    val json = parse(jsonStr)
    json.extract[T]
  }

}
