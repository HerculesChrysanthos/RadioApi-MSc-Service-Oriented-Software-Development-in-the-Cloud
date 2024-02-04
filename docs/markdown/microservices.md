# Microservices

1. [User](#User)
2. [Song](#Song)
3. [Ad](#Ad)
4. [Broadcast](#Broadcast)
5. [MultimediaBroadcast](#MultimediaBroadcast)

## Επικοινωνίες που θα γίνονται μεταξύ των Microservices

## User
- Δε θα πραγματοποιεί κλήση προς άλλα microservices. Τα άλλα microservices θα επικοινωνούν μαζί με αυτό.

## Song
### Delete process
- Από το **MultimediaBroadcast** παίρνει όλες τις μεταδόσεις του τραγουδιού(SongBroadcast) κάνοντας κλήση στο `GET /multimediaBroadcasts/songsBroadcasts?song={id}`
- Αφαιρεί το SongBroadcast απο το **Broadcast** `DELETE /broadcasts/songsBroadcasts/{songBroadcastId}`

## Ad
### Delete process
- Από το **MultimediaBroadcast** παίρνει όλες τις μεταδόσεις της διαφήμισης(AdBroadcast) κάνοντας κλήση στο `GET /multimediaBroadcasts/adsBroadcasts/{id}`
- Αφαιρεί το SongBroadcast απο το **Broadcast** `DELETE /broadcasts/adsBroadcasts/{adBroadcastId}`

## Broadcast
### Create process
- Καλεί το song για να βρει τραγούδια απο το genre `GET /songs?genre={genre}`
- Καλεί το ad για να βρει διαφημίσεις απο το time zone `GET /ads?timezone={timezone}`
- Καλεί το MultimediaBroadcast και δημιουργεί SongBroadcast `POST /multimediaBroadcasts/songsBroadcasts`
- Καλεί το MultimediaBroadcast και δημιουργεί AdBroadcast `POST /multimediaBroadcasts/adsBroadcasts`

### Delete process
- Αφαιρεί τις adBroadcast αυτού του broadcast `DELETE /multimediaBroadcasts/adsBroadcasts/{id}`
- Αφαιρεί τις songBroadcast αυτού του broadcast `DELETE /multimediaBroadcasts/songsBroadcasts/{id}`


### Suggestions process
- Καλεί το **Ad** για να πάρει διαφημίσεις για το timezone του broadcast που δόθηκε αρχικά `GET /ads?timezone={timezone}`
- Καλεί το **Song** για να πάρει τραγούδια για το genre του broadcast που δόθηκε αρχικά `GET /songs?genre={genre}`


## MultimediaBroadcast

### Ad Broadcast
#### Create process
- Καλεί το **Ad** για να βρει αντίστοιχο ad `GET /ads/{id}`
- Βρίσκει με βάση το broadcastId το αντίστοιχο **Broadcast** στο οποίο πρόκειται να προστεθεί το adBroadcast `GET /broadcasts/{id}`
- Στέλνει στο **Broadcast** ενημέρωση για το νέο adBroadcastId `PUT /broadcasts/{id}` και το request στο body περιέχει το adBroadcastToAdd πεδίο το οποίο θα ενημερώσει αντίστοιχα το array

#### Delete process
- Βρίσκει το τρέχον broadcast από το **Broadcast**
- Αφαιρεί το AdBroadcast από το broadcast `DELETE /broadcasts/{broadcastId}/adBroadcasts/{adBroadcastId}`
- Στο **Ad** στέλνει για διαγραφή του adBroadcast από τη λιστα με τα adBroadcasts `DELETE /ads/{adId}/adBroadcasts/{adBroadcastId}`

### Song Broadcast
#### Create process
- Καλεί το **Song** για να βρει αντίστοιχο song `GET /songs/{id}`
- Βρίσκει με βάση το broadcastId το αντίστοιχο **Broadcast** στο οποίο πρόκειται να προστεθεί το songBroadcast `GET /broadcasts/{id}`
- Στέλνει στο **Broadcast** ενημέρωση για το νέο songBroadcastId `PUT /broadcasts/{id}` και το request στο body περιέχει το songBroadcastToAdd πεδίο το οποίο θα ενημερώσει αντίστοιχα το array

#### Delete process
- Βρίσκει το τρέχον broadcast από το **Broadcast**
- Αφαιρεί το SongBroadcast από το broadcast `DELETE /broadcasts/{broadcastId}/songBroadcasts/{songBroadcastId}`
- Στο **Song** στέλνει για διαγραφή του songBroadcast από τη λίστα με τα songBroadcasts `DELETE /songs/{songId}/songBroadcasts/{songBroadcastId}`
