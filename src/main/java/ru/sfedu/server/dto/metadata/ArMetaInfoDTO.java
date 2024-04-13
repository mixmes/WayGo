package ru.sfedu.server.dto.metadata;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArMetaInfoDTO {
    private String key;
    private String bucketName;
    private int scale;
}
