package ru.sfedu.server.dto.converters;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sfedu.server.dto.route.RouteDTO;
import ru.sfedu.server.model.route.Route;

import java.util.Set;

@Component
public class RouteConverter implements Converter<Route, RouteDTO> {
    @Autowired
    private ModelMapper mapper;

    @PostConstruct
    private void init() {
        TypeMap<Route, RouteDTO> propertyMapper = mapper.createTypeMap(Route.class, RouteDTO.class);
        propertyMapper.addMappings(mapper -> mapper.map(Route::getRouteGrades, RouteDTO::setRouteGrades));
        propertyMapper.addMappings(mapper -> mapper.map(Route::getOrderedPoints, RouteDTO::setStopsOnRoute));
    }

    @Override
    public RouteDTO convertToDto(Route entity) {
        return mapper.map(entity, RouteDTO.class);
    }



    @Override
    public Route convertToEntity(RouteDTO dto) {
        return mapper.map(dto, Route.class);
    }


}
