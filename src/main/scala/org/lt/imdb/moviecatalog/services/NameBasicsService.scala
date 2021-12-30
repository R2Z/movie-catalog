package org.lt.imdb.moviecatalog.services

import org.lt.imdb.moviecatalog.entity.NameBasics
import org.lt.imdb.moviecatalog.repositories.NameBasicsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class NameBasicsService(@Autowired private val nameBasicsRepo : NameBasicsRepository) {

  def findByPrimaryName(primaryTitle:String) : NameBasics = {
    nameBasicsRepo.findByPrimaryName(primaryTitle)
  }

}
