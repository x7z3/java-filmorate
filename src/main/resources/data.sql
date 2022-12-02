insert into MPA_RATINGS (MPA_RATING_NAME)
select *
from (values ('G'),
             ('PG'),
             ('PG-13'),
             ('R'),
             ('NC-17'));


insert into GENRES (GENRE_NAME)
select *
from (values ('Комедия'),
             ('Драма'),
             ('Мультфильм'),
             ('Триллер'),
             ('Документальный'),
             ('Боевик'));