package com.spro.pcshop.controller;

import com.spro.pcshop.configs.UrlConfig;
import com.spro.pcshop.servise.ImageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @GetMapping("/api/v1/images/{name}")
    public ResponseEntity<?> getImageByUrl(@PathVariable String name){
        log.info("Received a request GET image with name: ["+name+"]");
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/jpeg"))
                .body(imageService.getImageByUrlName(name));
    }
}
