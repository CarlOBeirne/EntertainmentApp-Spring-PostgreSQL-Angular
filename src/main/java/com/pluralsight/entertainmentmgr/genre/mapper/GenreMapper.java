package com.pluralsight.entertainmentmgr.genre.mapper;

import com.pluralsight.entertainmentmgr.core.mapper.BaseMapper;
import com.pluralsight.entertainmentmgr.genre.entities.Genre;
import com.pluralsight.entertainmentmgr.genre.models.GenreDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface GenreMapper extends BaseMapper<Genre, GenreDto> {

    @Override
    GenreDto toDTO(Genre entity);

    @Override
    Genre toEntity(GenreDto dto);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(GenreDto dto, @MappingTarget Genre entity);
}
