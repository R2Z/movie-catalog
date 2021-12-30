package org.lt.imdb.moviecatalog.repositories

import org.lt.imdb.moviecatalog.entity.TitleBasics
import org.springframework.data.jpa.repository.JpaRepository

trait TitleBasicsRepository extends JpaRepository[TitleBasics, String] {
  def findFirstByPrimaryTitleIgnoreCase(primaryTitle: String): TitleBasics
}
