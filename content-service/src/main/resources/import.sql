delete from ads;
delete from songs;

insert into ads (id, duration, rep_per_zone, starting_date, ending_date, timezone) values (1001, 15, 0, PARSEDATETIME('01-01-2022','dd-MM-yyyy'), PARSEDATETIME('01-03-2022','dd-MM-yyyy'), 'LateNight');
insert into ads (id, duration, rep_per_zone, starting_date, ending_date, timezone) values (1002, 30, 0, PARSEDATETIME('01-01-2022','dd-MM-yyyy'), PARSEDATETIME('01-04-2022','dd-MM-yyyy'), 'LateNight');
