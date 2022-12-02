create table if not exists FILMS
(
    FILM_ID      INTEGER auto_increment
        primary key,
    NAME         CHARACTER VARYING(100)  not null,
    DESCRIPTION  CHARACTER VARYING(1000) not null,
    RELEASE_DATE DATE                    not null,
    DURATION     INTEGER                 not null
);

create table if not exists GENRES
(
    GENRE_ID   INTEGER auto_increment
        primary key,
    GENRE_NAME CHARACTER VARYING(20) UNIQUE not null
);

create table if not exists FILM_GENRES
(
    FILM_ID  INTEGER not null,
    GENRE_ID INTEGER not null,
    constraint FK_FILM_GENRES_FILM_ID
        foreign key (FILM_ID) references FILMS,
    constraint FK_FILM_GENRES_GENRE_ID
        foreign key (GENRE_ID) references GENRES
);

create table if not exists MPA_RATINGS
(
    MPA_RATING_ID   INTEGER auto_increment
        primary key,
    MPA_RATING_NAME CHARACTER VARYING(5) UNIQUE not null
);

create table if not exists FILM_MPA_RATINGS
(
    FILM_ID       INTEGER not null,
    MPA_RATING_ID INTEGER not null,
    constraint FK_FILM_MPA_RATING_FILM_ID
        foreign key (FILM_ID) references FILMS,
    constraint FK_FILM_MPA_RATING_MPA_RATING_ID
        foreign key (MPA_RATING_ID) references MPA_RATINGS
);

create table if not exists USERS
(
    USER_ID  INTEGER auto_increment
        primary key,
    EMAIL    CHARACTER VARYING(256) UNIQUE,
    LOGIN    CHARACTER VARYING(50) UNIQUE,
    NAME     CHARACTER VARYING(100),
    BIRTHDAY DATE
);

create table if not exists FRIENDS
(
    USER_ID           INTEGER not null,
    FRIEND_ID         INTEGER not null,
    FRIENDSHIP_STATUS INTEGER,
    constraint FK_FRIENDS_FRIEND_ID
        foreign key (FRIEND_ID) references USERS,
    constraint FK_FRIENDS_USER_ID
        foreign key (USER_ID) references USERS
);

create table if not exists LIKES
(
    FILM_ID INTEGER not null,
    USER_ID INTEGER not null,
    constraint FK_LIKES_FILM_ID
        foreign key (FILM_ID) references FILMS,
    constraint FK_LIKES_USER_ID
        foreign key (USER_ID) references USERS
);
