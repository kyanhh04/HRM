package com.vatek.hrmtool.service;

import com.vatek.hrmtool.entity.Image;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    Image uploadAndCreate(MultipartFile file, String userId);
}
