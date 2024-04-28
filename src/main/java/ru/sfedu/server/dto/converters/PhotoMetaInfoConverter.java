package ru.sfedu.server.dto.converters;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sfedu.server.dto.metadata.PhotoMetadataInfoDto;
import ru.sfedu.server.model.metainfo.PhotoMetaInfo;

@Service
public class PhotoMetaInfoConverter implements Converter<PhotoMetaInfo, PhotoMetadataInfoDto>{
    @Autowired
    private ModelMapper mapper;
    @Override
    public PhotoMetadataInfoDto convertToDto(PhotoMetaInfo entity) {
        return mapper.map(entity, PhotoMetadataInfoDto.class);
    }

    @Override
    public PhotoMetaInfo convertToEntity(PhotoMetadataInfoDto dto) {
        return mapper.map(dto,PhotoMetaInfo.class);
    }
}
