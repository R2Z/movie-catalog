package org.lt.imdb.moviecatalog.model

object MovieCatalog {

  case class TitleCrew(tconst: String, directors: String, writers: List[String])

  case class TopRatedMovies(title: String, rating: Double, genres: List[String])

  case class MovieCast(name: String, role: List[String])

  case class Movie(titleType: String, primaryTitle: String, originalTitle: String, ratings: Double)

  case class MovieDetail(movie: Movie, starCast: List[MovieCast])

  case class MovieDetailRaw(tconst: String, primaryTitle: String, originalTitle: String, genres: String, averageRating: Double, primaryName: String, primaryProfession: String)

  case class MovieCrewDetail(movieId: String, movieName: String, actorId: String, actorName: String)

  case class SocialNetwork(startName: String, endName: String, degreeOfSeparation: Int = -1, network: List[List[String]] = List.empty)

  object Status extends Enumeration {
    val SUCCESS, FAILURE = Value
  }

  case class ResponseStatus(status: String, message: String, timeTaken: Long)

  case class Response(responseStatus: ResponseStatus, socialNetwork: SocialNetwork)

}
