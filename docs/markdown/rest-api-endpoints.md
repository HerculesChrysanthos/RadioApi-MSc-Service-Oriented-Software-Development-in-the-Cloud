# REST API Εργασίας

## Περιεχόμενα

1. [User REST API](#user)
2. [Song REST API](#song)
3. [Ad REST API](#ad)
4. [Broadcast REST API](#broadcast)
5. [Ad Broadcast REST API](#ad-broadcast)
6. [Song Broadcast REST API](#song-broadcast)
7. [Statistics REST API](#statistics)

## User

| Method | Url         | Description                 |
|--------|-------------|-----------------------------|
| GET    | /users/{id} | Ανάκτηση user με βάση το id |
| POST   | /users      | Δημιουργία νέου user        |

## Song

| Method | Url                                                | Description                                     |
|--------|----------------------------------------------------|-------------------------------------------------|
| GET    | /songs/{id}                                        | Ανάκτηση song με βάση το id                     |
| GET    | /songs?artist={artist}?genre={genre}?title={title} | Αναζήτηση songs με βάση κάποια φίλτρα           |
| POST   | /songs                                             | Δημιουργία νέου song                            |
| DELETE | /songs/{id}                                        | Διαγραφή song με βάση το id του                 |
| PUT    | /songs/{id}                                        | Ενημέρωση στοιχείων ενος song με βάση το id του |

## Ad

| Method | Url                      | Description                                   |
|--------|--------------------------|-----------------------------------------------|
| GET    | /ads/{id}                | Ανάκτηση ad με βάση το id                     |
| GET    | /ads?timezone={timezone} | Αναζήτηση ads με βάση κάποια φίλτρα           |
| POST   | /ads                     | Δημιουργία νέου ad                            |
| DELETE | /ads/{id}                | Διαγραφή ad με βάση το id του                 |
| PUT    | /ads/{id}                | Ενημέρωση στοιχείων ενος ad με βάση το id του |

## Broadcast

| Method | Url                                                     | Description                                                 |
|--------|---------------------------------------------------------|-------------------------------------------------------------|
| GET    | /broadcasts/{id}                                        | Ανάκτηση broadcast με βάση το id                            |
| GET    | /broadcasts?from={from}?to={to}?date={date}?type={type} | Αναζήτηση broadcasts με βάση κάποια φίλτρα                  |
| POST   | /broadcasts                                             | Δημιουργία νέου broadcast                                   |
| DELETE | /broadcasts/{id}                                        | Διαγραφή broadcasts με βάση το id του                       |
| PUT    | /broadcasts/{id}                                        | Ενημέρωση στοιχείων ενος broadcasts με βάση το id του       |
| GET    | /broadcasts/{id}/suggestions                            | Προτάσεις songs και ads για ενα broadcast με βάση το id του |

## Ad Broadcast

| Method | Url                        | Description                                   |
|--------|----------------------------|-----------------------------------------------|
| GET    | /ad_broadcasts/{id}        | Ανάκτηση ad_broadcast με βάση το id           |
| GET    | /ad_broadcasts?date={date} | Αναζήτηση ad_broadcasts με βάση κάποια φίλτρα |
| POST   | /ad_broadcasts             | Δημιουργία νέου ad_broadcast                  |
| DELETE | /ad_broadcasts/{id}        | Διαγραφή ad_broadcast με βάση το id του       |

## Song Broadcast

| Method | Url                          | Description                                    |
|--------|------------------------------|------------------------------------------------|
| GET    | /song_broadcasts/{id}        | Ανάκτηση song_broadcast με βάση το id          |
| GET    | /song_broadcasts?date={date} | Αναζήτηση song_broadcast με βάση κάποια φίλτρα |
| POST   | /song_broadcasts             | Δημιουργία νέου song_broadcast                 |
| DELETE | /song_broadcasts/{id}        | Διαγραφή song_broadcast με βάση το id του      |

## Statistics

| Method | Url                        | Description                                             |
|--------|----------------------------|---------------------------------------------------------|
| GET    | /stats/program?date={date} | Ανάκτηση προγράμματος μεταδόσεων με βάση κάποια ημέρα   |
| GET    | /stats/ads?date={date}     | Ανάκτηση στατιστικών στοιχείων ads με βάση κάποια ημέρα |