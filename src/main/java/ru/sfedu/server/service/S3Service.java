package ru.sfedu.server.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sfedu.server.model.metainfo.PhotoMetaInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class S3Service {
    @Autowired
    AmazonS3 s3Client;

    public List<byte[]> convertPhotosInfoToByteArrays(List<PhotoMetaInfo> photos) throws IOException {
        List<S3Object> objects = new ArrayList<>();
        for (PhotoMetaInfo metaInfo : photos) {
            objects.add(getS3Object(metaInfo.getBucketName(), metaInfo.getKey()));
        }

        List<byte[]> photoBytes = new ArrayList<>();
        for (S3Object s3Object : objects) {
            photoBytes.add(convertS3objectToByteArray(s3Object));
        }

        return photoBytes;
    }

    private byte[] convertS3objectToByteArray(S3Object s3Object) throws IOException {
        return s3Object.getObjectContent().readAllBytes();
    }

    private S3Object getS3Object(String bucketName, String key) {
        return s3Client.getObject(bucketName, key);
    }
}
