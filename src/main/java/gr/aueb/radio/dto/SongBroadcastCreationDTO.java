package gr.aueb.radio.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class SongBroadcastCreationDTO {
    public Integer broadcastId;
    public Integer songId;
    public String startingTime;
}
