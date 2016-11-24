DROP TABLE IF EXISTS EBOOK CASCADE;
DROP TABLE IF EXISTS EBOOK_AUTHORS CASCADE;
DROP TABLE IF EXISTS EBOOK_FILES CASCADE;
DROP TABLE IF EXISTS LIBRARIES_EBOOKS CASCADE;
DROP TABLE IF EXISTS LITERARY_GENRE CASCADE;
DROP TABLE IF EXISTS USERS_LIBRARIES CASCADE;
DROP TABLE IF EXISTS WRITERS CASCADE;

CREATE TABLE EBOOK (
  EBOOK_ID      BIGINT   NOT NULL,
  TITLE         TEXT     NOT NULL,
  NUM_OF_PAGES  INTEGER  NOT NULL,
  YEAR          SMALLINT NOT NULL,
  RATING        REAL,
  GENRE_ID      BIGINT,
  COMMENTS      TEXT,
  AUTHOR_ID     BIGINT,
  OWNER_ID      BIGINT,
  EBOOK_FILE_ID BIGINT   NOT NULL
);

CREATE TABLE EBOOK_AUTHORS (
  EBOOK_ID  BIGINT NOT NULL,
  AUTHOR_ID BIGINT NOT NULL
);

CREATE TABLE EBOOK_FILES (
  EBOOK_FILE_ID BIGINT               NOT NULL,
  FILE_NAME     VARCHAR(1024)        NOT NULL,
  FILE_PATH     TEXT                 NOT NULL,
  EXTENSION     VARCHAR(10)          NOT NULL,
  CREATION_DATE TIMESTAMP            NOT NULL,
  IS_PUBLIC     BOOLEAN DEFAULT TRUE NOT NULL,
  FILE          OID                  NOT NULL
);

CREATE TABLE LIBRARIES_EBOOKS (
  LIBRARY_ID   BIGINT  NOT NULL,
  EBOOK_ID     BIGINT  NOT NULL,
  ORDER_NUMBER INTEGER NOT NULL
);

CREATE TABLE LITERARY_GENRE (
  GENRE_ID BIGINT NOT NULL,
  NAME     TEXT   NOT NULL,
  COMMENTS TEXT
);

CREATE TABLE USERS_LIBRARIES (
  LIBRARY_ID    BIGINT NOT NULL,
  USER_ID       BIGINT,
  TITLE         TEXT   NOT NULL,
  CREATION_DATE TIMESTAMP
);

CREATE TABLE WRITERS (
  WRITER_ID  BIGINT NOT NULL,
  NAME       VARCHAR(255),
  NAME_2     VARCHAR(255),
  SURNAME    VARCHAR(255),
  BIRTH_DATE TIMESTAMP,
  DEATH_DATE TIMESTAMP,
  COMMENTS   TEXT,
  RATINGS    REAL
);


ALTER TABLE EBOOK
  ADD CONSTRAINT PK_EBOOK
PRIMARY KEY (EBOOK_ID);


ALTER TABLE EBOOK_AUTHORS
  ADD CONSTRAINT PK_EBOOK_AUTHORS
PRIMARY KEY (EBOOK_ID, AUTHOR_ID);


ALTER TABLE EBOOK_FILES
  ADD CONSTRAINT PK_EBOOK_FILES
PRIMARY KEY (EBOOK_FILE_ID);


ALTER TABLE LIBRARIES_EBOOKS
  ADD CONSTRAINT PK_LIBRARIES_EBOOKS
PRIMARY KEY (LIBRARY_ID, EBOOK_ID);


ALTER TABLE LITERARY_GENRE
  ADD CONSTRAINT PK_LITERARY_GENRE
PRIMARY KEY (GENRE_ID);


ALTER TABLE USERS_LIBRARIES
  ADD CONSTRAINT PK_USERS_LIBRARIES
PRIMARY KEY (LIBRARY_ID);


ALTER TABLE WRITERS
  ADD CONSTRAINT PK_WRITERS
PRIMARY KEY (WRITER_ID);


ALTER TABLE EBOOK
  ADD CONSTRAINT FK_EBOOK_EBOOK_FILES
FOREIGN KEY (EBOOK_FILE_ID) REFERENCES EBOOK_FILES (EBOOK_FILE_ID)
ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE EBOOK
  ADD CONSTRAINT FK_EBOOK_LITERARY_GENRE
FOREIGN KEY (GENRE_ID) REFERENCES LITERARY_GENRE (GENRE_ID)
ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE EBOOK
  ADD CONSTRAINT FK_EBOOK_USERS
FOREIGN KEY (OWNER_ID) REFERENCES USERS (USER_ID)
ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE EBOOK_AUTHORS
  ADD CONSTRAINT FK_EBOOK_AUTHORS_EBOOK
FOREIGN KEY (EBOOK_ID) REFERENCES EBOOK (EBOOK_ID)
ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE EBOOK_AUTHORS
  ADD CONSTRAINT FK_EBOOK_AUTHORS_WRITERS
FOREIGN KEY (AUTHOR_ID) REFERENCES WRITERS (WRITER_ID)
ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE LIBRARIES_EBOOKS
  ADD CONSTRAINT FK_LIBRARIES_EBOOKS_EBOOK
FOREIGN KEY (EBOOK_ID) REFERENCES EBOOK (EBOOK_ID)
ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE LIBRARIES_EBOOKS
  ADD CONSTRAINT FK_LIBRARIES_EBOOKS_USERS_LIBRARIES
FOREIGN KEY (LIBRARY_ID) REFERENCES USERS_LIBRARIES (LIBRARY_ID)
ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE USERS_LIBRARIES
  ADD CONSTRAINT FK_USERS_LIBRARIES_USERS
FOREIGN KEY (USER_ID) REFERENCES USERS (USER_ID)
ON DELETE CASCADE ON UPDATE CASCADE;

CREATE INDEX ON EBOOK_AUTHORS (EBOOK_ID);

CREATE INDEX ON EBOOK_AUTHORS (AUTHOR_ID);

CREATE INDEX ON EBOOK (GENRE_ID);

CREATE INDEX ON EBOOK (OWNER_ID);

CREATE INDEX ON EBOOK (EBOOK_FILE_ID);

CREATE INDEX ON LIBRARIES_EBOOKS (LIBRARY_ID);

CREATE INDEX ON LIBRARIES_EBOOKS (EBOOK_ID);

CREATE INDEX ON USERS_LIBRARIES (USER_ID);

CREATE SEQUENCE DEFAULTDBSEQ START 1;

CREATE OR REPLACE VIEW USERS_VIEW AS
  SELECT *
  FROM USERS;