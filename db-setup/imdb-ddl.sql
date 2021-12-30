CREATE DATABASE imdb;

--     nconst (string) - alphanumeric unique identifier of the name/person
--     primaryName (string)– name by which the person is most often credited
--     birthYear – in YYYY format
--     deathYear – in YYYY format if applicable, else '\N'
--     primaryProfession (array of strings)– the top-3 professions of the person
--     knownForTitles (array of tconsts) – titles the person is known for
CREATE TABLE IF NOT EXISTS name_basics (
    nconst	            VARCHAR CONSTRAINT PK_NAME_BASICS PRIMARY KEY,
    primaryName	        VARCHAR,
    birthYear           INTEGER,
    deathYear	        INTEGER,
    primaryProfession   VARCHAR,
    knownForTitles      VARCHAR
);--   tconst (string) - alphanumeric unique identifier of the title
--   titleType (string) – the type/format of the title (e.g. movie, short, tvseries, tvepisode, video, etc)
--   primaryTitle (string) – the more popular title / the title used by the filmmakers on promotional materials at the point of release
--   originalTitle (string) - original title, in the original language
--   isAdult (boolean) - false: non-adult title; true: adult title
--   startYear (YYYY) – represents the release year of a title. In the case of TV Series, it is the series start year
--   endYear (YYYY) – TV Series end year.
--   runtimeMinutes – primary runtime of the title, in minutes
--   genres (string array) – includes up to three genres associated with the title
CREATE TABLE IF NOT EXISTS title_basics (
    tconst          VARCHAR CONSTRAINT PK_TITLE_BASICS PRIMARY KEY,
    titleType       VARCHAR,
    primaryTitle    VARCHAR,
    originalTitle   VARCHAR,
    isAdult         BOOLEAN,
    startYear       INTEGER,
    endYear         INTEGER,
    runtimeMinutes  INTEGER,
    genres          VARCHAR
);
--   tconst (string) - alphanumeric unique identifier of the title
--   averageRating – weighted average of all the individual user ratings
--   numVotes - number of votes the title has received
CREATE TABLE IF NOT EXISTS title_ratings (
    tconst          VARCHAR CONSTRAINT PK_TITLE_RATINGS PRIMARY KEY,
    averageRating   DOUBLE PRECISION,
    numVotes        INTEGER,
    FOREIGN KEY (tconst) REFERENCES title_basics(tconst)
);--   tconst (string) - alphanumeric unique identifier of the title
--   ordering (integer) – a number to uniquely identify rows for a given titleId
--   nconst (string) - alphanumeric unique identifier of the name/person
--   category (string) - the category of job that person was in
--   job (string) - the specific job title if applicable
--   characters (string) - the name of the character played if applicable
CREATE TABLE IF NOT EXISTS title_principals (
    tconst          VARCHAR,
    ordering        INTEGER,
    nconst          VARCHAR,
    category        VARCHAR,
    job             VARCHAR,
    characters      VARCHAR,
    PRIMARY KEY (tconst, ordering, nconst),
    FOREIGN KEY (tconst) REFERENCES title_basics(tconst),
    FOREIGN KEY (nconst) REFERENCES name_basics(nconst)
);
--   tconst (string) - alphanumeric unique identifier of the title
--   directors (array of nconsts) - director(s) of the given title
--   writers (array of nconsts) – writer(s) of the given title
CREATE TABLE IF NOT EXISTS title_crew (
    tconst      VARCHAR CONSTRAINT PK_TITLE_CREW PRIMARY KEY,
    directors   VARCHAR,
    writers     VARCHAR,
    FOREIGN KEY (tconst) REFERENCES title_basics(tconst)
);

