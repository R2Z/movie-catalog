package org.lt.imdb.moviecatalog.controllers

import org.lt.imdb.moviecatalog.model.MovieCatalog.{MovieDetail, Response, TopRatedMovies}
import org.lt.imdb.moviecatalog.services.MovieCatalogService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{RequestMapping, RequestMethod, RequestParam, RestController}

@RestController
@RequestMapping(Array("/api"))
class MovieCatalogController(@Autowired private val catalogService: MovieCatalogService) {


  @RequestMapping(method = Array(RequestMethod.GET), value = Array("/getTopRatedMovies"))
  def getTopRatedMovies(@RequestParam(name = "genres", defaultValue = "Biography") genres: String,
                        @RequestParam(name = "topK", defaultValue = "10") topK: Int): Iterable[TopRatedMovies] = {
    catalogService.getTopRatedMovies(genres, topK)
  }

  @RequestMapping(method = Array(RequestMethod.GET), value = Array("/getMovieDetailsByName"))
  def getMovieDetailsByName(@RequestParam(name = "primaryTitle", defaultValue = "Bohemios") primaryTitle: String,
                            @RequestParam(name = "originalTitle", defaultValue = "Bohemios") originalTitle: String): Iterable[MovieDetail] = {
    catalogService.getMovieDetailsByName(primaryTitle, originalTitle)
  }

  @RequestMapping(method = Array(RequestMethod.GET), value = Array("/findDegreeOfSeparation"))
  def findDegreeOfSeparation(@RequestParam(name = "firstName", defaultValue = "Russell Crowe") firstName: String,
                             @RequestParam(name = "secondName", defaultValue = "Tom Cruise") secondName: String): Response = {
    catalogService.findDegreeOfSeparation(firstName, secondName)
  }

}
