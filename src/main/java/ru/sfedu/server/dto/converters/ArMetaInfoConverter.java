package ru.sfedu.server.dto.converters;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sfedu.server.dto.metadata.ArMetaInfoDTO;
import ru.sfedu.server.model.metainfo.ArMetaInfo;

@Service
public class ArMetaInfoConverter implements Converter<ArMetaInfo, ArMetaInfoDTO>{
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ArMetaInfoDTO convertToDto(ArMetaInfo entity) {
        return modelMapper.map(entity,ArMetaInfoDTO.class);
    }

    @Override
    public ArMetaInfo convertToEntity(ArMetaInfoDTO dto) {
        return modelMapper.map(dto,ArMetaInfo.class);
    }
}
