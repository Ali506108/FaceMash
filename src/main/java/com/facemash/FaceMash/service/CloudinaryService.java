package com.facemash.FaceMash.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {


    private final Cloudinary cloudinary;

    @Autowired
    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }


    public String uploadToImage(MultipartFile path) {

        Map<String , Object> params = ObjectUtils.asMap(
                "folder", "Movie",
                "resource_type", "image"
        );
        try{
            Map<?, ?> uploadResult = cloudinary.uploader().upload(path.getBytes(), params );
            return uploadResult.get("secure_url").toString();
        }catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при загрузке изображения в Cloudinary");
        }

    }
}
