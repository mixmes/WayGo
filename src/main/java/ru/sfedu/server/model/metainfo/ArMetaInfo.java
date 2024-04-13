package ru.sfedu.server.model.metainfo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ArMetaInfo extends MetaInfo{
    @Id
    @GeneratedValue
    private long id;
    private int scale;
}
