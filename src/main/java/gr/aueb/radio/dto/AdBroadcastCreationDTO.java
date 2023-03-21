package gr.aueb.radio.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class AdBroadcastCreationDTO {
    public Integer broadcastId;
    public Integer addId;

    public String startingTime;
}
