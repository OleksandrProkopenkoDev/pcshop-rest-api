package com.spro.pcshop.controller;

import com.spro.pcshop.dto.ProductItemDto;
import com.spro.pcshop.servise.ProductItemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/monitors")
public class ProductItemController {

    private final ProductItemService productItemService;

    @GetMapping
    public List<ProductItemDto> getProductItemsList(){
        return productItemService.getAllProductItems();
    }
}
