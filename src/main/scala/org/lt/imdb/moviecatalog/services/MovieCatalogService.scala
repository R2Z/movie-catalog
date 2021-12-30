package org.lt.imdb.moviecatalog.services

import org.lt.imdb.moviecatalog.model.MovieCatalog.{TitleCrew, _}
import org.lt.imdb.moviecatalog.repositories.{MovieCatLogCacheManager, MovieCatalogRepoImpl}
import org.lt.imdb.moviecatalog.util.MovieCatalogUtils._
import org.lt.imdb.moviecatalog.util._
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MovieCatalogService(@Autowired private val catalogRepo: MovieCatalogRepoImpl,
                          @Autowired private val cache: MovieCatLogCacheManager) extends MovieCatalogTrait {

  private val LOGGER = LoggerFactory.getLogger(classOf[MovieCatalogService])

  override def getTitleCrew(tconst: String): Iterable[TitleCrew] = catalogRepo.getTitleCrew(tconst)

  override def getTopRatedMovies(genres: String, topK: Int): Iterable[TopRatedMovies] = catalogRepo.getTopRatedMovies(genres, topK)

  override def getMovieDetailsByName(primaryTitle: String, originalTitle: String): Iterable[MovieDetail] = {
    val details = catalogRepo.getMovieDetailsByName(primaryTitle, originalTitle)
    val movieDetails = details.groupBy(_.tconst).
      map(d => d._2.
        map(d => (Movie(d.primaryTitle, d.originalTitle, d.genres, d.averageRating), MovieCast(d.primaryName, d.primaryProfession.split(",").toList))).groupBy(_._1))
    movieDetails.flatten.map(d => MovieDetail(d._1, d._2.map(_._2).toList))
  }

  override def findDegreeOfSeparation(firstName: String, secondName: String): Response = {

    val t1 = System.currentTimeMillis()
    if (nonEmpty(firstName) && nonEmpty(secondName) && catalogRepo.checkIfActorsExists(firstName, secondName)) {
      val graph = cache.generateGraph.graph
      val t3 = System.currentTimeMillis()
      val ans = GraphUtils.breadthFirstSearch(graph, firstName, secondName)
      val t4 = System.currentTimeMillis()
      LOGGER.info(s"Time Taken to findDegreeOfSeparation ${(t4 - t3) / 1000}")
      if (ans.nonEmpty) {
        Response(ResponseStatus(Status.SUCCESS.toString, s"Found network between $firstName and $secondName", (System.currentTimeMillis() - t1) / 1000),
          SocialNetwork(firstName, secondName, ans.map(_.size).min / 2, ans))
      } else {
        Response(
          ResponseStatus(Status.SUCCESS.toString, s"Found non network between $firstName and $secondName", (System.currentTimeMillis() - t1) / 1000),
          SocialNetwork(firstName, secondName))
      }
    } else {
      Response(ResponseStatus(Status.FAILURE.toString, s"Either of firstName $firstName or secondName $secondName missing in repository", (System.currentTimeMillis() - t1) / 1000),
        SocialNetwork(firstName, secondName))
    }
  }
}
