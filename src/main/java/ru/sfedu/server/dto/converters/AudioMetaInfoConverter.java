package ru.sfedu.server.dto.converters;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sfedu.server.dto.metadata.AudioMetaInfoDto;
import ru.sfedu.server.model.metainfo.AudioMetaInfo;

@Service
public class AudioMetaInfoConverter implements Converter<AudioMetaInfo, AudioMetaInfoDto> {
    @Autowired
    private ModelMapper mapper;

    @Override
    public AudioMetaInfoDto convertToDto(AudioMetaInfo entity) {
        return mapper.map(entity, AudioMetaInfoDto.class);
    }

    @Override
    public AudioMetaInfo convertToEntity(AudioMetaInfoDto dto) {
        return mapper.map(dto, AudioMetaInfo.class);
    }
}
