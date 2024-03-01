delete from ad_broadcasts;
delete from song_broadcasts;
delete from broadcasts;

insert into broadcasts (id, duration, starting_date, starting_time, type, user_id, genre_id) values(3001, 200, PARSEDATETIME('01-02-2022','dd-MM-yyyy'), '00:00', 'PLAYLIST', 6001, 1);
insert into broadcasts (id, duration, starting_date, starting_time, type, user_id, genre_id) values(3002, 200, PARSEDATETIME('10-02-2023','dd-MM-yyyy'), '08:00', 'PLAYLIST', 6001, 1);

insert into ad_broadcasts (id, broadcast_date, broadcast_time, ad_id, broadcast_id) values (4001, PARSEDATETIME('01-02-2022','dd-MM-yyyy'), '00:40', 1001, 3001);
insert into ad_broadcasts (id, broadcast_date, broadcast_time, ad_id, broadcast_id) values (4002, PARSEDATETIME('01-02-2022','dd-MM-yyyy'), '01:50', 1002, 3001);

insert into song_broadcasts (id, broadcast_date, broadcast_time, song_id, broadcast_id) values (5001, PARSEDATETIME('01-02-2022','dd-MM-yyyy'), '00:00', 7001, 3001);
insert into song_broadcasts (id, broadcast_date, broadcast_time, song_id, broadcast_id) values (5002, PARSEDATETIME('01-02-2022','dd-MM-yyyy'), '01:10', 7002, 3001);