
PRAGMA encoding = "UTF-8"; 

CREATE TABLE Auth (
                    ID INT PRIMARY KEY NOT NULL,
                    NAME    TEXT   NOT NULL
                )

CREATE TABLE Users (
                    ID INT PRIMARY KEY NOT NULL,
                    NAME TEXT   NOT NULL,
                    SURNAME TEXT    NOT NULL,
                    ADDRESS TEXT    NOT NULL,
                    PHONE   INT     NOT NULL,
                    LOGIN   TEXT    NOT NULL,
                    PASSWORD    TEXT    NOT NULL,
                    ID_auth INT NOT NULL FOREIGN KEY(ID_auth) REFERENCES Auth(ID)
                    )
