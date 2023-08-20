package com.spro.pcshop.dto;

import com.spro.pcshop.entity.ItemDetails;

public record ProductItemRequestPart(
        int price,
        String title,
        ItemDetailsDto details
) {
}
