package ru.sfedu.server.dto.converters;

import java.util.Set;

public interface Converter <S,D>{
    D convertToDto(S entity);
    S convertToEntity(D dto);
}
