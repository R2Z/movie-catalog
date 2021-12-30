package org.lt.imdb.moviecatalog.util

import org.junit.Assert._
import org.junit.Test
import org.lt.imdb.moviecatalog.model.MovieCatalog.MovieCrewDetail
import org.slf4j.LoggerFactory

import scala.collection.parallel.immutable.ParVector

class GraphUtilTest {

  private val LOGGER = LoggerFactory.getLogger(classOf[GraphUtilTest])

  private def loadCrewDetailFromJsonFile(filePath: String): Iterable[MovieCrewDetail] = JsonUtils.parseAndGet[Iterable[MovieCrewDetail]](MovieCatalogUtils.readFile(filePath))

  private def loadCrewDetailFromCSVFile(filePath: String): List[MovieCrewDetail] = {
    def func(str: String): MovieCrewDetail = {
      val strArr = str.replace("\"", "").split(",")
      MovieCrewDetail(strArr(0), strArr(1), strArr(2), strArr(3))
    }

    MovieCatalogUtils.readFile[MovieCrewDetail](filePath, func)
  }

  @Test
  def buildGraphTest(): Unit = {
    val filePath = "movie-catalog/movie-crew.json"
    val movieAndCrewDetails = loadCrewDetailFromJsonFile(filePath)
    assertEquals(10000, movieAndCrewDetails.size)
    val gr = GraphUtils.buildGraph(movieAndCrewDetails).graph
    assertNotNull(gr)
  }

  @Test
  def buildGraphFromCSVTest(): Unit = {
    val movieData = loadCrewDetailFromCSVFile("movie-catalog/movie-crew-3.csv")
    assertEquals(500000, movieData.size)
    val graph = GraphUtils.buildGraph(movieData).graph
    assertNotNull(graph)
    val movieCrew = graph.filter(_._1.value.contains("Hood of Horror"))
    assertEquals(10, movieCrew.map(_._2).head.size)

  }

  @Test
  def breadthFirstSearchTest(): Unit = {
    val filePath = "movie-catalog/movie-crew.json"
    val movieAndCrewDetails = loadCrewDetailFromJsonFile(filePath)
    assertEquals(10000, movieAndCrewDetails.size)
    val gr = GraphUtils.buildGraph(movieAndCrewDetails).graph
    assertNotNull(gr)
    val vector = ParVector.range(0, 5)
    vector.foreach { _ =>
      val result = GraphUtils.breadthFirstSearch(gr, "Guillermo Perrín", "Alberto Marro")
      LOGGER.info(s"${result.head}")
      assertEquals(5, result.head.size)
    }
  }

  @Test
  def breadthFirstSearchFromCSVTest(): Unit = {
    val movieAndCrewDetails = loadCrewDetailFromCSVFile("movie-catalog/movie-crew-3.csv")
    val gr = GraphUtils.buildGraph(movieAndCrewDetails).graph
    val vector = ParVector.range(0, 5)
    vector.foreach { _ =>
      val result = GraphUtils.breadthFirstSearch(gr, "Guillermo Perrín", "Alberto Marro")
      LOGGER.info(s"${result.head}")
      assertEquals(11, result.head.size)
    }
  }

}
