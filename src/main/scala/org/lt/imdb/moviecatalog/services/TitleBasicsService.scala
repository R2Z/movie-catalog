package org.lt.imdb.moviecatalog.services

import org.lt.imdb.moviecatalog.entity.TitleBasics
import org.lt.imdb.moviecatalog.repositories.TitleBasicsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TitleBasicsService(@Autowired private val titleBasicsRepo: TitleBasicsRepository) {

  def findFirstByPrimaryTitle(primaryTitle: String): TitleBasics = {
    titleBasicsRepo.findFirstByPrimaryTitleIgnoreCase(primaryTitle)
  }

}
