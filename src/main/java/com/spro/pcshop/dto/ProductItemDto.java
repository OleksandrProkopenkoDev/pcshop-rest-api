package com.spro.pcshop.dto;

import com.spro.pcshop.entity.ImageData;

import java.util.List;

public record ProductItemDto(

        Long id,
        int price,
        String title,
        List<ImageData> images
) {
}
