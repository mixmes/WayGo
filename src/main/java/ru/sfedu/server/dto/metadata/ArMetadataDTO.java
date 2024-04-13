package ru.sfedu.server.dto.metadata;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArMetadataDTO {
    private byte[] arFile;
    private int scale;
}
