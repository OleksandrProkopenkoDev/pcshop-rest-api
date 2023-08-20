package com.spro.pcshop.servise;

import com.spro.pcshop.entity.ImageData;
import com.spro.pcshop.repository.ImageDataRepository;
import com.spro.pcshop.util.ImageUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ImageService {

    private final ImageDataRepository imageDataRepository;

    public byte[] getImageByUrlName(String name) {
        Long id = parseId(name);
        if (!imageDataRepository.existsById(id)) {
            return new byte[0];
        }
        Optional<ImageData> imageDataOptional = imageDataRepository.findById(id);
         if(imageDataOptional.isPresent()){
             return ImageUtils.decompressImage(imageDataOptional.get().getImageData());
         }else {
             return new byte[0];
         }
    }

    private Long parseId(String name) {
        String[] strings = name.split("\\.");
        try {
            return Long.parseLong(strings[0]);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Failed to parse image name : [" + name + "]", e);
        }
    }
}
