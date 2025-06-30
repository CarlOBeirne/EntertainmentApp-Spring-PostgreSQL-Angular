package com.pluralsight.entertainmentmgr.playlist.mapper;

import com.pluralsight.entertainmentmgr.playlist.entities.Playlist;
import com.pluralsight.entertainmentmgr.playlist.models.PlaylistDto;
import com.pluralsight.entertainmentmgr.track.mapper.TrackMapper;

import com.pluralsight.entertainmentmgr.core.mapper.BaseMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = { TrackMapper.class })
public interface PlaylistMapper extends BaseMapper<Playlist, PlaylistDto> {

    //TrackMapper trackMapper = null;

    @Override
    @Mapping(target = "tracks", ignore = true)
    PlaylistDto toDTO(Playlist entity);

    //default PlaylistDto mapToDto(Playlist entity) {
    //    PlaylistDto dto = toDTO(entity);
    //    dto.setTracks(
    //            entity.getPlaylistTracks().stream()
    //                    .sorted(Comparator.comparingInt(PlaylistTrack::getTrackOrder))
    //                    .map(PlaylistTrack::getTrack)
    //                    .map(getTrackMapper()::toDTO)
    //                    .collect(Collectors.toList())
    //    );
    //    return dto;
    //}

    @Override
    @Mapping(target = "tracks", ignore = true)
    void updateEntityFromDto(PlaylistDto dto, @MappingTarget Playlist entity);

    @Override
    @Mapping(target = "tracks", ignore = true)
    Playlist toEntity(PlaylistDto dto);

}
