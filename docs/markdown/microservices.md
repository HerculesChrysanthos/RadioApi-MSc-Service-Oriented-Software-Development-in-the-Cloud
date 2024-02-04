# Microservices

1. [User](#user)
2. [Song](#song)
3. [Ad](#ad)
4. [Broadcast](#broadcast)
5. [MultimediaBroadcast](#multimediabroadcast)

## Interactions between Microservices

### User
- Will not make calls to other microservices. Other microservices will communicate with it.

### Song
#### Delete Process
- Calls **MultimediaBroadcast** to get all broadcasts of the song (`GET /multimediaBroadcasts/songsBroadcasts?song={id}`).
- Removes the SongBroadcast from **Broadcast** (`DELETE /broadcasts/songsBroadcasts/{songBroadcastId}`).

### Ad
#### Delete Process
- Calls **MultimediaBroadcast** to get all broadcasts of the ad (`GET /multimediaBroadcasts/adsBroadcasts/{id}`).
- Removes the AdBroadcast from **Broadcast** (`DELETE /broadcasts/adsBroadcasts/{adBroadcastId}`).

### Broadcast
#### Create Process
- Calls **Song** to find songs by genre (`GET /songs?genre={genre}`).
- Calls **Ad** to find ads by time zone (`GET /ads?timezone={timezone}`).
- Calls **MultimediaBroadcast** and creates SongBroadcast (`POST /multimediaBroadcasts/songsBroadcasts`).
- Calls **MultimediaBroadcast** and creates AdBroadcast (`POST /multimediaBroadcasts/adsBroadcasts`).

#### Delete Process
- Removes adBroadcasts of this broadcast (`DELETE /multimediaBroadcasts/adsBroadcasts/{id}`).
- Removes songBroadcasts of this broadcast (`DELETE /multimediaBroadcasts/songsBroadcasts/{id}`).

#### Suggestions Process
- Calls **Ad** to get ads for the timezone of the initial broadcast (`GET /ads?timezone={timezone}`).
- Calls **Song** to get songs for the genre of the initial broadcast (`GET /songs?genre={genre}`).

### MultimediaBroadcast

#### Ad Broadcast
##### Create Process
- Calls **Ad** to find the corresponding ad (`GET /ads/{id}`).
- Finds the corresponding **Broadcast** based on the broadcastId (`GET /broadcasts/{id}`).
- Sends an update to **Broadcast** for the new adBroadcastId (`PUT /broadcasts/{id}`), with the adBroadcastToAdd field to update the array.

##### Delete Process
- Finds the current broadcast from **Broadcast**.
- Removes the AdBroadcast from the broadcast (`DELETE /broadcasts/{broadcastId}/adBroadcasts/{adBroadcastId}`).
- Sends a deletion request to **Ad** for the adBroadcast (`DELETE /ads/{adId}/adBroadcasts/{adBroadcastId}`).

#### Song Broadcast
##### Create Process
- Calls **Song** to find the corresponding song (`GET /songs/{id}`).
- Finds the corresponding **Broadcast** based on the broadcastId (`GET /broadcasts/{id}`).
- Sends an update to **Broadcast** for the new songBroadcastId (`PUT /broadcasts/{id}`), with the songBroadcastToAdd field to update the array.

##### Delete Process
- Finds the current broadcast from **Broadcast**.
- Removes the SongBroadcast from the broadcast (`DELETE /broadcasts/{broadcastId}/songBroadcasts/{songBroadcastId}`).
- Sends a deletion request to **Song** for the songBroadcast (`DELETE /songs/{songId}/songBroadcasts/{songBroadcastId}`).
