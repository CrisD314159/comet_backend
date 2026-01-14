package com.crisd.comet.services.interfaces;

import com.crisd.comet.model.Image;
import jakarta.mail.Multipart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IMediaService {
    Image UploadImage(MultipartFile image);
    List<Image> UploadSeveralImages(List<MultipartFile> images);
    void DeleteSeveral(List<Image> images);
    void DeleteImage(String imageId);
    Image GetImage(String id);
}
