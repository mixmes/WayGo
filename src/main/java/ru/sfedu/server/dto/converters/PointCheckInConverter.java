package ru.sfedu.server.dto.converters;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sfedu.server.dto.point.PointCheckInDTO;
import ru.sfedu.server.model.point.PointCheckIn;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PointCheckInConverter implements Converter<PointCheckIn, PointCheckInDTO> {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PointCheckInDTO convertToDto(PointCheckIn entity) {
        return modelMapper.map(entity, PointCheckInDTO.class);
    }



    @Override
    public PointCheckIn convertToEntity(PointCheckInDTO dto) {
        return modelMapper.map(dto, PointCheckIn.class);
    }


}
