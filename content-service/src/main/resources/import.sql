delete from ads;
delete from songs;
delete from genres;

insert into ads (id, duration, rep_per_zone, starting_date, ending_date, timezone) values (1001, 15, 0, PARSEDATETIME('01-01-2022','dd-MM-yyyy'), PARSEDATETIME('01-03-2022','dd-MM-yyyy'), 'LateNight');
insert into ads (id, duration, rep_per_zone, starting_date, ending_date, timezone) values (1002, 30, 0, PARSEDATETIME('01-01-2022','dd-MM-yyyy'), PARSEDATETIME('01-04-2022','dd-MM-yyyy'), 'LateNight');

insert into genres (id, title) values (1, 'Hip hop');
insert into genres (id, title) values (2, 'Rock');

insert into songs (id, title, artist, release_year, duration, genre_id) values(7001, 'Ride', 'Twenty One Pilots', 2015, 25, 1);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7002, 'Stressed Out', 'Twenty One Pilots', 2015, 5, 1);

