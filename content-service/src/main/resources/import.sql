delete from ads;
delete from songs;

insert into ads (id, duration, rep_per_zone, starting_date, ending_date, timezone) values (1001, 15, 0, PARSEDATETIME('01-01-2022','dd-MM-yyyy'), PARSEDATETIME('01-03-2022','dd-MM-yyyy'), 'LateNight');
insert into ads (id, duration, rep_per_zone, starting_date, ending_date, timezone) values (1002, 30, 0, PARSEDATETIME('01-01-2022','dd-MM-yyyy'), PARSEDATETIME('01-04-2022','dd-MM-yyyy'), 'LateNight');

insert into genres (id, title) values (1, 'Hip hop');
insert into genres (id, title) values (2, 'Rock');

insert into songs (id, title, artist, release_year, duration, genre_id) values(7001, 'Ride', 'Twenty One Pilots', 2015, 25, 1);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7002, 'Stressed Out', 'Twenty One Pilots', 2015, 5, 1);

insert into broadcasts (id, duration, starting_date, starting_time, type) values(3001, 200, PARSEDATETIME('01-02-2022','dd-MM-yyyy'), '00:00', 'PLAYLIST');

insert into ad_broadcasts (id, broadcast_date, broadcast_time, ad_id, broadcast_id) values (4001, PARSEDATETIME('01-02-2022','dd-MM-yyyy'), '00:40', 1001, 3001);
insert into ad_broadcasts (id, broadcast_date, broadcast_time, ad_id, broadcast_id) values (4002, PARSEDATETIME('01-02-2022','dd-MM-yyyy'), '01:50', 1002, 3001);

insert into song_broadcasts (id, broadcast_date, broadcast_time, song_id, broadcast_id) values (5001, PARSEDATETIME('01-02-2022','dd-MM-yyyy'), '00:00', 2001, 3001);
insert into song_broadcasts (id, broadcast_date, broadcast_time, song_id, broadcast_id) values (5002, PARSEDATETIME('01-02-2022','dd-MM-yyyy'), '01:10', 2002, 3001);