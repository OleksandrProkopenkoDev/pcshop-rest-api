package com.spro.pcshop.dto;

public record ProductItemRequestPart(
        int price,
        String model,
        String brand,
        ItemDetailsDto details
) {
}
