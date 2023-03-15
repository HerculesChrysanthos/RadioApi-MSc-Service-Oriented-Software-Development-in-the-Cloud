package gr.aueb.radio.mappers;

import gr.aueb.radio.domains.Song;
import gr.aueb.radio.representations.SongRepresentation;

import java.util.List;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi",
injectionStrategy = InjectionStrategy.CONSTRUCTOR,
		uses = {SongBroadcastMapper.class})
public abstract class SongMapper {

    public abstract SongRepresentation toRepresentation(Song song);
	
	public abstract List<SongRepresentation> toRepresentationList(List<Song> list);

    public abstract Song toModel(SongRepresentation songRepresentation);
}
