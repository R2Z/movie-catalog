package org.lt.imdb.moviecatalog.services

import org.lt.imdb.moviecatalog.model.MovieCatalog.{MovieDetail, Response, TitleCrew, TopRatedMovies}

trait MovieCatalogTrait {

  def getTitleCrew(tconst: String): Iterable[TitleCrew]

  def getTopRatedMovies(genres: String, topK: Int): Iterable[TopRatedMovies]

  def getMovieDetailsByName(primaryTitle: String, originalTitle: String): Iterable[MovieDetail]

  def findDegreeOfSeparation(firstName: String, secondName: String): Response

}
