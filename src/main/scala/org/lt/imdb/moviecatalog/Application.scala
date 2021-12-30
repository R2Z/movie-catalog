package org.lt.imdb.moviecatalog

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean

@SpringBootApplication
@EnableCaching
class Application {
  @Bean
  def mapper: ObjectMapper = {
    JsonMapper
      .builder()
      .addModule(DefaultScalaModule)
      .build()
  }
}

object Application extends App {
  SpringApplication.run(classOf[Application]);
}