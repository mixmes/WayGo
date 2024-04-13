package ru.sfedu.server.dto.point;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sfedu.server.dto.metadata.ArMetadataDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointDTO {
    private long id;
    private String pointName;
    private String address;
    private String city;
    private String description;
    private double longitude;
    private double latitude;
    private byte[] photo;
    private ArMetadataDTO arMetadataDTO;
}
