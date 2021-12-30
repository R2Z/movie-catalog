# Six Degrees of Kevin Bacon

https://en.wikipedia.org/wiki/Six_Degrees_of_Kevin_Bacon

https://oracleofbacon.org/


## Prerequisites
```bash
apache-maven-3.8.4 
java 8
scala 2.10.7
```

## Installation

Database set-up guide

```bash
  1. Install docker on windows
Follow Steps provided - 
https://www.youtube.com/watch?v=RdPYA-wDhTA&t=491s


docker run --name imdb-postgres-container -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres

This command will download and start postgres, starting postgres image can also be done using docker client.

2. Download sqlclient for postgress pgAdmin

https://www.pgadmin.org/download/pgadmin-4-windows/


3. Data Import database.

From directory db-setup/compressed unzip .rar files and import using pg4Admin as steps provided in document.
```
## Build , Deployment & Testing

To build this project go to project root directory and run maven command

```bash
  mvn clean install -Dmaven.test.skip=true
```

On successful build of project project can be started using below commands

```bash
  1. Using java command, go to target directory and run command given below
     java -jar -Xms3g  -Xmx3g movie-catalog-0.0.1-SNAPSHOT.jar

  2. Using maven command, go to project direcory and run below command
     mvn -Dmaven.test.skip=true spring-boot:run
```

Upon successful start , application api's can be tested using swagger end point

```bash
  http://localhost:8081/swagger-ui.html
```
    