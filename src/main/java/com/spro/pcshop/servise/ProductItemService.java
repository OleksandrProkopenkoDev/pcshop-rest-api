package com.spro.pcshop.servise;

import com.spro.pcshop.dto.ItemDetailsDto;
import com.spro.pcshop.dto.ProductItemDto;
import com.spro.pcshop.dto.ProductItemRequestPart;
import com.spro.pcshop.entity.*;
import com.spro.pcshop.repository.ConnectionInterfaceRepository;
import com.spro.pcshop.repository.FeatureRepository;
import com.spro.pcshop.repository.ImageDataRepository;
import com.spro.pcshop.repository.ProductItemRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.spro.pcshop.util.ImageUtils.compressImage;

@Slf4j
@AllArgsConstructor
@Service
public class ProductItemService {
    private final ProductItemRepository productItemRepository;
    private final ImageDataRepository imageDataRepository;
    private final FeatureRepository featureRepository;
    private final ConnectionInterfaceRepository connectionInterfaceRepository;

    public List<ProductItemDto> getAllProductItems() {
// get Product items with images, but without ItemDetails
        List<ProductItem> productItems = productItemRepository.findAll();
        return productItems.stream()
                .map(productItemToDtoMapper())
                .toList();
    }

    @Transactional
    public String addNewProductItem(ProductItemRequestPart itemRequestPart, List<MultipartFile> images) {
//create new Product item
// save this object
        ItemDetailsDto itemDetailsDto = itemRequestPart.details();
        List<String> featuresStrings = itemDetailsDto.features();
        if (featuresStrings == null){
            featuresStrings = new ArrayList<>();
        }
        List<Feature> features = featuresStrings
                .stream()
                .map(Feature::new)
                .toList();
//        save features. If feature is not saved -> save and return feature with id.
//        If feature already saved with this name, return feature with id.
        List<Feature> featuresWithIds = saveFeatures(features);

        List<String> interfacesStrings = itemDetailsDto.interfaces();
        if(interfacesStrings == null){
            interfacesStrings = new ArrayList<>();
        }
        List<ConnectionInterface> interfaces = interfacesStrings
                .stream()
                .map(ConnectionInterface::new)
                .toList();
        //        save interfaces. If interface is not saved -> save and return interface with id.
//        If interface already saved with this name, return interface with id.
        List<ConnectionInterface> interfacesWithIds = saveInterfaces(interfaces);

        List<ImageData> imageDataList = images.stream()
                .map(file -> {
                    try {
                        return ImageData.builder()
                                .name(file.getOriginalFilename())
                                .type(file.getContentType())
                                .imageData(compressImage(file.getBytes()))
                                .isPrimary(images.indexOf(file) == 0)
                                .build();
                    } catch (IOException e) {
                        throw new RuntimeException("Error during getting file bytes", e);
                    }

                })
                .toList();
//              save images, or try to add List<ImageData> as is, and save whole ProductItem
        ItemDetails itemDetails = ItemDetails.builder()
                .warranty(itemDetailsDto.warranty())
                .producingCountry(itemDetailsDto.producingCountry())
                .color(itemDetailsDto.color())
                .brightness(itemDetailsDto.brightness())
                .diagonal(itemDetailsDto.diagonal())
                .frequency(itemDetailsDto.frequency())
                .maxDisplayResolution(itemDetailsDto.maxDisplayResolution())
                .matrixType(itemDetailsDto.matrixType())
                .features(featuresWithIds)
                .interfaces(interfacesWithIds)
                .build();

        ProductItem productItem = ProductItem.builder()
                .title(itemRequestPart.title())
                .price(itemRequestPart.price())
                .details(itemDetails)
                .imageDataList(imageDataList)
                .build();

        ProductItem saved = productItemRepository.save(productItem);
        return "ProductItem successfully saved to DB with id [" + saved.getId() + "]";
    }

    public List<ImageData> saveImageDataList(List<ImageData> imageDataList) {
        return imageDataList.stream()
                .map(imageDataRepository::save)
                .toList();

    }

    public List<ConnectionInterface> saveInterfaces(List<ConnectionInterface> interfaces) {
        return interfaces.stream()
                .map(connectionInterface -> {
                    ConnectionInterface savedInterface;
                    if (!connectionInterfaceRepository.existsByName(connectionInterface.getName())) {
                        log.info("ConnectionInterface " + connectionInterface + " not exists in DB. Trying to save...");
                        savedInterface = connectionInterfaceRepository.save(connectionInterface);
                    } else {
                        savedInterface = connectionInterfaceRepository.findByName(connectionInterface.getName()).orElseThrow();
                    }
                    return savedInterface;
                }).toList();
    }

    public List<Feature> saveFeatures(List<Feature> features) {
        return features.stream()
                .map(feature -> {
                    Feature saved;
                    if (!featureRepository.existsByName(feature.getName())) {
                        log.info("Feature " + feature + " not exists in DB. Trying to save...");
                        saved = featureRepository.save(feature);
                    } else {
                        saved = featureRepository.findByName(feature.getName()).orElseThrow();
                    }
                    return saved;
                })
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
