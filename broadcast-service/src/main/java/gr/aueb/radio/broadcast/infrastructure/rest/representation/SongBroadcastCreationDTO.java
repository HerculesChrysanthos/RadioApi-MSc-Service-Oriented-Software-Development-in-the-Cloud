package gr.aueb.radio.broadcast.infrastructure.rest.representation;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class SongBroadcastCreationDTO {
    public Integer broadcastId;
    public Integer songId;
    public String startingTime;
}
