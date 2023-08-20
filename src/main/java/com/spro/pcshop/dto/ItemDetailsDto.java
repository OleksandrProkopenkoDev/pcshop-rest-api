package com.spro.pcshop.dto;

import java.util.List;

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
