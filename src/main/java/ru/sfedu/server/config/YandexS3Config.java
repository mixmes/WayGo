package ru.sfedu.server.config;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class YandexS3Config {
    private static final String BUCKET_NAME = "photo-metainfo";
    private static final String BUCKET_ID = "YCAJE_0oNBqpGTSQOG0hCZPEy";
    private static final String BUCKET_KEY = "YCPhwRE6_oMeDW8IhgxTYTQWiHz5942F4Pi5H0WC";

    @Bean
    public AmazonS3 s3Client() {
        AmazonS3 s3Client = null;
        try {
            s3Client = AmazonS3ClientBuilder.standard()
                    .withEndpointConfiguration(
                            new AwsClientBuilder.EndpointConfiguration(
                                    "https://storage.yandexcloud.net",
                                    "ru-central1"
                            )
                    )
                    .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(BUCKET_ID, BUCKET_KEY)))
                    .build();
        } catch (SdkClientException exception) {
            log.error(exception.getMessage());
        }
        return s3Client;
    }
}
