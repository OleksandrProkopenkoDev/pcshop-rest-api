package com.spro.pcshop.dto;

import java.util.List;

public record ProductItemDetailedDto(
        Long id,
        int price,
        String title,
        List<String> images,
        ItemDetailsDto details
) {
}
