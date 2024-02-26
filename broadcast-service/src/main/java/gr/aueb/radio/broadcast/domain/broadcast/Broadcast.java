package gr.aueb.radio.broadcast.domain.broadcast;

import gr.aueb.radio.broadcast.common.DateUtil;
import gr.aueb.radio.broadcast.domain.adBroadcast.AdBroadcast;
import gr.aueb.radio.broadcast.domain.songBroadcast.SongBroadcast;
import gr.aueb.radio.broadcast.infrastructure.service.content.representation.AdBasicRepresentation;
import gr.aueb.radio.broadcast.infrastructure.service.content.representation.SongBasicRepresentation;
import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jaxb.core.v2.TODO;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "broadcasts")
@Slf4j
public class Broadcast {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Column(name = "starting_date", nullable = false)
    private LocalDate startingDate;

    @Column(name = "starting_time", nullable = false)
    private LocalTime startingTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private BroadcastType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "timezone")
    private Zone timezone;

    @OneToMany(mappedBy = "broadcast", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<AdBroadcast> adBroadcasts = new ArrayList<>();

    @OneToMany(mappedBy = "broadcast", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<SongBroadcast> songBroadcasts = new ArrayList<>();

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "genre_id")
    private Integer genreId;

    public Broadcast() {
    }


    public Broadcast(Integer duration, LocalDate startingDate, LocalTime startingTime, BroadcastType type) {
        this.duration = duration;
        this.startingDate = startingDate;
        this.startingTime = startingTime;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public LocalDate getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(LocalDate startingDate) {
        this.startingDate = startingDate;
    }

    public LocalTime getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(LocalTime startingTime) {
        this.startingTime = startingTime;
        this.timezone = DateUtil.calculateTimezone(this.startingTime);
    }

    public BroadcastType getType() {
        return type;
    }

    public void setType(BroadcastType type) {
        this.type = type;
    }

    public List<AdBroadcast> getAdBroadcasts() {
        return this.adBroadcasts;
    }

    public List<SongBroadcast> getSongBroadcasts() {
        return this.songBroadcasts;
    }

    public Zone getTimezone() {
        return DateUtil.calculateTimezone(this.startingTime);
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getGenreId() {
        return genreId;
    }

    public void setGenreId(Integer genreId) {
        this.genreId = genreId;
    }

    public AdBroadcast createAdBroadcast
            (AdBasicRepresentation ad,
             LocalTime time,
             List<AdBroadcast> adBroadcastsOfDay,
             List<AdBasicRepresentation> broadcastAds
            ) {
        if (!adCanBeAdded(ad, time, adBroadcastsOfDay, broadcastAds)) {
            System.out.println("adCanBeAdded " + "mpika");

            return null;
        }
        if (checkForOccurrence(time, ad.duration)) {
            System.out.println("checkForOccurrence " + "mpika");
//            log.info("Broadcast occurrence restriction");
            return null;
        }
        AdBroadcast adBroadcast = new AdBroadcast(this.startingDate, time);
        adBroadcast.setBroadcast(this);
        // edo theloyme na mpainei i metadosi pano sti diafimisi? mas xreiazetai?
        //ad.addBroadcastAd(adBroadcast);
        this.adBroadcasts.add(adBroadcast);
        return adBroadcast;
    }

    public SongBroadcast createSongBroadcast(
            SongBasicRepresentation song,
            LocalTime time,
            List songBroadcastsOfDay,
            List<SongBasicRepresentation> broadcastSongs
    ) {
        if (!songCanBeAdded(song, time, songBroadcastsOfDay, broadcastSongs)) {
            return null;
        }
        if (checkForOccurrence(time, song.duration)) {
//            log.info("Broadcast occurrence restriction");
            return null;
        }
        SongBroadcast songBroadcast = new SongBroadcast(this.startingDate, time, song.id);
        songBroadcast.setBroadcast(this);
//        song.addSongBroadcast(songBroadcast);
        this.songBroadcasts.add(songBroadcast);
        return songBroadcast;
    }

    public void removeAdBroadcast(AdBroadcast adBroadcast) {
        this.adBroadcasts.remove(adBroadcast);
        adBroadcast.setBroadcast(null);
        // to decide whether to keep a list of all adBroadcasts on ad - better not due to traffic
//        adBroadcast.getAd().removeAdBroadcast(adBroadcast);
    }

    public void removeSongBroadcast(SongBroadcast songBroadcast) {
        this.songBroadcasts.remove(songBroadcast);
        songBroadcast.setBroadcast(null);
        // to decide whether to keep a list of all aongBroadcasts on song - better not due to traffic
//        songBroadcast.getSong().removeSongBroadcast(songBroadcast);
    }

    private boolean checkForOccurrence(LocalTime startingTime, Integer duration) {
        // Starting time of song/add broadcast
        LocalDateTime startingDateTime = this.startingDate.atTime(startingTime);
        // Ending time of song/add broadcast
        LocalDateTime endingDateTime = startingDateTime.plusMinutes(duration);
        for (SongBroadcast sb : this.songBroadcasts) {
            LocalDateTime sbStartingTime = this.startingDate.atTime(sb.getBroadcastTime());
            LocalDateTime sbEndingTime = sb.getBroadcastEndingDateTime(duration);
            if (DateUtil.between(sbStartingTime, startingDateTime, sbEndingTime)) {
                return true;
            }

            if (DateUtil.between(sbStartingTime, endingDateTime, sbEndingTime)) {
                return true;
            }
        }

        for (AdBroadcast ab : this.adBroadcasts) {
            LocalDateTime abStartingTime = this.startingDate.atTime(ab.getBroadcastTime());
            LocalDateTime abEndingTime = ab.getBroadcastEndingDateTime(duration);
            if (DateUtil.between(abStartingTime, startingDateTime, abEndingTime)) {
                return true;
            }

            if (DateUtil.between(abStartingTime, endingDateTime, abEndingTime)) {
                return true;
            }
        }
        return false;
    }

//    private boolean validSongGenre(String genre){
//        if(this.songBroadcasts.size() == 0){
//            return true;
//        }
//        SongBroadcast sb = this.songBroadcasts.get(0);
//        if (sb.getSong().getGenre().equals(genre)){
//            return true;
//        }
//        return false;
//    }

    private boolean exceedsLimits(LocalTime startingTime, Integer duration) {
        // Starting time of song/add broadcast
        LocalDateTime startingDateTime = this.startingDate.atTime(startingTime);
        // Ending time of song/add broadcast
        LocalDateTime endingDateTime = startingDateTime.plusMinutes(duration);

        LocalDateTime broadcastStartingTime = this.startingDate.atTime(this.startingTime);
        LocalDateTime broadcastEndingTime = broadcastStartingTime.plusMinutes(this.duration);
        // check that the starting time of a (add|song)broadcast is before or after the starting time of the broadcast itself
        if (!DateUtil.betweenCloseOpen(broadcastStartingTime, startingDateTime, broadcastEndingTime)) {
            return true;
        }
        // check that the ending time of a (add|song)broadcast is before or after the starting time of the broadcast itself
        if (!DateUtil.betweenOpenClose(startingDateTime, endingDateTime, broadcastEndingTime)) {
            return true;
        }
        return false;
    }

    public LocalDateTime getBroadcastEndingDateTime() {
        LocalDateTime startingDate = this.startingDate.atTime(this.startingTime);
        return startingDate.plusMinutes(this.duration);
    }

    public Integer getAllocatedTime(List<AdBasicRepresentation> ads, List<SongBasicRepresentation> songs) {
        Integer totalTime = 0;
        if (ads != null) {
            for (AdBasicRepresentation ad : ads) {
                totalTime += ad.duration;
            }
        }
        if (songs != null) {
            for (SongBasicRepresentation song : songs) {
                totalTime += song.duration;
            }
        }

        return totalTime;
    }

    public boolean songCanBeAdded(
            SongBasicRepresentation song,
            LocalTime time,
            List songBroadcastsOfDay,
            List<SongBasicRepresentation> broadcastSongs
    ) {
        if (!toBeBroadcastedSong(this.startingDate, time, songBroadcastsOfDay)) {
//            log.info("Broadcast song broadcast restriction");
            return false;
        }

//        if(!validSongGenre(song.getGenre())){
//            log.info("Inconsistent song genre");
//            return false;
//        }

        if (getAllocatedTime(null, broadcastSongs) + song.duration > this.duration) {
//            log.info("Broadcast duration restriction");
            return false;
        }
        if (this.exceedsLimits(time, song.duration)) {
//            log.info("Broadcast limit restriction");
            return false;
        }
        return true;
    }


    public boolean adCanBeAdded(
            AdBasicRepresentation ad,
            LocalTime time,
            List<AdBroadcast> adBroadcastsOfDay,
            List<AdBasicRepresentation> broadcastAds) {
        System.out.println("adId " + ad.id);
        System.out.println("adId " + ad.timezone);
        this.timezone = DateUtil.calculateTimezone(this.startingTime);
        String broadcastTimezone = this.timezone.name();

        if (!ad.timezone.equals(broadcastTimezone)) {
//            log.info("Broadcast timezone restriction");
            return false;
        }

        // Replace with the above - The != operator compares the references of the string objects, not their content.
//        if(ad.timezone != broadcastTimezone){
//            System.out.println ("mpika");
//            log.info("Broadcast timezone restriction");
//            return false;
//        }
//        if(!ad.toBeBroadcasted(this.startingDate)){
//            log.info("Add restrictions");
//            return false;
//        }
        if (!toBeBroadcastedAd(this.startingDate, ad, adBroadcastsOfDay)) {
//            log.info("Add restrictions");
            return false;
        }


        if (getAllocatedTime(broadcastAds, null) + ad.duration > this.duration) {
            return false;
        }
        if (this.exceedsLimits(time, ad.duration)) {
//            log.info("Broadcast limit restriction");
            return false;
        }
        return true;
    }

    public boolean toBeBroadcastedAd(LocalDate date, AdBasicRepresentation ad, List<AdBroadcast> adBroadcastsOfDay) {
        if (!DateUtil.between(ad.startingDate, date, ad.endingDate)) {
            System.out.println("startingDate" + ad.startingDate);
            System.out.println("endingDate" + ad.endingDate);
            System.out.println("date" + date);
            return false;
        }
        return adBroadcastsOfDay.size() < ad.repPerZone;
    }

    public boolean toBeBroadcastedSong(LocalDate date, LocalTime time, List broadcastsOfDay) {

        // Filter from list and extract broadcasts from date
//        List<SongBroadcast> broadcastsOfDay = getBroadcastsOfDay(date);
        if (broadcastsOfDay.size() == 4) {
            return false;
        }
        LocalDateTime dateTime = date.atTime(time);
        // Check for occurrence in last hour
//        if (checkForPrevOccurrence(broadcastsOfDay, dateTime) || checkForNextOccurrence(broadcastsOfDay, dateTime)){
//            return false;
//        }
        return true;
    }
}

