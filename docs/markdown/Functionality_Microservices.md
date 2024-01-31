Songs
Delete process
Από το SongBroadcast παίρνει όλες τις μεταδόσεις του τραγουδιού
Παίρνει από το broadcast τις μεταδόσεις που περιέχουν αυτό το τραγούδι
Βρισκει τo SongBroadcast που περιέχει αυτό το τραγούδι
αφαιρεί το SongBroadcast απο το broadcast
Διαγράφει το SongBroadcast

Ads
Delete process
Από το AdBroadcast παίρνει όλες τις μεταδόσεις της διαφήμισης
Παίρνει από το broadcast τις μεταδόσεις που περιέχουν αυτή τη διαφήμιση
Βρισκει το AdBroadcast που περιέχει αυτή τη διαφήμιση
αφαιρεί το AdBroadcast απο το broadcast
Διαγράφει το AdBroadcast

Broadcasts
Create
καλει το song, songBroadcast, ad & AdBroadcast
Delete
παίρνει τις adBroadcast αυτου του broadcast & τα αφαιρει
παίρνει τις songBroadcast αυτου του broadcast & τα αφαιρει
κανει delete το συγκεκριμενο id

suggestions
Καλει τα repository των songs/ ads για να βρει αντιστοιχα genres/ timezones

Ad Broadcast
Create
Καλεί το Ad repo για να βρει αντιστοιχο ad
βρισκει με βαση το broadcastId το αντιστοιχο broadcast

Delete
βρισκει το τρεχον broadcast
αφαιρει το adBroadcast απο το broadcast

Song Broadcast
create
Καλεί το song repo για να βρει αντιστοιχο song
βρισκει με βαση το broadcastId το αντιστοιχο broadcast

Delete
βρισκει το τρεχον broadcast
αφαιρει το SongBroadcast απο το broadcast
