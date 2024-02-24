package gr.aueb.radio.content.infrastructure.rest.representation;

import gr.aueb.radio.content.domain.song.Song;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

class SongMapperTest {

    private SongMapper songMapperUnderTest;

    @BeforeEach
    void setUp() {
        songMapperUnderTest = new SongMapper() {
            @Override
            public SongRepresentation toRepresentation(Song song) {
                return null;
            }

            @Override
            public Song toModel(SongRepresentation representation) {
                return null;
            }

            @Override
            public List<SongRepresentation> toRepresentationList(List<Song> songs) {
                return null;
            }
        };
    }
}
