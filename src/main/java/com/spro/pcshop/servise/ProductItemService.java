package com.spro.pcshop.servise;

import com.spro.pcshop.dto.ProductItemDto;
import com.spro.pcshop.entity.ProductItem;
import com.spro.pcshop.repository.ImageDataRepository;
import com.spro.pcshop.repository.ProductItemRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Slf4j
@AllArgsConstructor
@Service
public class ProductItemService {
    private final ProductItemRepository productItemRepository;
    private final ImageDataRepository imageDataRepository;
    public List<ProductItemDto> getAllProductItems() {
// get Product items with images, but without ItemDetails
        List<ProductItem> productItems = productItemRepository.findAll();
        return productItems.stream()
                .map(productItemToDtoMapper())
                .toList();
    }

    private Function<ProductItem, ProductItemDto> productItemToDtoMapper() {
        return productItem -> new ProductItemDto(
                productItem.getId(),
                productItem.getPrice(),
                productItem.getTitle(),
                productItem.getImageDataList()
        );
    }
}
