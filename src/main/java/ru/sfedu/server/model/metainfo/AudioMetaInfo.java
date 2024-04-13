package ru.sfedu.server.model.metainfo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class AudioMetaInfo extends MetaInfo {
    @Id
    @GeneratedValue
    private long id;
}
