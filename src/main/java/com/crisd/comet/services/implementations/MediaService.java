package com.crisd.comet.services.implementations;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.crisd.comet.exceptionHandling.exceptions.EntityNotFoundException;
import com.crisd.comet.exceptionHandling.exceptions.UnexpectedException;
import com.crisd.comet.model.Image;
import com.crisd.comet.repositories.ImageRepository;
import com.crisd.comet.services.interfaces.IMediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MediaService implements IMediaService {

    private final Cloudinary cloudinary;
    private final ImageRepository imageRepository;

    @Override
    public Image UploadImage(MultipartFile image) {
        try{
            HashMap<Object, Object> options = new HashMap<>();
            options.put("folder", "comet");
            Map uploadedFile = cloudinary.uploader().upload(image.getBytes(), options);
            String publicId = (String) uploadedFile.get("public_id");
            String publicUrl = cloudinary.url().secure(true).generate(publicId);

            Image imageObject = Image.builder().id(publicId).url(publicUrl).build();
            imageRepository.save(imageObject);

            return imageObject;
        } catch (IOException e) {
            throw new UnexpectedException("Failed to upload image: " + e.getMessage());
        }

    }

    @Override
    public List<Image> UploadSeveralImages(List<MultipartFile> images) {
        return images.stream().map(this::UploadImage).toList();
    }

    @Override
    public void DeleteSeveral(List<Image> images) {
        images.forEach(image -> DeleteImage(image.getId()));
    }

    @Override
    public void DeleteImage(String imageId) {
        try{
            Optional<Image> imageOptional = imageRepository.findById(imageId);
            if (imageOptional.isEmpty()) throw new EntityNotFoundException("Image not found");
            Image image = imageOptional.get();
            cloudinary.uploader().destroy(imageId, ObjectUtils.emptyMap());
            imageRepository.delete(image);
        } catch (IOException e) {
            throw new UnexpectedException("Failed to upload image: " + e.getMessage());
        }

    }

    @Override
    public Image GetImage(String id) {
        Optional<Image> imageOptional = imageRepository.findById(id);

        if(imageOptional.isEmpty()){
            throw new EntityNotFoundException("Image not found");
        }

        return imageOptional.get();
    }
}
