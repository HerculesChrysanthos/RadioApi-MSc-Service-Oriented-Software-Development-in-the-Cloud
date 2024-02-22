package gr.aueb.radio.broadcast.application;

import gr.aueb.radio.broadcast.infrastructure.service.content.representation.AdBasicRepresentation;

public interface ContentService {

    AdBasicRepresentation getAd(String auth, Integer adId);
}
