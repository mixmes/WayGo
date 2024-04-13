package ru.sfedu.server.model.metainfo;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class MetaInfo {
    private String key;
    private String bucketName;
}
