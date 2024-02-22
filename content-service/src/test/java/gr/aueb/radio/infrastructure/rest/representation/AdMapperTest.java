//package gr.aueb.radio.infrastructure.rest.representation;
//
//import gr.aueb.radio.content.domain.ad.Ad;
//import gr.aueb.radio.content.infrastructure.rest.representation.AdMapper;
//import gr.aueb.radio.content.infrastructure.rest.representation.AdRepresentation;
//import org.junit.jupiter.api.Test;
//
//import jakarta.inject.Inject;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.mock;
//public class AdMapperTest {
//
//    @Inject
//    AdMapper adMapper;
//
//    @Test
//    public void testToModel() {
//        // Given
//        AdRepresentation representation = mock(AdRepresentation.class);
//
//        // When
//        Ad ad = adMapper.toModel(representation);
//
//        // Then
//        assertEquals(Ad.class, ad.getClass());
//        // Add more assertions as needed
//    }
//
//    @Test
//    public void testToRepresentation() {
//        // Given
//        Ad ad = mock(Ad.class);
//
//        // When
//        AdRepresentation adRepresentation = adMapper.toRepresentation(ad);
//
//        // Then
//        // Add assertions as needed
//    }
//
//    @Test
//    public void testToRepresentationList() {
//        // Given
//        List<Ad> adList = new ArrayList<>();
//        // Populate adList with mock Ad objects
//
//        // When
//        List<AdRepresentation> adRepresentationList = adMapper.toRepresentationList(adList);
//
//        // Then
//        // Add assertions as needed
//    }
//}