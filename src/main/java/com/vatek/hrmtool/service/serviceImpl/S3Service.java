package com.vatek.hrmtool.service.serviceImpl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class S3Service {
    
    @Autowired(required = false)
    private AmazonS3 amazonS3;
    
    @Autowired
    private Environment env;
    
    public Map<String, Object> uploadS3(byte[] fileBytes, String bucketName, String userId, String filename) {
        if (amazonS3 == null) {
            return uploadLocal(fileBytes, userId, filename);
        }
        
        try {
            String s3Folder = env.getProperty("aws.s3.folder");
            if (s3Folder == null || s3Folder.isEmpty()) {
                s3Folder = "";
            } else if (!s3Folder.endsWith("/")) {
                s3Folder = s3Folder + "/";
            }
            String objectKey = s3Folder + "avatar/" + userId + "/" + System.currentTimeMillis() + "_" + filename;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(fileBytes.length);
            InputStream inputStream = new ByteArrayInputStream(fileBytes);
            PutObjectRequest request = new PutObjectRequest(
                bucketName,
                objectKey,
                inputStream,
                metadata
            );
            
            PutObjectResult result = amazonS3.putObject(request);
            String s3ObjectUrl = env.getProperty("aws.s3.object.url") + objectKey;
            
            Map<String, Object> response = new HashMap<>();
            response.put("Location", s3ObjectUrl);
            response.put("ETag", result.getETag());
            response.put("Key", objectKey);
            
            return response;
        } catch (AmazonServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Failed to upload to S3: " + e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error uploading file to S3: " + e.getMessage());
        }
    }
    
    private Map<String, Object> uploadLocal(byte[] fileBytes, String userId, String filename) {
        try {
            String uniqueFilename = System.currentTimeMillis() + "_" + filename;
            String objectKey = "avatar/" + userId + "/" + uniqueFilename;
            Path uploadPath = Paths.get("uploads").toAbsolutePath();
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path avatarPath = uploadPath.resolve("avatar");
            if (!Files.exists(avatarPath)) {
                Files.createDirectories(avatarPath);
            }
            Path userPath = avatarPath.resolve(userId);
            if (!Files.exists(userPath)) {
                Files.createDirectories(userPath);
            }
            Path filePath = userPath.resolve(uniqueFilename);
            Files.write(filePath, fileBytes);
            
            Map<String, Object> response = new HashMap<>();
            response.put("Location", "/uploads/" + objectKey);
            response.put("Key", objectKey);
            
            return response;
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error uploading file locally: " + ex.getMessage());
        }
    }
}
