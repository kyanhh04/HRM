package com.vatek.hrmtool.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.vatek.hrmtool.constant.CommonConstant;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;


@Configuration
@RequiredArgsConstructor
public class S3Config {

    private final Environment env;

    @Bean
    public AmazonS3 amazonS3(){

        String accessKey = env.getProperty(CommonConstant.AWS_S3_ACCESS_KEY_ID);
        String secretKey = env.getProperty(CommonConstant.AWS_S3_SECRET_ACCESS_KEY);
        String region = env.getProperty(CommonConstant.AWS_S3_REGION);
        String endpoint = env.getProperty("aws.s3.endpoint");
        String bucket = env.getProperty(CommonConstant.AWS_S3_BUCKET);

        if(StringUtils.isAnyBlank(accessKey, secretKey))
        {
            return  null;
        }

        AWSCredentials credentials = new BasicAWSCredentials(
            accessKey,secretKey
        );

        AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials));

        // If a custom endpoint is provided (for MinIO or other S3-compatible storage), configure it
        if(!StringUtils.isBlank(endpoint)){
            AwsClientBuilder.EndpointConfiguration endpointConfig = new AwsClientBuilder.EndpointConfiguration(endpoint, region);
            builder.withEndpointConfiguration(endpointConfig);
            // For S3-compatible storages like MinIO, enable path-style access
            builder.setPathStyleAccessEnabled(true);
        } else {
            builder.withRegion(region);
        }

        AmazonS3 s3 = builder.build();

        // Ensure bucket exists when running locally against MinIO
        if(!StringUtils.isBlank(bucket)){
            if(!s3.doesBucketExistV2(bucket)){
                s3.createBucket(bucket);
            }
        }

        return s3;
    }
}