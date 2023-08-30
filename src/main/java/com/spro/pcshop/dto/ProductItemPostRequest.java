package com.spro.pcshop.dto;

import java.util.List;


public record ProductItemPostRequest(
        int price,
        String model,
        String brand,
        List<String> images,
        ItemDetailsDto details
) {
}
