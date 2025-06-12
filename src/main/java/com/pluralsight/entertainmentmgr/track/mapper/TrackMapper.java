package com.pluralsight.entertainmentmgr.track.mapper;

import com.pluralsight.entertainmentmgr.core.mapper.BaseMapper;
import com.pluralsight.entertainmentmgr.track.entities.Track;
import com.pluralsight.entertainmentmgr.track.models.TrackDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TrackMapper extends BaseMapper<Track, TrackDto> {

    @Override
    TrackDto toDTO(Track entity);

    @Override
    @Mapping(target = "artists", ignore = true)
    @Mapping(target = "genre", ignore = true)
    Track toEntity(TrackDto dto);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "artists", ignore = true)
    @Mapping(target = "genre", ignore = true)
    void updateEntityFromDto(TrackDto dto, @MappingTarget Track entity);
}
