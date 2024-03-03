# Microservices

1. [User](#User)
2. [Content](#Content)
3. [Broadcast](#Broadcast)


## User
- Δε θα πραγματοποιεί κλήση προς άλλα microservices. Τα άλλα microservices θα επικοινωνούν μαζί με αυτό για authorisation / authentication σκοπούς.

## Content
#### Delete process
- `DELETE /song-broadcasts?songId={songId}` Αφαιρεί το SongBroadcast απο το **Broadcast** 
- `DELETE /ad-broadcasts?adId={adId}` Αφαιρεί το SongBroadcast απο το **Broadcast**
#### Update process 
- `GET /song-broadcasts?songId={songId}` Λαμβάνει τις μεταδόσεις ενός τραγουδιού απο το **Broadcast**
- `GET /ad-broadcasts?adId={adId}` Λαμβάνει τις μεταδόσεις μιας διαφήμισης απο το **Broadcast**
#### Authorisation
- `GET/users/verify-auth` **User**


## Broadcast
#### Create process
- `GET /songs?artist={artist}&genreId={genreId}&genreTitle={genreTitle}&title={title}&songsIds={songsIds}` Καλεί το content για να βρει τραγούδια βάσει φίλτρων (πχ. genre) 
- `GET /ads?timezone={timezone}&adsIds={adsIds}` Καλεί το content για να βρει διαφημίσεις απο το time zone ή για να λάβει τις διαφημίσεις για συγκεκριμένα ids 

#### Update process
- `GET /genre/{id}` Καλεί το **Content** για να ελέγξει πως το είδος υπάρχει.

#### Suggestions process
- `GET /ads?timezone={timezone}&adsIds={adsIds}` Καλεί το **Content** για να πάρει διαφημίσεις για το timezone του broadcast που δόθηκε αρχικά 
- `GET /songs?artist={artist}&genreId={genreId}&genreTitle={genreTitle}&title={title}&songsIds={songsIds}` Καλεί το **Content** για να πάρει τραγούδια για το genre του broadcast που δόθηκε αρχικά. 

#### Statistics process
- `GET /ads?timezone={timezone}&adsIds={adsIds}` Καλεί το content για να βρει διαφημίσεις απο το time zone ή για να λάβει τις διαφημίσεις για συγκεκριμένα ids κατά την εξαγωγή στατιστικών.

#### Create AdBroadcast process
- `GET /ads/{id}` Καλεί το **Content** για να βρει αντίστοιχο ad κατά τη δημιουργία μετάδοσης.
- `GET /ads?timezone={timezone}&adsIds={adsIds}` Καλεί το **content** για να λάβει τις διαφημίσεις για τα συγκεκριμένα adIds που ήδη έχουν μεταδοθεί στην εκπομπή. 

#### Create SongBroadcast process
- `GET /songs/{id}` Καλεί το **Content** για να βρει αντίστοιχο song κατά τη δημιουργία μετάδοσης.
- `GET /songs?artist={artist}&genreId={genreId}&genreTitle={genreTitle}&title={title}&songsIds={songsIds}` Καλεί το **content** για να λάβει τα τραγούδια για τα συγκεκριμένα songIds που ήδη έχουν μεταδοθεί στην εκπομπή. 
  
#### Authorisation
- `GET/users/verify-auth` **User**
