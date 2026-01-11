package com.crisd.comet.services.interfaces;

import jakarta.mail.Multipart;

public interface IMediaService {
    String UploadImage(Multipart image);
}
