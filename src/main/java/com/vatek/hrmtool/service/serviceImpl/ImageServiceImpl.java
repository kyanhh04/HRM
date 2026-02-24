package com.vatek.hrmtool.service.serviceImpl;

import com.vatek.hrmtool.entity.Image;
import com.vatek.hrmtool.respository.ImageRepository;
import com.vatek.hrmtool.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class ImageServiceImpl implements ImageService {
    
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
        "image/jpeg",
        "image/png",
        "image/gif",
        "image/webp",
        "image/jpg"
    );
    
    @Autowired
    private ImageRepository imageRepository;
    
    @Autowired
    private S3Service s3Service;
    
    @Autowired
    private Environment env;
    
    @Override
    public Image uploadAndCreate(MultipartFile file, String userId) {
        if (!checkImageTypeIsValid(file)) {
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Invalid image type");
        }
        try {
            String bucketName = env.getProperty("aws.s3.bucket");
            if (bucketName == null || bucketName.isEmpty()) {
                bucketName = "hrm-tool";
            }
            Map<String, Object> uploadResult = s3Service.uploadS3(
                file.getBytes(),
                bucketName,
                userId,
                file.getOriginalFilename()
            );
            if (uploadResult == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to upload to S3");
            }
            String s3Location = (String) uploadResult.get("Location");
            String s3Url = s3Location + "?" + System.currentTimeMillis();
            Image image = new Image();
            image.setName(file.getOriginalFilename());
            image.setSrc(s3Url);
            return imageRepository.save(image);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error reading file: " + e.getMessage());
        }
    }
    
    private boolean checkImageTypeIsValid(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null) {
            return false;
        }
        return ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase());
    }
}
