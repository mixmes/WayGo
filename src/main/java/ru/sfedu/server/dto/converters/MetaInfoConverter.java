package ru.sfedu.server.dto.converters;

import org.aspectj.asm.AsmManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sfedu.server.dto.metadata.MetaInfoDto;
import ru.sfedu.server.model.metainfo.MetaInfo;

@Service
public class MetaInfoConverter implements Converter<MetaInfo, MetaInfoDto> {
    @Autowired
    ModelMapper mapper;

    @Override
    public MetaInfoDto convertToDto(MetaInfo entity) {
        return mapper.map(entity, MetaInfoDto.class);
    }

    @Override
    public MetaInfo convertToEntity(MetaInfoDto dto) {
        return mapper.map(dto, MetaInfo.class);
    }
}
