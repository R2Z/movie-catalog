package org.lt.imdb.moviecatalog.config

import org.lt.imdb.moviecatalog.repositories.MovieCatalogRepoImpl
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.{Bean, Configuration, Lazy}
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

@Configuration
class MovieCatalogConfig {

  @Lazy
  @ConditionalOnMissingBean(name = Array("MovieCatalogRepoImpl"))
  @Bean def movieCatalogImpl(@Qualifier("jdbcTemplate") jdbcTemplate: JdbcTemplate): MovieCatalogRepoImpl = {
    val namedParamJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource)
    new MovieCatalogRepoImpl(jdbcTemplate, namedParamJdbcTemplate)
  }

}
