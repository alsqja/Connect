package com.example.connect.domain.file.service;

import com.example.connect.domain.file.dto.UrlResDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URL;
import java.time.Duration;
import java.util.UUID;

@Service
public class FileService {

    private final S3Presigner presigner;
    private final String bucketName;

    public FileService(
            @Value("${aws.s3.bucket-name}") String bucketName,
            @Value("${aws.s3.region}") String region,
            @Value("${aws.access-key}") String accessKey,
            @Value("${aws.secret-key}") String secretKey) {
        this.bucketName = bucketName;
        this.presigner = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                ))
                .build();
    }

    public UrlResDto createPreSignedUrl(String fileName) {

        String finalFileName = UUID.randomUUID() + "_" + fileName;

        PutObjectPresignRequest preSignRequest = PutObjectPresignRequest.builder()
                .putObjectRequest(req -> req.bucket(bucketName).key(finalFileName))
                .signatureDuration(Duration.ofMinutes(10))
                .build();

        URL preSignedUrl = presigner.presignPutObject(preSignRequest).url();

        return new UrlResDto(preSignedUrl.toString(), finalFileName);
    }
}
