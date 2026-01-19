package com.crisd.comet.dto.input;

import jakarta.validation.constraints.Null;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public record CreatePostDTO(
        @Null String text,
       @Null List<MultipartFile> media
) {

}
