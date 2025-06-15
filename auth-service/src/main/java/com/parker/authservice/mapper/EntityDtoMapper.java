package com.parker.authservice.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntityDtoMapper {
    private final ModelMapper modelMapper;

    public <E, D> E toEntity(D dto, Class<E> entityClass) {
        return modelMapper.map(dto, entityClass);
    }

    public <E, D> D toDto(E entity, Class<D> dtcClass) {
        return modelMapper.map(entity, dtcClass);
    }
}
