delete from users;
delete from add_broadcasts;
delete from song_broadcasts;
delete from adds;
delete from songs;
delete from broadcasts;

insert into users (id, username, password, email, role) values(6001, 'producer', 'producer', 'producer@email.com', 'PRODUCER');
insert into users (id, username, password, email, role) values(6002, 'user', 'user', 'user@email.com', 'USER');

insert into adds (id, duration, rep_per_zone, starting_date, ending_date, timezone) values (1001, 15, 0, PARSEDATETIME('01-01-2022','dd-MM-yyyy'), PARSEDATETIME('01-03-2022','dd-MM-yyyy'), 'LateNight');
insert into adds (id, duration, rep_per_zone, starting_date, ending_date, timezone) values (1002, 30, 0, PARSEDATETIME('01-01-2022','dd-MM-yyyy'), PARSEDATETIME('01-04-2022','dd-MM-yyyy'), 'LateNight');

insert into songs (id, title, artist, releaseYear, duration, genre) values(2001, 'title', 'artist', 2023, 25, 'genre');
insert into songs (id, title, artist, releaseYear, duration, genre) values(2002, 'title1', 'artist1', 2023, 10, 'genre2');

insert into broadcasts (id, duration, startingDate, startingTime, type) values(3001, 200, PARSEDATETIME('01-02-2022','dd-MM-yyyy'), '00:00', 'PLAYLIST');

insert into add_broadcasts (id, broadcast_date, broadcast_time, add_id, broadcast_id) values (4001, PARSEDATETIME('01-02-2022','dd-MM-yyyy'), '00:40', 1001, 3001);
insert into add_broadcasts (id, broadcast_date, broadcast_time, add_id, broadcast_id) values (4002, PARSEDATETIME('01-02-2022','dd-MM-yyyy'), '01:50', 1002, 3001);

insert into song_broadcasts (id, broadcast_date, broadcast_time, song_id, broadcast_id) values (5001, PARSEDATETIME('01-02-2022','dd-MM-yyyy'), '00:00', 2001, 3001);
insert into song_broadcasts (id, broadcast_date, broadcast_time, song_id, broadcast_id) values (5002, PARSEDATETIME('01-02-2022','dd-MM-yyyy'), '01:10', 2002, 3001);