package org.lt.imdb.moviecatalog.entity

import javax.persistence.{Id, _}
import scala.beans.BeanProperty

@Entity
@Table(name = "name_basics")
class NameBasics {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @BeanProperty
  var nconst: String = _

  @Column(name = "primaryname")
  @BeanProperty
  var primaryName: String = _

  @Column(name = "birthyear")
  @BeanProperty
  var birthYear: Integer = _

  @Column(name = "deathyear")
  @BeanProperty
  var deathYear: Integer = _

  @Column(name = "primaryprofession")
  @BeanProperty
  var primaryProfession: String = _

  @Column(name = "knownfortitles")
  @BeanProperty
  var knownForTitles: String = _
}
