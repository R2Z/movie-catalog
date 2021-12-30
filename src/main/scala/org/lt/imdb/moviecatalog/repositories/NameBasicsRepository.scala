package org.lt.imdb.moviecatalog.repositories

import org.lt.imdb.moviecatalog.entity.NameBasics
import org.springframework.data.jpa.repository.JpaRepository

trait NameBasicsRepository extends JpaRepository[NameBasics, String] {
  def findByPrimaryName(primaryName: String):NameBasics
}
