package com.spro.pcshop.servise;

import com.spro.pcshop.configs.UrlConfig;
import com.spro.pcshop.dto.*;
import com.spro.pcshop.entity.*;
import com.spro.pcshop.repository.*;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.spro.pcshop.util.ImageUtils.compressImage;

@Slf4j
@AllArgsConstructor
@Service
public class ProductItemService {

    private final ProductItemRepository productItemRepository;
    private final ImageDataRepository imageDataRepository;
    private final FeatureRepository featureRepository;
    private final ConnectionInterfaceRepository connectionInterfaceRepository;
    private final BrandRepository brandRepository;
    private final UrlConfig urlConfig;
    private final WebClient webClient;


    public List<ProductItemDto> getAllProductItems() {
// get Product items with images, but without ItemDetails
        List<ProductItem> productItems = productItemRepository.findAll();
        return productItems.stream()
                .map(productItemToDtoMapper())
                .toList();
    }

    public List<ProductItemDetailedDto> getAllProductItemsWithDetails() {
// get Product items with images with ItemDetails
        List<ProductItem> productItems = productItemRepository.findAll();
        return productItems.stream()
                .map(productItemToDtoDetailsMapper())
                .toList();
    }

    @Transactional
    public String addNewProductItem(ProductItemPostRequest productItemPostRequest) {

        ItemDetailsDto itemDetailsDto = productItemPostRequest.details();

        List<Feature> features = getFeatures(itemDetailsDto.features());
        List<Feature> featuresWithIds = saveFeatures(features);

        List<ConnectionInterface> interfaces = getInterfaces(itemDetailsDto.interfaces());
        List<ConnectionInterface> interfacesWithIds = saveInterfaces(interfaces);

        ItemDetails itemDetails = getItemDetailsFromDto(itemDetailsDto, featuresWithIds, interfacesWithIds);

        List<ImageData> imageDataList = getImageDataListFromUrls(productItemPostRequest.images());
//      save images, or try to add List<ImageData> as is, and save whole ProductItem

        Brand brand = getBrand(productItemPostRequest);

        ProductItem productItem = ProductItem.builder()
                .model(productItemPostRequest.model())
                .brand(brand)
                .price(productItemPostRequest.price())
                .details(itemDetails)
                .imageDataList(imageDataList)
                .build();

        ProductItem saved = productItemRepository.save(productItem);
        return "ProductItem successfully saved to DB with id [" + saved.getId() + "]";

    }

    private List<ImageData> getImageDataListFromUrls(List<String> images) {
        return images.stream()
                .map(url ->  ImageData.builder()
                            .name(getNameFromUrl(url))
                            .type(getTypeFromUrl(url))
                            .imageData(compressImage(downloadImage(url)))
                            .isPrimary(images.indexOf(url) == 0)
                            .build()
                )
                .toList();
    }

    private String getTypeFromUrl(String url) {
        // "https://content1.rozetka.com.ua/goods/images/original/175135466.jpg"
        String[] strings = url.split("\\.");
        return strings[strings.length - 1];
    }

    private String getNameFromUrl(String url) {
        // "https://content1.rozetka.com.ua/goods/images/original/175135466.jpg"
        String[] strings = url.split("/");
        return strings[strings.length - 1];
    }

    private byte[] downloadImage(String imageUrl) {
        return webClient.get()
                .uri(imageUrl)
                .retrieve()
                .bodyToMono(byte[].class)
                .block();
    }


    @Transactional
    public String addNewProductItem(ProductItemRequestPart itemRequestPart, List<MultipartFile> images) {
//create new Product item
// save this object
        ItemDetailsDto itemDetailsDto = itemRequestPart.details();

        List<Feature> features = getFeatures(itemDetailsDto.features());
//        save features. If feature is not saved -> save and return feature with id.
//        If feature already saved with this name, return feature with id.
        List<Feature> featuresWithIds = saveFeatures(features);

        List<ConnectionInterface> interfaces = getInterfaces(itemDetailsDto.interfaces());
//        save interfaces. If interface is not saved -> save and return interface with id.
//        If interface already saved with this name, return interface with id.
        List<ConnectionInterface> interfacesWithIds = saveInterfaces(interfaces);

        List<ImageData> imageDataList = getImageDataListFromMultipartFiles(images);
//      save images, or try to add List<ImageData> as is, and save whole ProductItem
        ItemDetails itemDetails = getItemDetailsFromDto(itemDetailsDto, featuresWithIds, interfacesWithIds);

        Brand brand = getBrand(itemRequestPart);

        ProductItem productItem = ProductItem.builder()
                .model(itemRequestPart.model())
                .brand(brand)
                .price(itemRequestPart.price())
                .details(itemDetails)
                .imageDataList(imageDataList)
                .build();

        ProductItem saved = productItemRepository.save(productItem);
        return "ProductItem successfully saved to DB with id [" + saved.getId() + "]";
    }

    private List<ConnectionInterface> getInterfaces(List<String> interfacesStrings) {
        if (interfacesStrings == null) {
            interfacesStrings = new ArrayList<>();
        }
        return interfacesStrings
                .stream()
                .map(ConnectionInterface::new)
                .toList();
    }

    private List<Feature> getFeatures(List<String> featuresStrings) {
        if (featuresStrings == null) {
            featuresStrings = new ArrayList<>();
        }
        return featuresStrings
                .stream()
                .map(Feature::new)
                .toList();
    }

    private Brand getBrand(ProductItemPostRequest productItemPostRequest) {
        Brand brand;
        String brandName = productItemPostRequest.brand();
        if (!brandRepository.existsByName(brandName)) {
            brand = brandRepository.save(new Brand(brandName));
        } else {
            brand = brandRepository.findByName(brandName).orElseThrow();
        }
        return brand;
    }

    private Brand getBrand(ProductItemRequestPart itemRequestPart) {
        Brand brand;
        String brandName = itemRequestPart.brand();
        if (!brandRepository.existsByName(brandName)) {
            brand = brandRepository.save(new Brand(brandName));
        } else {
            brand = brandRepository.findByName(brandName).orElseThrow();
        }
        return brand;
    }

    private List<ImageData> getImageDataListFromMultipartFiles(List<MultipartFile> images) {
        return images.stream()
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
    }

    private ItemDetails getItemDetailsFromDto(ItemDetailsDto itemDetailsDto, List<Feature> featuresWithIds, List<ConnectionInterface> interfacesWithIds) {
        return ItemDetails.builder()
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

    private String assembleTitle(ProductItem productItem) {
        StringBuilder title = new StringBuilder();
        title.append("Монітор ")
                .append(productItem.getDetails().getDiagonal() == 0 ? "" : (productItem.getDetails().getDiagonal() + "'"))
                .append(productItem.getBrand() == null ? "" : productItem.getBrand().getName())
                .append(productItem.getModel().isEmpty() ? "" : (" (" + productItem.getModel() + ")"))
                .append(productItem.getDetails().getFrequency() == 0 ? "" : ("/ " + productItem.getDetails().getFrequency() + "Hz"))
                .append(printFeatures(productItem.getDetails().getFeatures()));
        return title.toString();
    }

    private String printFeatures(List<Feature> features) {
        if (features.isEmpty()) {
            return "";
        }
        StringBuilder featuresString = new StringBuilder();
        features.forEach(feature -> featuresString
                                        .append(" / ")
                                        .append(feature.getName()));
        return featuresString.toString();
    }

    private Function<ProductItem, ProductItemDto> productItemToDtoMapper() {
        return productItem -> new ProductItemDto(
                productItem.getId(),
                productItem.getPrice(),
                assembleTitle(productItem),
                mapToUrls(productItem.getImageDataList())
        );
    }

    private Function<ProductItem, ProductItemDetailedDto> productItemToDtoDetailsMapper() {
        return productItem -> new ProductItemDetailedDto(
                productItem.getId(),
                productItem.getPrice(),
                assembleTitle(productItem),
                mapToUrls(productItem.getImageDataList()),
                itemDetailsToDtoMapper(productItem.getDetails())
        );
    }

    private ItemDetailsDto itemDetailsToDtoMapper(ItemDetails details) {
        return ItemDetailsDto.builder()
                .warranty(details.getWarranty())
                .producingCountry(details.getProducingCountry())
                .color(details.getColor())
                .brightness(details.getBrightness())
                .diagonal(details.getDiagonal())
                .frequency(details.getFrequency())
                .maxDisplayResolution(details.getMaxDisplayResolution())
                .matrixType(details.getMatrixType())
                .interfaces(mapInterfacesToStrings(details.getInterfaces()))
                .features(mapFeaturesToStrings(details.getFeatures()))
                .build();
    }

    private List<String> mapFeaturesToStrings(List<Feature> features) {
        return features.stream()
                .map(Feature::getName)
                .toList();
    }

    private List<String> mapInterfacesToStrings(List<ConnectionInterface> interfaces) {
        return interfaces.stream()
                .map(ConnectionInterface::getName)
                .toList();
    }

    private List<String> mapToUrls(List<ImageData> imageDataList) {
        String port = urlConfig.getPORT().equals("0") ? "" : (":" + urlConfig.getPORT());
        return imageDataList.stream()
                .map(imageData ->
                        urlConfig.getHOST_URL() +
                                port +
                                urlConfig.getPATH_URL() +
                                imageData.getId() + ".jpg")
                .toList();
    }


}
