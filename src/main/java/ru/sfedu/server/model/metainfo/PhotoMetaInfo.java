package ru.sfedu.server.model.metainfo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class PhotoMetaInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private String key;
    private String bucketName;

}
