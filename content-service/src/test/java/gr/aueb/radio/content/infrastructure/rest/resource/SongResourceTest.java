package gr.aueb.radio.content.infrastructure.rest.resource;

import gr.aueb.radio.content.application.SongService;
import gr.aueb.radio.content.domain.song.Song;
import gr.aueb.radio.content.infrastructure.rest.representation.SongInputDTO;
import gr.aueb.radio.content.infrastructure.rest.representation.SongRepresentation;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public final class SongResourceTest {

    public void testCreate_errors() {


    }

    /**
     * @utbot.classUnderTest {@link SongResource}
     * @utbot.methodUnderTest {@link SongResource#delete(Integer, String)}
     * @utbot.invokes {@link SongService#delete(Integer, String)}
     * @utbot.invokes {@link Response#noContent()}
     * @utbot.invokes {@link ResponseBuilder#build()}
     * @utbot.returnsFrom {@code return Response.noContent().build();}
     */
    @Test
    @DisplayName("delete: SongServiceDelete -> return Response.noContent().build()")
    public void testDelete_ResponseBuild() {
        org.mockito.MockedStatic mockedStatic = null;
        try {
            mockedStatic = mockStatic(Response.class);
            ResponseBuilder responseBuilderMock = mock(ResponseBuilder.class);
            (when(responseBuilderMock.build())).thenReturn(((Response) null));
            (mockedStatic.when(Response::noContent)).thenReturn(responseBuilderMock);
            SongResource songResource = new SongResource();
            SongService songServiceMock = mock(SongService.class);
            songResource.songService = songServiceMock;

            Response actual = songResource.delete(null, null);

            assertNull(actual);
        } finally {
            mockedStatic.close();
        }
    }


    /**
     * @utbot.classUnderTest {@link SongResource}
     * @utbot.methodUnderTest {@link SongResource#delete(Integer, String)}
     * @utbot.invokes {@link SongService#delete(Integer, String)}
     * @utbot.invokes {@link Response#noContent()}
     * @utbot.invokes {@link ResponseBuilder#build()}
     * @utbot.throwsException {@link NullPointerException} in: return Response.noContent().build();
     */
    @Test
    @DisplayName("delete: return Response.noContent().build() : True -> ThrowNullPointerException")
    public void testDelete_ResponseBuild_1() {
        org.mockito.MockedStatic mockedStatic = null;
        try {
            mockedStatic = mockStatic(Response.class);
            (mockedStatic.when(Response::noContent)).thenReturn(((ResponseBuilder) null));
            SongResource songResource = new SongResource();
            SongService songServiceMock = mock(SongService.class);
            songResource.songService = songServiceMock;


            songResource.delete(null, null);
        } finally {
            mockedStatic.close();
        }
    }


    /**
     * @utbot.classUnderTest {@link SongResource}
     * @utbot.methodUnderTest {@link SongResource#delete(Integer, String)}
     */
    @Test
    @DisplayName("delete: id = zero, auth = '\n\t\r' -> throw NullPointerException")
    public void testDeleteThrowsNPEWithCornerCaseAndBlankString() {
        SongResource songResource = new SongResource();
        

        songResource.delete(0, "\n\t\r");
    }

    /**
     * @utbot.classUnderTest {@link SongResource}
     * @utbot.methodUnderTest {@link SongResource#delete(Integer, String)}
     */
    @Test
    @DisplayName("delete: id = min, auth = '\n\t\r' -> throw NullPointerException")
    public void testDeleteThrowsNPEWithCornerCaseAndBlankString1() {
        SongResource songResource = new SongResource();
        SongService songService = new SongService();
        songResource.songService = songService;
        

        songResource.delete(Integer.MIN_VALUE, "\n\t\r");
    }


    /**
     * @utbot.classUnderTest {@link SongResource}
     * @utbot.methodUnderTest {@link SongResource#getSong(Integer, String)}
     * @utbot.invokes {@link SongService#findSong(Integer, String)}
     * @utbot.invokes {@link Response#ok()}
     * @utbot.invokes {@link ResponseBuilder#entity(Object)}
     * @utbot.invokes {@link ResponseBuilder#build()}
     * @utbot.returnsFrom {@code return Response.ok().entity(found).build();}
     */
    @Test
    @DisplayName("getSong: SongServiceFindSong -> return Response.ok().entity(found).build()")
    public void testGetSong_ResponseBuild() {
        org.mockito.MockedStatic mockedStatic = null;
        try {
            mockedStatic = mockStatic(Response.class);
            ResponseBuilder responseBuilderMock = mock(ResponseBuilder.class);
            ResponseBuilder responseBuilderMock1 = mock(ResponseBuilder.class);
            (when(responseBuilderMock1.build())).thenReturn(((Response) null));
            (when(responseBuilderMock.entity(any()))).thenReturn(responseBuilderMock1);
            (mockedStatic.when(Response::ok)).thenReturn(responseBuilderMock);
            SongResource songResource = new SongResource();
            SongService songServiceMock = mock(SongService.class);
            (when(songServiceMock.findSong(any(), any()))).thenReturn(((SongRepresentation) null));
            songResource.songService = songServiceMock;

            Response actualResponse = (Response) songResource.getSong(null, null);

            assertNull(actualResponse);
        } finally {
            mockedStatic.close();
        }
    }


    /**
     * @utbot.classUnderTest {@link SongResource}
     * @utbot.methodUnderTest {@link SongResource#getSong(Integer, String)}
     * @utbot.throwsException {@link NullPointerException} in: return Response.ok().entity(found).build();
     */
    @Test
    @DisplayName("getSong: return Response.ok().entity(found).build() : True -> ThrowNullPointerException")
    public void testGetSong_ThrowNullPointerException() {
        org.mockito.MockedStatic mockedStatic = null;
        try {
            mockedStatic = mockStatic(Response.class);
            (mockedStatic.when(Response::ok)).thenReturn(((ResponseBuilder) null));
            SongResource songResource = new SongResource();
            SongService songServiceMock = mock(SongService.class);
            (when(songServiceMock.findSong(any(), any()))).thenReturn(((SongRepresentation) null));
            songResource.songService = songServiceMock;

            /* This test fails because method [gr.aueb.radio.content.infrastructure.rest.resource.SongResource.getSong] produces [java.lang.NullPointerException] */
            songResource.getSong(null, null);
        } finally {
            mockedStatic.close();
        }
    }

    /**
     * @utbot.classUnderTest {@link SongResource}
     * @utbot.methodUnderTest {@link SongResource#getSong(Integer, String)}
     * @utbot.invokes {@link ResponseBuilder#build()}
     * @utbot.throwsException {@link NullPointerException} in: return Response.ok().entity(found).build();
     */
    @Test
    @DisplayName("getSong: return Response.ok().entity(found).build() : True -> ThrowNullPointerException")
    public void testGetSong_ResponseBuild_1() {
        org.mockito.MockedStatic mockedStatic = null;
        try {
            mockedStatic = mockStatic(Response.class);
            ResponseBuilder responseBuilderMock = mock(ResponseBuilder.class);
            (when(responseBuilderMock.entity(any()))).thenReturn(((ResponseBuilder) null));
            (mockedStatic.when(Response::ok)).thenReturn(responseBuilderMock);
            SongResource songResource = new SongResource();
            SongService songServiceMock = mock(SongService.class);
            (when(songServiceMock.findSong(any(), any()))).thenReturn(((SongRepresentation) null));
            songResource.songService = songServiceMock;

            songResource.getSong(null, null);
        } finally {
            mockedStatic.close();
        }
    }


    /**
     * @utbot.classUnderTest {@link SongResource}
     * @utbot.methodUnderTest {@link SongResource#getSong(Integer, String)}
     */
    @Test
    @DisplayName("getSong: id = zero, auth = '\n\t\r' -> throw NullPointerException")
    public void testGetSongThrowsNPEWithCornerCaseAndBlankString() {
        SongResource songResource = new SongResource();
        

        songResource.getSong(0, "\n\t\r");
    }

    /**
     * @utbot.classUnderTest {@link SongResource}
     * @utbot.methodUnderTest {@link SongResource#getSong(Integer, String)}
     */
    @Test
    @DisplayName("getSong: id = min, auth = '\n\t\r' -> throw NullPointerException")
    public void testGetSongThrowsNPEWithCornerCaseAndBlankString1() {
        SongResource songResource = new SongResource();
        SongService songService = new SongService();
        songResource.songService = songService;

        songResource.getSong(Integer.MIN_VALUE, "\n\t\r");
    }

    /**
     * @utbot.classUnderTest {@link SongResource}
     * @utbot.methodUnderTest {@link SongResource#search(String, String, String, String, String)}
     * @utbot.invokes {@link String#split(String)}
     * @utbot.throwsException {@link NullPointerException} in: List<Integer> convertedSongsId = Arrays.stream(songsIds.split(",")).map(Integer::parseInt).collect(Collectors.toList());
     */
    @Test
    @DisplayName("search: convertedSongsId = Arrays.stream(songsIds.split(\",\")).map(Integer::parseInt).collect(Collectors.toList()) : True -> ThrowNullPointerException")
    public void testSearch_StringSplit() {
        SongResource songResource = new SongResource();
        
        /* This test fails because method [gr.aueb.radio.content.infrastructure.rest.resource.SongResource.search] produces [java.lang.NullPointerException]
            gr.aueb.radio.content.infrastructure.rest.resource.SongResource.search(SongResource.java:60) */
        songResource.search(null, null, null, null, null);
    }
    ///endregion

    ///region FUZZER: ERROR SUITE for method search(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)

    @Test
    public void testSearchByFuzzer() {
        SongResource songResource = new SongResource();
        
        /* This test fails because method [gr.aueb.radio.content.infrastructure.rest.resource.SongResource.search] produces [java.lang.NullPointerException]
            gr.aueb.radio.content.infrastructure.rest.resource.SongResource.search(SongResource.java:63) */
        songResource.search("", "", "", ",", "abc");
    }

    @Test
    public void testSearchByFuzzer1() {
        SongResource songResource = new SongResource();
        SongService songService = new SongService();
        songResource.songService = songService;
        
        /* This test fails because method [gr.aueb.radio.content.infrastructure.rest.resource.SongResource.search] produces [java.lang.NullPointerException]
            gr.aueb.radio.content.application.SongService.search(SongService.java:41)
            gr.aueb.radio.content.infrastructure.rest.resource.SongResource.search(SongResource.java:63) */
        songResource.search(",", ",", "abc", ",", "\n\t\r");
    }
    ///endregion

    ///region OTHER: ERROR SUITE for method search(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)

    @Test
    public void testSearch1() {
        SongResource songResource = new SongResource();
        String songsIds = "";
        
        /* This test fails because method [gr.aueb.radio.content.infrastructure.rest.resource.SongResource.search] produces [java.lang.NumberFormatException: For input string: ""]
            java.base/java.lang.NumberFormatException.forInputString(NumberFormatException.java:65)
            java.base/java.lang.Integer.parseInt(Integer.java:662)
            java.base/java.lang.Integer.parseInt(Integer.java:770)
            java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
            java.base/java.util.Spliterators$ArraySpliterator.forEachRemaining(Spliterators.java:948)
            java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:484)
            java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
            java.base/java.util.stream.ReduceOps$ReduceOp.evaluateSequential(ReduceOps.java:913)
            java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
            java.base/java.util.stream.ReferencePipeline.collect(ReferencePipeline.java:578)
            gr.aueb.radio.content.infrastructure.rest.resource.SongResource.search(SongResource.java:62) */
        songResource.search(null, null, null, songsIds, null);
    }
    ///endregion

    ///endregion

    ///region Test suites for executable gr.aueb.radio.content.infrastructure.rest.resource.SongResource.update

    ///region SYMBOLIC EXECUTION: SUCCESSFUL EXECUTIONS for method update(java.lang.Integer, gr.aueb.radio.content.infrastructure.rest.representation.SongInputDTO, java.lang.String)

    /**
     * @utbot.classUnderTest {@link SongResource}
     * @utbot.methodUnderTest {@link SongResource#update(Integer, SongInputDTO, String)}
     * @utbot.invokes {@link SongInputDTO#toRepresentation()}
     * @utbot.invokes {@link SongService#update(Integer, SongRepresentation, String)}
     * @utbot.invokes {@link Response#noContent()}
     * @utbot.invokes {@link ResponseBuilder#build()}
     * @utbot.returnsFrom {@code return Response.noContent().build();}
     */
    @Test
    @DisplayName("update: SongInputDTOToRepresentation -> return Response.noContent().build()")
    public void testUpdate_ResponseBuild() {
        org.mockito.MockedStatic mockedStatic = null;
        try {
            mockedStatic = mockStatic(Response.class);
            ResponseBuilder responseBuilderMock = mock(ResponseBuilder.class);
            (when(responseBuilderMock.build())).thenReturn(((Response) null));
            (mockedStatic.when(Response::noContent)).thenReturn(responseBuilderMock);
            SongResource songResource = new SongResource();
            SongService songServiceMock = mock(SongService.class);
            (when(songServiceMock.update(any(), any(), any()))).thenReturn(((Song) null));
            songResource.songService = songServiceMock;
            SongInputDTO songRepresentationMock = mock(SongInputDTO.class);
            (when(songRepresentationMock.toRepresentation())).thenReturn(((SongRepresentation) null));

            Response actualResponse = (Response) songResource.update(null, songRepresentationMock, null);

            assertNull(actualResponse);
        } finally {
            mockedStatic.close();
        }
    }

    /**
     * @utbot.classUnderTest {@link SongResource}
     * @utbot.methodUnderTest {@link SongResource#update(Integer, SongInputDTO, String)}
     * @utbot.throwsException {@link NullPointerException} in: songService.update(id, songRepresentation.toRepresentation(), auth);
     */
    @Test
    @DisplayName("update: songService.update(id, songRepresentation.toRepresentation(), auth) : True -> ThrowNullPointerException")
    public void testUpdate_ThrowNullPointerException() {
        SongResource songResource = new SongResource();
        SongService songServiceMock = mock(SongService.class);
        songResource.songService = songServiceMock;

        songResource.update(null, null, null);
    }

    /**
     * @utbot.classUnderTest {@link SongResource}
     * @utbot.methodUnderTest {@link SongResource#update(Integer, SongInputDTO, String)}
     * @utbot.invokes {@link SongService#update(Integer, SongRepresentation, String)}
     * @utbot.invokes {@link Response#noContent()}
     * @utbot.invokes {@link ResponseBuilder#build()}
     * @utbot.throwsException {@link NullPointerException} in: return Response.noContent().build();
     */
    @Test
    @DisplayName("update: return Response.noContent().build() : True -> ThrowNullPointerException")
    public void testUpdate_ResponseBuild_1() {
        org.mockito.MockedStatic mockedStatic = null;
        try {
            mockedStatic = mockStatic(Response.class);
            (mockedStatic.when(Response::noContent)).thenReturn(((ResponseBuilder) null));
            SongResource songResource = new SongResource();
            SongService songServiceMock = mock(SongService.class);
            (when(songServiceMock.update(any(), any(), any()))).thenReturn(((Song) null));
            songResource.songService = songServiceMock;
            SongInputDTO songRepresentationMock = mock(SongInputDTO.class);
            (when(songRepresentationMock.toRepresentation())).thenReturn(((SongRepresentation) null));

            /* This test fails because method [gr.aueb.radio.content.infrastructure.rest.resource.SongResource.update] produces [java.lang.NullPointerException] */
            songResource.update(null, songRepresentationMock, null);
        } finally {
            mockedStatic.close();
        }
    }

    /**
     * @utbot.classUnderTest {@link SongResource}
     * @utbot.methodUnderTest {@link SongResource#update(Integer, SongInputDTO, String)}
     */
    @Test
    @DisplayName("update: id = positive, songRepresentation = SongInputDTO(), auth = '\n\t\r' -> throw NullPointerException")
    public void testUpdateThrowsNPEWithBlankString() {
        SongResource songResource = new SongResource();
        SongInputDTO songRepresentation = new SongInputDTO();
        songRepresentation.artist = "\n\t\r";
        songRepresentation.title = "abc";
        songRepresentation.year = 1;

        songResource.update(1, songRepresentation, "\n\t\r");
    }

    /**
     * @utbot.classUnderTest {@link SongResource}
     * @utbot.methodUnderTest {@link SongResource#update(Integer, SongInputDTO, String)}
     */
    @Test
    @DisplayName("update: id = max, songRepresentation = SongInputDTO(), auth = '' -> throw NullPointerException")
    public void testUpdateThrowsNPEWithCornerCaseAndEmptyString() {
        SongResource songResource = new SongResource();
        SongService songService = new SongService();
        songResource.songService = songService;
        SongInputDTO songRepresentation = new SongInputDTO();
        songRepresentation.duration = Integer.MAX_VALUE;
        

        songResource.update(Integer.MAX_VALUE, songRepresentation, "");
    }

}
