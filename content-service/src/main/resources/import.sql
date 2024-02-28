delete from ads;
delete from songs;
delete from genres;

insert into ads (id, duration, rep_per_zone, starting_date, ending_date, timezone) values (1001, 15, 1, PARSEDATETIME('01-01-2022','dd-MM-yyyy'), PARSEDATETIME('01-03-2022','dd-MM-yyyy'), 'LateNight');
insert into ads (id, duration, rep_per_zone, starting_date, ending_date, timezone) values (1002, 30, 2, PARSEDATETIME('01-01-2022','dd-MM-yyyy'), PARSEDATETIME('01-04-2022','dd-MM-yyyy'), 'LateNight');
insert into ads ( id, duration, rep_per_zone, starting_date, ending_date, timezone) VALUES ( 1102, 8, 3, PARSEDATETIME('07-02-2023', 'dd-MM-yyyy'), PARSEDATETIME('23-08-2024', 'dd-MM-yyyy'), 'EarlyMorning');
insert into ads ( id, duration, rep_per_zone, starting_date, ending_date, timezone) VALUES ( 1103, 6, 3, PARSEDATETIME('07-02-2023', 'dd-MM-yyyy'), PARSEDATETIME('23-08-2024', 'dd-MM-yyyy'), 'Morning');
insert into ads ( id, duration, rep_per_zone, starting_date, ending_date, timezone) VALUES ( 1104, 6, 3, PARSEDATETIME('07-02-2023', 'dd-MM-yyyy'), PARSEDATETIME('23-08-2024', 'dd-MM-yyyy'), 'Morning');
INSERT INTO ads ( id, duration, rep_per_zone, starting_date, ending_date, timezone) VALUES ( 1074, 1, 2, PARSEDATETIME('15-04-2022', 'dd-MM-yyyy'), PARSEDATETIME('21-10-2023', 'dd-MM-yyyy'), 'LateNight');
INSERT INTO ads ( id, duration, rep_per_zone, starting_date, ending_date, timezone) VALUES ( 1075, 6, 4, PARSEDATETIME('05-08-2022', 'dd-MM-yyyy'), PARSEDATETIME('03-07-2024', 'dd-MM-yyyy'), 'LateNight');
INSERT INTO ads ( id, duration, rep_per_zone, starting_date, ending_date, timezone) VALUES ( 1076, 5, 5, PARSEDATETIME('06-11-2022', 'dd-MM-yyyy'), PARSEDATETIME('29-11-2024', 'dd-MM-yyyy'), 'Afternoon');
INSERT INTO ads ( id, duration, rep_per_zone, starting_date, ending_date, timezone) VALUES ( 1077, 5, 5, PARSEDATETIME('05-07-2022', 'dd-MM-yyyy'), PARSEDATETIME('19-01-2024', 'dd-MM-yyyy'), 'Afternoon');
INSERT INTO ads ( id, duration, rep_per_zone, starting_date, ending_date, timezone) VALUES ( 1078, 6, 4, PARSEDATETIME('23-10-2022', 'dd-MM-yyyy'), PARSEDATETIME('28-07-2024', 'dd-MM-yyyy'), 'EarlyMorning');
INSERT INTO ads ( id, duration, rep_per_zone, starting_date, ending_date, timezone) VALUES ( 1079, 6, 1, PARSEDATETIME('28-06-2022', 'dd-MM-yyyy'), PARSEDATETIME('22-12-2023', 'dd-MM-yyyy'), 'EarlyMorning');
INSERT INTO ads ( id, duration, rep_per_zone, starting_date, ending_date, timezone) VALUES ( 1080, 6, 5, PARSEDATETIME('09-08-2022', 'dd-MM-yyyy'), PARSEDATETIME('26-02-2024', 'dd-MM-yyyy'), 'Morning');
INSERT INTO ads ( id, duration, rep_per_zone, starting_date, ending_date, timezone) VALUES ( 1081, 7, 5, PARSEDATETIME('16-05-2022', 'dd-MM-yyyy'), PARSEDATETIME('13-12-2024', 'dd-MM-yyyy'), 'Morning');
INSERT INTO ads ( id, duration, rep_per_zone, starting_date, ending_date, timezone) VALUES ( 1082, 3, 5, PARSEDATETIME('12-07-2022', 'dd-MM-yyyy'), PARSEDATETIME('24-06-2023', 'dd-MM-yyyy'), 'Afternoon');
INSERT INTO ads ( id, duration, rep_per_zone, starting_date, ending_date, timezone) VALUES ( 1083, 6, 1, PARSEDATETIME('20-06-2022', 'dd-MM-yyyy'), PARSEDATETIME('28-08-2023', 'dd-MM-yyyy'), 'Morning');
INSERT INTO ads ( id, duration, rep_per_zone, starting_date, ending_date, timezone) VALUES ( 1084, 9, 1, PARSEDATETIME('27-09-2022', 'dd-MM-yyyy'), PARSEDATETIME('16-12-2023', 'dd-MM-yyyy'), 'Noon');
INSERT INTO ads ( id, duration, rep_per_zone, starting_date, ending_date, timezone) VALUES ( 1085, 8, 5, PARSEDATETIME('04-10-2022', 'dd-MM-yyyy'), PARSEDATETIME('28-08-2023', 'dd-MM-yyyy'), 'Morning');
INSERT INTO ads ( id, duration, rep_per_zone, starting_date, ending_date, timezone) VALUES ( 1086, 9, 4, PARSEDATETIME('16-04-2023', 'dd-MM-yyyy'), PARSEDATETIME('01-04-2024', 'dd-MM-yyyy'), 'Morning');
INSERT INTO ads ( id, duration, rep_per_zone, starting_date, ending_date, timezone) VALUES ( 1087, 6, 4, PARSEDATETIME('11-01-2023', 'dd-MM-yyyy'), PARSEDATETIME('26-12-2024', 'dd-MM-yyyy'), 'EarlyMorning');
INSERT INTO ads ( id, duration, rep_per_zone, starting_date, ending_date, timezone) VALUES ( 1088, 5, 3, PARSEDATETIME('20-01-2023', 'dd-MM-yyyy'), PARSEDATETIME('04-10-2023', 'dd-MM-yyyy'), 'Noon');
INSERT INTO ads ( id, duration, rep_per_zone, starting_date, ending_date, timezone) VALUES ( 1089, 3, 5, PARSEDATETIME('13-02-2022', 'dd-MM-yyyy'), PARSEDATETIME('02-09-2024', 'dd-MM-yyyy'), 'PrimeTime');
INSERT INTO ads ( id, duration, rep_per_zone, starting_date, ending_date, timezone) VALUES ( 1090, 7, 3, PARSEDATETIME('26-09-2022', 'dd-MM-yyyy'), PARSEDATETIME('18-11-2024', 'dd-MM-yyyy'), 'Morning');
INSERT INTO ads ( id, duration, rep_per_zone, starting_date, ending_date, timezone) VALUES ( 1091, 9, 5, PARSEDATETIME('25-06-2022', 'dd-MM-yyyy'), PARSEDATETIME('16-11-2024', 'dd-MM-yyyy'), 'Noon');
INSERT INTO ads ( id, duration, rep_per_zone, starting_date, ending_date, timezone) VALUES ( 1092, 9, 5, PARSEDATETIME('23-03-2023', 'dd-MM-yyyy'), PARSEDATETIME('21-01-2024', 'dd-MM-yyyy'), 'Afternoon');
INSERT INTO ads ( id, duration, rep_per_zone, starting_date, ending_date, timezone) VALUES ( 1093, 7, 3, PARSEDATETIME('11-07-2022', 'dd-MM-yyyy'), PARSEDATETIME('27-05-2024', 'dd-MM-yyyy'), 'Morning');
INSERT INTO ads ( id, duration, rep_per_zone, starting_date, ending_date, timezone) VALUES ( 1094, 6, 3, PARSEDATETIME('28-12-2022', 'dd-MM-yyyy'), PARSEDATETIME('21-10-2023', 'dd-MM-yyyy'), 'Morning');
INSERT INTO ads ( id, duration, rep_per_zone, starting_date, ending_date, timezone) VALUES ( 1095, 7, 4, PARSEDATETIME('06-10-2022', 'dd-MM-yyyy'), PARSEDATETIME('01-10-2024', 'dd-MM-yyyy'), 'PrimeTime');
INSERT INTO ads ( id, duration, rep_per_zone, starting_date, ending_date, timezone) VALUES ( 1096, 7, 1, PARSEDATETIME('25-11-2022', 'dd-MM-yyyy'), PARSEDATETIME('01-10-2024', 'dd-MM-yyyy'), 'Afternoon');
INSERT INTO ads ( id, duration, rep_per_zone, starting_date, ending_date, timezone) VALUES ( 1097, 2, 5, PARSEDATETIME('17-10-2022', 'dd-MM-yyyy'), PARSEDATETIME('30-10-2024', 'dd-MM-yyyy'), 'Morning');
INSERT INTO ads ( id, duration, rep_per_zone, starting_date, ending_date, timezone) VALUES ( 1098, 8, 5, PARSEDATETIME('19-03-2022', 'dd-MM-yyyy'), PARSEDATETIME('09-08-2023', 'dd-MM-yyyy'), 'Noon');
INSERT INTO ads ( id, duration, rep_per_zone, starting_date, ending_date, timezone) VALUES ( 1099, 5, 1, PARSEDATETIME('26-02-2022', 'dd-MM-yyyy'), PARSEDATETIME('05-06-2024', 'dd-MM-yyyy'), 'PrimeTime');
INSERT INTO ads ( id, duration, rep_per_zone, starting_date, ending_date, timezone) VALUES ( 1100, 8, 4, PARSEDATETIME('19-02-2022', 'dd-MM-yyyy'), PARSEDATETIME('25-07-2024', 'dd-MM-yyyy'), 'PrimeTime');
INSERT INTO ads ( id, duration, rep_per_zone, starting_date, ending_date, timezone) VALUES ( 1101, 4, 5, PARSEDATETIME('04-12-2022', 'dd-MM-yyyy'), PARSEDATETIME('24-10-2023', 'dd-MM-yyyy'), 'Afternoon');
INSERT INTO ads ( id, duration, rep_per_zone, starting_date, ending_date, timezone) VALUES ( 1102, 8, 3, PARSEDATETIME('07-02-2023', 'dd-MM-yyyy'), PARSEDATETIME('23-08-2024', 'dd-MM-yyyy'), 'Noon');
INSERT INTO ads ( id, duration, rep_per_zone, starting_date, ending_date, timezone) VALUES ( 1103, 8, 1, PARSEDATETIME('05-06-2022', 'dd-MM-yyyy'), PARSEDATETIME('18-05-2024', 'dd-MM-yyyy'), 'EarlyMorning');
INSERT INTO ads ( id, duration, rep_per_zone, starting_date, ending_date, timezone) VALUES ( 1104, 8, 3, PARSEDATETIME('05-05-2022', 'dd-MM-yyyy'), PARSEDATETIME('26-08-2024', 'dd-MM-yyyy'), 'EarlyMorning');
INSERT INTO ads ( id, duration, rep_per_zone, starting_date, ending_date, timezone) VALUES ( 1105, 8, 1, PARSEDATETIME('19-12-2022', 'dd-MM-yyyy'), PARSEDATETIME('05-08-2024', 'dd-MM-yyyy'), 'Noon');
INSERT INTO ads ( id, duration, rep_per_zone, starting_date, ending_date, timezone) VALUES ( 1106, 1, 1, PARSEDATETIME('31-12-2022', 'dd-MM-yyyy'), PARSEDATETIME('29-07-2023', 'dd-MM-yyyy'), 'LateNight');
INSERT INTO ads ( id, duration, rep_per_zone, starting_date, ending_date, timezone) VALUES ( 1107, 8, 1, PARSEDATETIME('05-03-2022', 'dd-MM-yyyy'), PARSEDATETIME('27-05-2024', 'dd-MM-yyyy'), 'EarlyMorning');
INSERT INTO ads ( id, duration, rep_per_zone, starting_date, ending_date, timezone) VALUES ( 1108, 5, 1, PARSEDATETIME('25-04-2022', 'dd-MM-yyyy'), PARSEDATETIME('14-01-2024', 'dd-MM-yyyy'), 'LateNight');
INSERT INTO ads ( id, duration, rep_per_zone, starting_date, ending_date, timezone) VALUES ( 1109, 7, 3, PARSEDATETIME('09-05-2022', 'dd-MM-yyyy'), PARSEDATETIME('01-09-2024', 'dd-MM-yyyy'), 'PrimeTime');



insert into genres (id, title) values (1, 'Hip hop');
insert into genres (id, title) values (2, 'Rock');
insert into genres (id, title) values (3, 'Pop');
insert into genres (id, title) values (4, 'Electronic');
insert into genres (id, title) values (5, 'Classical');
insert into genres (id, title) values (6, 'Jazz');


insert into songs (id, title, artist, release_year, duration, genre_id) values(7001, 'Ride', 'Twenty One Pilots', 2015, 25, 1);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7002, 'Stressed Out', 'Twenty One Pilots', 2015, 5, 1);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7003, 'Stressed Out2', 'Twenty One Pilots2', 2015, 5, 1);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7004, 'Hotel California', 'Eagles', 1976, 6, 2);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7005, 'Imagine', 'John Lennon', 1971, 3, 2);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7006, 'Shape of You', 'Ed Sheeran', 2017, 4, 3);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7007, 'Shape of You', 'Ed Sheeran', 2017, 4, 3);
INSERT INTO songs (id, title, artist, release_year, duration, genre_id) values(7008, 'Smells Like Teen Spirit', 'Nirvana', 1991, 5, 2);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7009, 'Shape of You', 'Ed Sheeran', 2017, 4, 3);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7010, 'Take Five', 'Dave Brubeck Quartet', 1959, 5, 6);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7011, 'Canon in D', 'Johann Pachelbel', 1680, 5, 5);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7012, 'Symphony No. 9', 'Ludwig van Beethoven', 1824, 7, 5);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7013, 'Take Five', 'Dave Brubeck Quartet', 1959, 5, 6);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7014, 'So What', 'Miles Davis', 1959, 6)
insert into songs (id, title, artist, release_year, duration, genre_id) values(7015, 'Lose Yourself', 'Eminem', 2002, 5, 1);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7016, 'Juicy', 'The Notorious B.I.G.', 1994, 4, 1);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7017, 'Smells Like Teen Spirit', 'Nirvana', 1991, 5, 2);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7018, 'Bohemian Rhapsody', 'Queen', 1975, 6, 2);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7019, 'Shape of You', 'Ed Sheeran', 2017, 4, 3);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7020, 'Uptown Funk', 'Mark Ronson ft. Bruno Mars', 2014, 4, 3);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7021, 'Lose Yourself', 'Eminem', 2002, 5, 1);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7022, 'Juicy', 'The Notorious B.I.G.', 1994, 4, 1);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7023, 'Smells Like Teen Spirit', 'Nirvana', 1991, 5, 2);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7024, 'Bohemian Rhapsody', 'Queen', 1975, 6, 2);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7025, 'Shape of You', 'Ed Sheeran', 2017, 4, 3);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7026, 'Uptown Funk', 'Mark Ronson ft. Bruno Mars', 2014, 4, 3);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7027, 'Clarity', 'Zedd ft. Foxes', 2012, 4, 4);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7028, 'Titanium', 'David Guetta ft. Sia', 2011, 4, 4);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7029, 'Canon in D', 'Johann Pachelbel', 1680, 5, 5);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7030, 'Symphony No. 9', 'Ludwig van Beethoven', 1824, 7, 5);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7031, 'Take Five', 'Dave Brubeck Quartet', 1959, 5, 6);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7032, 'So What', 'Miles Davis', 1959, 9, 6);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7033, 'Titanium', 'David Guetta ft. Sia', 2011, 4, 4);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7035, 'Old Town Road', 'Lil Nas X ft. Billy Ray Cyrus', 2019, 3, 1);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7036, 'God''s Plan', 'Drake', 2018, 4, 1);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7037, 'Smells Like Teen Spirit', 'Nirvana', 1991, 5, 2);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7038, 'Bohemian Rhapsody', 'Queen', 1975, 6, 2);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7039, 'Shape of You', 'Ed Sheeran', 2017, 4, 3);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7040, 'Uptown Funk', 'Mark Ronson ft. Bruno Mars', 2014, 4, 3);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7041, 'Clarity', 'Zedd ft. Foxes', 2012, 4, 4);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7042, 'Titanium', 'David Guetta ft. Sia', 2011, 4, 4);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7043, 'Canon in D', 'Johann Pachelbel', 1680, 5, 5);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7044, 'Symphony No. 9', 'Ludwig van Beethoven', 1824, 7, 5);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7045, 'Take Five', 'Dave Brubeck Quartet', 1959, 5, 6);
insert into songs (id, title, artist, release_year, duration, genre_id) values(7046, 'So What', 'Miles Davis', 1959, 9, 6);
