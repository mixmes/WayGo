package ru.sfedu.server.dto.converters;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sfedu.server.dto.point.PointDTO;
import ru.sfedu.server.dto.user.UserDTO;
import ru.sfedu.server.model.point.Point;
import ru.sfedu.server.model.user.User;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PointConverter implements Converter<Point, PointDTO> {
    @Autowired
    private ModelMapper modelMapper;

    @PostConstruct
    private void initTypeMaps(){
        TypeMap<Point,PointDTO> propertyMapper = modelMapper.createTypeMap(Point.class, PointDTO.class);
        propertyMapper.addMappings(mapper -> mapper.skip(PointDTO::setPhotos));
    }
    @Override
    public PointDTO convertToDto(Point entity) {
        return modelMapper.map(entity, PointDTO.class);
    }


    @Override
    public Point convertToEntity(PointDTO dto) {
        return modelMapper.map(dto, Point.class);
    }


}
