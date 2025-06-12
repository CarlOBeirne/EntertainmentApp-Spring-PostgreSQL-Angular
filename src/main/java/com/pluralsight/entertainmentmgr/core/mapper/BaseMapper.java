package com.pluralsight.entertainmentmgr.core.mapper;

import com.pluralsight.entertainmentmgr.core.auditable.entity.BaseEntity;
import com.pluralsight.entertainmentmgr.core.auditable.models.BaseDto;
import org.mapstruct.*;

public interface BaseMapper<E extends BaseEntity, D extends BaseDto> {

    D toDTO(E entity);
    E toEntity(D dto);
    void updateEntityFromDto(D dto, @MappingTarget E entity);

}
