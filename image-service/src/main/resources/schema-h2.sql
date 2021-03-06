DROP TABLE IF EXISTS ALBUMS CASCADE;
DROP TABLE IF EXISTS ALBUMS_IMAGES CASCADE;
DROP TABLE IF EXISTS ARTISTS CASCADE;
DROP TABLE IF EXISTS IMAGE_AUTHORS CASCADE;
DROP TABLE IF EXISTS IMAGE_FILES CASCADE;
DROP TABLE IF EXISTS IMAGE_TYPES CASCADE;
DROP TABLE IF EXISTS IMAGES CASCADE;

CREATE TABLE ALBUMS (
  ALBUM_ID      BIGINT NOT NULL,
  USER_ID       BIGINT,
  TITLE         TEXT   NOT NULL,
  CREATION_DATE TIMESTAMP
);

CREATE TABLE ALBUMS_IMAGES (
  ALBUM_ID     BIGINT  NOT NULL,
  IMAGE_ID     BIGINT  NOT NULL,
  ORDER_NUMBER INTEGER NOT NULL
);

CREATE TABLE ARTISTS (
  ARTIST_ID  BIGINT NOT NULL,
  NAME       VARCHAR(255),
  NAME_2     VARCHAR(255),
  SURNAME    VARCHAR(255),
  BIRTH_YEAR TIMESTAMP,
  DEATH_YEAR TIMESTAMP,
  COMMENTS   TEXT,
  RATINGS    REAL
);

CREATE TABLE IMAGE_AUTHORS (
  IMAGE_ID  BIGINT NOT NULL,
  AUTHOR_ID BIGINT NOT NULL
);

CREATE TABLE IMAGE_FILES (
  IMAGE_FILE_ID  BIGINT               NOT NULL,
  FILE_NAME      VARCHAR(1024)        NOT NULL,
  FILE_SIZE      BIGINT               NOT NULL,
  FILE_EXTENSION VARCHAR(10)          NOT NULL,
  CREATION_DATE  TIMESTAMP            NOT NULL,
  IS_PUBLIC      BOOLEAN DEFAULT TRUE NOT NULL,
  FILE           OID                  NOT NULL
);

CREATE TABLE IMAGE_TYPES (
  TYPE_ID  BIGINT NOT NULL,
  NAME     TEXT   NOT NULL,
  COMMENTS TEXT
);

CREATE TABLE IMAGES (
  IMAGE_ID      BIGINT        NOT NULL,
  TITLE         VARCHAR(1024) NOT NULL,
  IMAGE_FILE_ID BIGINT        NOT NULL,
  NATIVE_WIDTH  INTEGER       NOT NULL,
  NATIVE_HEIGHT INTEGER       NOT NULL,
  RESOLUTION    INTEGER       NOT NULL,
  DEPTH         SMALLINT,
  COMMENTS      TEXT,
  RATING        REAL,
  RATING_TIMES  BIGINT,
  TYPE_ID       BIGINT,
  YEAR          SMALLINT,
  OWNER_ID      BIGINT
);

CREATE TABLE USERS (
  USER_ID     BIGINT        NOT NULL,
  USER_NAME   VARCHAR(255)  NOT NULL,
  PASSWORD    VARCHAR(255)  NOT NULL,
  NAME        VARCHAR(255),
  SURNAME     VARCHAR(255),
  NATIONALITY VARCHAR(255),
  EMAIL       VARCHAR(1024) NOT NULL
);

ALTER TABLE ALBUMS
  ADD CONSTRAINT PK_ALBUMS
PRIMARY KEY (ALBUM_ID);


ALTER TABLE ALBUMS_IMAGES
  ADD CONSTRAINT PK_ALBUMS_IMAGES
PRIMARY KEY (ALBUM_ID, IMAGE_ID);


ALTER TABLE ARTISTS
  ADD CONSTRAINT PK_IMAGE_AUTHORS
PRIMARY KEY (ARTIST_ID);


ALTER TABLE IMAGE_AUTHORS
  ADD CONSTRAINT PK_IMAGE_AUTHORS2
PRIMARY KEY (IMAGE_ID, AUTHOR_ID);


ALTER TABLE IMAGE_FILES
  ADD CONSTRAINT PK_IMAGE_FILES
PRIMARY KEY (IMAGE_FILE_ID);


ALTER TABLE IMAGE_TYPES
  ADD CONSTRAINT PK_IMAGE_TYPES
PRIMARY KEY (TYPE_ID);


ALTER TABLE IMAGES
  ADD CONSTRAINT PK_IMAGES
PRIMARY KEY (IMAGE_ID);


ALTER TABLE ALBUMS
  ADD CONSTRAINT FK_ALBUMS_USERS
FOREIGN KEY (USER_ID) REFERENCES USERS (USER_ID)
ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE ALBUMS_IMAGES
  ADD CONSTRAINT FK_ALBUMS_IMAGES_IMAGES
FOREIGN KEY (IMAGE_ID) REFERENCES IMAGES (IMAGE_ID)
ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE ALBUMS_IMAGES
  ADD CONSTRAINT FK_ALBUMS_IMAGES_ALBUMS
FOREIGN KEY (ALBUM_ID) REFERENCES ALBUMS (ALBUM_ID)
ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE IMAGE_AUTHORS
  ADD CONSTRAINT FK_IMAGE_AUTHORS_IMAGE_AUTHORS
FOREIGN KEY (AUTHOR_ID) REFERENCES ARTISTS (ARTIST_ID)
ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE IMAGE_AUTHORS
  ADD CONSTRAINT FK_IMAGE_AUTHORS_IMAGES
FOREIGN KEY (IMAGE_ID) REFERENCES IMAGES (IMAGE_ID)
ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE IMAGES
  ADD CONSTRAINT FK_IMAGES_IMAGE_FILES
FOREIGN KEY (IMAGE_FILE_ID) REFERENCES IMAGE_FILES (IMAGE_FILE_ID)
ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE IMAGES
  ADD CONSTRAINT FK_IMAGES_IMAGE_TYPES
FOREIGN KEY (TYPE_ID) REFERENCES IMAGE_TYPES (TYPE_ID)
ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE IMAGES
  ADD CONSTRAINT FK_IMAGES_USERS
FOREIGN KEY (OWNER_ID) REFERENCES USERS (USER_ID)
ON DELETE CASCADE ON UPDATE CASCADE;

CREATE INDEX ON IMAGES (IMAGE_FILE_ID);

CREATE INDEX ON IMAGES (TYPE_ID);

CREATE INDEX ON IMAGES (OWNER_ID);

CREATE INDEX ON IMAGE_AUTHORS (IMAGE_ID);

CREATE INDEX ON IMAGE_AUTHORS (AUTHOR_ID);

CREATE INDEX ON ALBUMS_IMAGES (ALBUM_ID);

CREATE INDEX ON ALBUMS_IMAGES (IMAGE_ID);

CREATE INDEX ON ALBUMS (USER_ID);

CREATE SEQUENCE DEFAULTDBSEQ START 1;

CREATE OR REPLACE VIEW USERS_VIEW AS
  SELECT *
  FROM USERS;