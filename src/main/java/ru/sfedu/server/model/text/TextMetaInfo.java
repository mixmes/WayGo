package ru.sfedu.server.model.text;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

import java.io.File;

@Entity
public class TextMetaInfo {
    @Id
    @GeneratedValue
    private long id;

    @Lob
    private File textMetaInfo;
}
