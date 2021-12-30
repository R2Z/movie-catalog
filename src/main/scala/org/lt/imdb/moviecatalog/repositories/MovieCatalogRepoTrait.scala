package org.lt.imdb.moviecatalog.repositories

import org.lt.imdb.moviecatalog.model.MovieCatalog._

trait MovieCatalogRepoTrait {

  def getTitleCrew(tconst: String): Iterable[TitleCrew]

  def getTopRatedMovies(genres: String, topK: Int): Iterable[TopRatedMovies]

  def getMovieDetailsByName(primaryTitle: String, originalTitle: String): Iterable[MovieDetailRaw]

  def checkIfActorsExists(firstName: String, secondName: String): Boolean

  def getActorNameIdList(firstName: String, secondName: String): Map[String, List[String]]

  def loadMovieAndCrewDetails: Iterable[MovieCrewDetail]
}
