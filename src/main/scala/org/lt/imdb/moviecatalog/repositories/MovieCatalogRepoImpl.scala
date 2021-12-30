package org.lt.imdb.moviecatalog.repositories

import org.lt.imdb.moviecatalog.model.MovieCatalog._
import org.springframework.jdbc.core.namedparam.{MapSqlParameterSource, NamedParameterJdbcTemplate}
import org.springframework.jdbc.core.{JdbcTemplate, RowMapper}

import java.sql.ResultSet
import scala.collection.JavaConverters._
import scala.collection.mutable

class MovieCatalogRepoImpl(jdbcTemplate: JdbcTemplate, namedParamJdbcTemplate: NamedParameterJdbcTemplate) extends MovieCatalogRepoTrait {

  override def getTitleCrew(tconst: String): Iterable[TitleCrew] = {

    val query = "select * from title_crew where tconst = ? "
    class MovieTitleRowMapper extends RowMapper[TitleCrew] {
      override def mapRow(resultSet: ResultSet, i: Int): TitleCrew = {
        val writers = resultSet.getString("writers")
        val writerList = if (writers != null) {
          writers.split(",").toList
        } else List.empty

        TitleCrew(resultSet.getString("tconst"),
          resultSet.getString("directors"),
          writerList)
      }
    }

    val args = Array(tconst)
    val result = jdbcTemplate.query[TitleCrew](query, args.map(_.asInstanceOf[AnyRef]), new MovieTitleRowMapper)
    result.asScala.toList
  }

  override def getTopRatedMovies(genres: String, topK: Int): Iterable[TopRatedMovies] = {

    val query =
      s"""
         | select tb.originalTitle,tr.averageRating,tb.genres from title_basics tb
         | join title_ratings tr on tb.tconst = tr.tconst
         | where lower(tb.genres) like :genres order by tr.averageRating desc,tb.originalTitle limit :topK
         |""".stripMargin

    val namedParams = new MapSqlParameterSource()
    namedParams.addValue("genres", s"%${genres.toLowerCase}%")
    namedParams.addValue("topK", topK)

    class TopRatedMoviesMapper extends RowMapper[TopRatedMovies] {
      override def mapRow(resultSet: ResultSet, i: Int): TopRatedMovies = {

        val genres = resultSet.getString("genres")
        val genresList = if (genres != null) {
          genres.split(",").toList
        } else List.empty

        TopRatedMovies(
          resultSet.getString("originalTitle"),
          resultSet.getDouble("averageRating"),
          genresList
        )
      }
    }

    val result = namedParamJdbcTemplate.query(query, namedParams, new TopRatedMoviesMapper)
    result.asScala.toList
  }

  override def getMovieDetailsByName(primaryTitle: String, originalTitle: String): Iterable[MovieDetailRaw] = {

    val query =
      """
        |select tpr.tconst,
        |tbs.primaryTitle,
        |tbs.originalTitle,
        |tbs.genres,
        |trg.averageRating,
        |nbs.primaryName,nbs.primaryProfession
        |from title_principals tpr
        |join name_basics nbs on tpr.nconst = nbs.nconst
        |join title_basics tbs on tpr.tconst = tbs.tconst
        |join title_ratings trg on tpr.tconst = trg.tconst
        |where tbs.primaryTitle = ? or tbs.originalTitle = ?
        |order by tpr.tconst
        |""".stripMargin

    val args = Array(primaryTitle, originalTitle)

    class MovieDetailsMapper extends RowMapper[MovieDetailRaw] {
      override def mapRow(resultSet: ResultSet, i: Int): MovieDetailRaw = {
        MovieDetailRaw(
          resultSet.getString("tconst"),
          resultSet.getString("primaryTitle"),
          resultSet.getString("originalTitle"),
          resultSet.getString("genres"),
          resultSet.getDouble("averageRating"),
          resultSet.getString("primaryName"),
          resultSet.getString("primaryProfession")
        )
      }
    }

    val result = jdbcTemplate.query[MovieDetailRaw](query, args.map(_.asInstanceOf[AnyRef]), new MovieDetailsMapper)
    result.asScala.toList
  }

  override def checkIfActorsExists(firstName: String, secondName: String): Boolean = {
    val query = "select count(1) cnt from name_basics where primaryName in ( ? , ? )"
    val args = Array(firstName, secondName)
    val result = jdbcTemplate.query(query, args.map(_.asInstanceOf[AnyRef]), new RowMapper[Boolean] {
      override def mapRow(resultSet: ResultSet, i: Int): Boolean = {
        if (firstName == secondName) {
          resultSet.getInt("cnt") >= 1
        } else {
          resultSet.getInt("cnt") >= 2
        }
      }
    })
    result.asScala.toList.head
  }

  override def getActorNameIdList(firstName: String, secondName: String): Map[String, List[String]] = {
    val query = "select nconst,primaryName from name_basics where primaryName in ( ? , ? )"
    val args = Array(firstName, secondName)
    val result = jdbcTemplate.query(query, args.map(_.asInstanceOf[AnyRef]), new RowMapper[Map[String, String]] {
      override def mapRow(resultSet: ResultSet, i: Int): Map[String, String] = {
        Map(resultSet.getString("nconst") -> resultSet.getString("primaryName"))
      }
    })

    val IdNameMap = result.asScala.toList.flatten.toMap
    val nameIdListMap = new mutable.HashMap[String, List[String]]
    IdNameMap.map { case (k, v) =>
      val IdList = nameIdListMap.getOrElse(v, List.empty)
      nameIdListMap += (v -> (IdList ++ List(k)))
    }
    nameIdListMap.toMap
  }

  override def loadMovieAndCrewDetails: Iterable[MovieCrewDetail] = {
    val query =
      """
        |select
        |tbs.tconst ,tbs.originalTitle,
        |nbs.nconst ,nbs.primaryName
        |from title_principals prp join name_basics nbs on prp.nconst = nbs.nconst
        |join title_basics tbs on prp.tconst = tbs.tconst
        |where ( nbs.primaryprofession like '%actor%' or  nbs.primaryprofession like '%actress%')
        |""".stripMargin
    val result = jdbcTemplate.query(query, new RowMapper[MovieCrewDetail] {
      override def mapRow(resultSet: ResultSet, i: Int): MovieCrewDetail = {
        MovieCrewDetail(resultSet.getString("tconst"), resultSet.getString("originalTitle"),
          resultSet.getString("nconst"), resultSet.getString("primaryName"))
      }
    })
    result.asScala.toList
  }
}
