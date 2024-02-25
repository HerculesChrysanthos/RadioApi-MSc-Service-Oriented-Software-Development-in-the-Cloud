package gr.aueb.radio.content.infrastructure.service.broadcast;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.MockitoAnnotations.openMocks;

class BroadcastServiceImplTest {

    @Mock
    private BroadcastApi mockBroadcastApi;

    private BroadcastServiceImpl broadcastServiceImplUnderTest;

    private AutoCloseable mockitoCloseable;

    @BeforeEach
    void setUp() {
        mockitoCloseable = openMocks(this);
        broadcastServiceImplUnderTest = new BroadcastServiceImpl();
        broadcastServiceImplUnderTest.broadcastApi = mockBroadcastApi;
    }

    @AfterEach
    void tearDown() throws Exception {
        mockitoCloseable.close();
    }

    @Test
    void testDeleteSongBroadcastsBySongId() {
        assertEquals(Optional.empty(), broadcastServiceImplUnderTest.deleteSongBroadcastsBySongId("auth", 0));
    }

    @Test
    void testDeleteAdBroadcastsByAdId() {
        assertEquals(Optional.empty(), broadcastServiceImplUnderTest.deleteAdBroadcastsByAdId("auth", 0));
    }
}
