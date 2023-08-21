package com.spro.pcshop.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ItemDetailsDto(
        Integer warranty,
        String producingCountry,
         String color,
        int brightness,
        double diagonal,
        int frequency,
        String maxDisplayResolution,
        String matrixType,
        List<String> interfaces,
        List<String> features
) {
}
