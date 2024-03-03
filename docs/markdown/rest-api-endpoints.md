# REST API Εργασίας

## Περιεχόμενα

1. [User REST API](#user)
2. [Content REST API](#content)
3. [Broadcast REST API](#broadcast)

## User

| Method | Url         | Description                 |
|--------|-------------|-----------------------------|
| GET    | /users/{id} | Ανάκτηση user με βάση το id |
| GET    | /users/verify-auth| Authorisation         |
| POST   | /users      | Δημιουργία νέου user        |

http://localhost:8080/q/swagger/

## Content

| Method | Url                                                | Description                                     |
|--------|----------------------------------------------------|-------------------------------------------------|
| GET    | /songs/{id}                                        | Ανάκτηση song με βάση το id                     |
| GET    | /songs?artist={artist}&genreId={genreId}&genreTitle={genreTitle}&title={title}&songsIds={songsIds} | Αναζήτηση songs με βάση κάποια φίλτρα           |
| POST   | /songs                                             | Δημιουργία νέου song                            |
| DELETE | /songs/{id}                                        | Διαγραφή song με βάση το id του                 |
| PUT    | /songs/{id}                                        | Ενημέρωση στοιχείων ενος song με βάση το id του |
| GET    | /ads/{id}                                          | Ανάκτηση ad με βάση το id                       |
| GET    | /ads?timezone={timezone}&adsIds={adsIds}           | Αναζήτηση ads με βάση κάποια φίλτρα             |
| POST   | /ads                                               | Δημιουργία νέου ad                              |
| DELETE | /ads/{id}                                          | Διαγραφή ad με βάση το id του                   |
| PUT    | /ads/{id}                                          | Ενημέρωση στοιχείων ενος ad με βάση το id του   |
| GET    | /genres/{id}                                       | Ανάκτηση song με βάση το id                     |

http://localhost:8081/q/swagger/

## Broadcast

| Method | Url                                                     | Description                                                       |
|--------|---------------------------------------------------------|-------------------------------------------------------------------|
| GET    | /broadcasts/{id}                                        | Ανάκτηση broadcast με βάση το id                                  |
| GET    | /broadcasts?from={from}?to={to}?date={date}?type={type} | Αναζήτηση broadcasts με βάση κάποια φίλτρα                        |
| GET    | /broadcasts/now                                         | Ανάκτηση broadcast που μεταδίδεται τώρα                           |
| POST   | /broadcasts                                             | Δημιουργία νέου broadcast                                         |
| DELETE | /broadcasts/{id}                                        | Διαγραφή broadcasts με βάση το id του                             |
| PUT    | /broadcasts/{id}                                        | Ενημέρωση στοιχείων ενος broadcasts με βάση το id του             |
| GET    | /broadcasts/{id}/suggestions                            | Προτάσεις songs και ads για ενα broadcast με βάση το id του       |
| GET    | /broadcasts/stats-ads?date={date}                       | Ανάκτηση στατιστικών στοιχείων διαφημίσεων με βάση κάποια ημέρα   |
| GET    | /broadcasts/stats-daily?date={date}                     | Ανάκτηση στατιστικών προγράμματος  με βάση κάποια ημέρα           |
| GET    | /ad-broadcasts?date={date}&adId={adId}                  | Ανάκτηση ad_broadcast με βάση το adId ή μέρα                      |
| GET    | /ad-broadcasts/{id}                                     | Αναζήτηση ad_broadcasts με βάση το id του                         |
| POST   | /ad-broadcasts                                          | Δημιουργία νέου ad_broadcast                                      |
| DELETE | /ad-broadcasts/{id}                                     | Διαγραφή ad_broadcast με βάση το id του                           |
| DELETE | /ad-broadcasts?adId={adId}                              | Διαγραφή ad_broadcast με βάση το adId                             |
| GET    | /song-broadcasts?date={date}&songId={songId}            | Ανάκτηση song_broadcast με βάση κάποια φίλτρα                     |
| GET    | /song-broadcasts/{id}                                   | Αναζήτηση song_broadcast με το id του                             |
| POST   | /song-broadcasts                                        | Δημιουργία νέου song_broadcast                                    |
| DELETE | /song-broadcasts/{id}                                   | Διαγραφή song_broadcast με βάση το id του                         |
| DELETE | /song-broadcasts?songId={adId}                          | Διαγραφή song_broadcast με βάση το songId                         |

http://localhost:8082/q/swagger/
