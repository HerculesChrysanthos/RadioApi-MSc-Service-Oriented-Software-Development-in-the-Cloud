package gr.aueb.radio.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SongBroadcastCreationDTO {
    public Integer broadcastId;
    public Integer songId;
    public String startingTime;
}
