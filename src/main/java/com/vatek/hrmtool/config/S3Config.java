package com.vatek.hrmtool.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;

import com.amazonaws.services.s3.AmazonS3ClientBuilder;
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
        if(StringUtils.isAnyBlank(accessKey, secretKey))
        {
//            ErrorResponse errorResponse = new ErrorResponse();
//            errorResponse.setCode("NOT_NULL");
//            errorResponse.setType("NOT_NULL_AWS_CREDENTIAL");
//            errorResponse.setMessage("AccessKey and SecretKey must not be null");
//            throw new HrmToolException(errorResponse);
            return  null;
        }

        AWSCredentials credentials = new BasicAWSCredentials(
            accessKey,secretKey
        );

        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(env.getProperty(CommonConstant.AWS_S3_REGION))
                .build();
    }
}
