package org.lt.imdb.moviecatalog.repositories

import org.lt.imdb.moviecatalog.util.GraphUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.{CacheConfig, Cacheable}
import org.springframework.context.annotation.Configuration

@Configuration
@CacheConfig(cacheNames = Array("movieCrewGraph"))
class MovieCatLogCacheManager(@Autowired private val catalogRepo: MovieCatalogRepoImpl) {

  private val LOGGER = LoggerFactory.getLogger(classOf[MovieCatLogCacheManager])

  @Cacheable
  def generateGraph: GraphUtils.Graph = {
    val t1 = System.currentTimeMillis()
    val movieAndCrewDetails = catalogRepo.loadMovieAndCrewDetails
    val t2 = System.currentTimeMillis()
    LOGGER.info(s"Time Taken to load data from db ${(t2 - t1) / 1000}")
    val graph = GraphUtils.buildGraph(movieAndCrewDetails)
    val t3 = System.currentTimeMillis()
    LOGGER.info(s"Time Taken to build graph ${(t3 - t2) / 1000}")
    graph
  }
}
