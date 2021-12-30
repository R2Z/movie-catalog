package org.lt.imdb.moviecatalog.controllers

import org.lt.imdb.moviecatalog.entity.{NameBasics, TitleBasics}
import org.lt.imdb.moviecatalog.model.MovieCatalog.TitleCrew
import org.lt.imdb.moviecatalog.services.{MovieCatalogService, NameBasicsService, TitleBasicsService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{RequestMapping, RequestMethod, RequestParam, RestController}

@RestController
@RequestMapping(Array("/test"))
class TestController(@Autowired private val nameBasicsService: NameBasicsService,
                     @Autowired private val titleBasicsService: TitleBasicsService,
                     @Autowired private val catalogService: MovieCatalogService) {

  @RequestMapping(method = Array(RequestMethod.GET), value = Array("/getNameBasics"))
  def findByPrimaryName(@RequestParam(name = "name", defaultValue = "Lauren Bacall") name: String): NameBasics = {
    nameBasicsService.findByPrimaryName(name)
  }

  @RequestMapping(method = Array(RequestMethod.GET), value = Array("/getTitleBasics"))
  def findMovieByTitle(@RequestParam(name = "name", defaultValue = "Bohemios") name: String): TitleBasics = {
    titleBasicsService.findFirstByPrimaryTitle(name)
  }

  @RequestMapping(method = Array(RequestMethod.GET), value = Array("/getTitleCrew"))
  def getTitleCrew(@RequestParam(name = "tconst", defaultValue = "tt0000502") tconst: String): Iterable[TitleCrew] = {
    catalogService.getTitleCrew(tconst)
  }

}
