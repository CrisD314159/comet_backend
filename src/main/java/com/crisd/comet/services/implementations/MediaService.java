package com.crisd.comet.services.implementations;

import com.crisd.comet.services.interfaces.IMediaService;
import jakarta.mail.Multipart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MediaService implements IMediaService {

    @Override
    public String UploadImage(Multipart image) {
        return "";
    }
}
