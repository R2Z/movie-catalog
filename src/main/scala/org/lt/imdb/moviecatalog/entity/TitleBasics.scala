package org.lt.imdb.moviecatalog.entity

import javax.persistence.{Id, _}
import scala.beans.BeanProperty

@Entity
@Table(name = "title_basics")
class TitleBasics {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @BeanProperty
  var tconst: String = _

  @Column(name = "titletype")
  @BeanProperty
  var titleType: String = _

  @Column(name = "primarytitle")
  @BeanProperty
  var primaryTitle: String = _

  @Column(name = "originaltitle")
  @BeanProperty
  var originalTitle: String = _

  @Column(name = "isadult")
  @BeanProperty
  var isAdult: Boolean  = _

  @Column(name = "startyear")
  @BeanProperty
  var startYear: Integer = _

  @Column(name = "endyear")
  @BeanProperty
  var endYear: Integer = _

  @Column(name = "runtimeminutes")
  @BeanProperty
  var runtimeMinutes: Integer = _

  @Column(name = "genres")
  @BeanProperty
  var genres: String = _
}
