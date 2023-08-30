package com.spro.pcshop.controller;

import com.spro.pcshop.dto.*;
import com.spro.pcshop.servise.ProductItemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/")
public class ProductItemController {

    private final ProductItemService productItemService;


    @GetMapping("monitors/{id}")
    public ResponseEntity<ProductItemDetailedDto> getProductItemsList(@PathVariable Long id){
        Optional<ProductItemDetailedDto> optionalProductItemDetailedDto =
                productItemService.getProductItemById(id);
        return optionalProductItemDetailedDto
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity
                        .notFound()
                        .build());
    }


    @GetMapping("monitors")
    public List<ProductItemDto> getProductItemsList(){
        return productItemService.getAllProductItems();
    }
    @GetMapping("monitors-details")
    public List<ProductItemDetailedDto> getProductItemsWithDetails(){
        return productItemService.getAllProductItemsWithDetails();
    }

    @PostMapping( path = "monitors/add",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String addProductItem(
            @RequestPart("productItem")ProductItemRequestPart productItemRequestPart,
            @RequestPart("images")List<MultipartFile> images){
        log.info("New request received");
        log.info("productItem :"+productItemRequestPart);
        return productItemService.addNewProductItem(productItemRequestPart, images);
    }
    @PostMapping("monitors/add-by-urls")
    public String addProductItemWithUrls(
            @RequestBody ProductItemPostRequest productItemPostRequest
        ){
        log.info("New POST request received");
        log.info("productItemRequestBody :"+productItemPostRequest);
        return productItemService.addNewProductItem(productItemPostRequest);
    }
}
