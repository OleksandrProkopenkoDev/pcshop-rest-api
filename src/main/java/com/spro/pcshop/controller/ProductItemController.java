package com.spro.pcshop.controller;

import com.spro.pcshop.dto.ProductItemDetailedDto;
import com.spro.pcshop.dto.ProductItemDto;
import com.spro.pcshop.dto.ProductItemRequestPart;
import com.spro.pcshop.servise.ProductItemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/")
public class ProductItemController {

    private final ProductItemService productItemService;

    @GetMapping("monitors")
    public List<ProductItemDto> getProductItemsList(){
        return productItemService.getAllProductItems();
    }
    @GetMapping("monitors-details")
    public List<ProductItemDetailedDto> getProductItemsWithDetails(){
        return productItemService.getAllProductItemsWithDetails();
    }

    @PostMapping( path = "monitors",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String addProductItem(
            @RequestPart("productItem")ProductItemRequestPart productItemRequestPart,
            @RequestPart("images")List<MultipartFile> images){
        log.info("New request received");
        log.info("productItem :"+productItemRequestPart);
        return productItemService.addNewProductItem(productItemRequestPart, images);
    }
}
