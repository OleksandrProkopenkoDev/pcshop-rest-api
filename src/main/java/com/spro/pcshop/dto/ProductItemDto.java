package com.spro.pcshop.dto;

import java.util.List;

public record ProductItemDto(

        Long id,
        int price,
        String title,
        List<String> images
) {
}
