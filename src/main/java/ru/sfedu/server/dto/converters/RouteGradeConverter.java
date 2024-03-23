package ru.sfedu.server.dto.converters;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sfedu.server.dto.route.RouteGradeDTO;
import ru.sfedu.server.model.route.RouteGrade;

@Component
public class RouteGradeConverter implements Converter<RouteGrade, RouteGradeDTO> {
    @Autowired
    private ModelMapper mapper;

    @Override
    public RouteGradeDTO convertToDto(RouteGrade entity) {
        return mapper.map(entity, RouteGradeDTO.class);
    }

    @Override
    public RouteGrade convertToEntity(RouteGradeDTO dto) {
        return mapper.map(dto, RouteGrade.class);
    }
}
