package com.spro.pcshop.servise;

import com.spro.pcshop.dto.ItemDetailsDto;
import com.spro.pcshop.dto.ProductItemPostRequest;
import com.spro.pcshop.dto.ProductItemRequestPart;
import com.spro.pcshop.entity.ConnectionInterface;
import com.spro.pcshop.entity.Feature;
import com.spro.pcshop.entity.Image;
import com.spro.pcshop.entity.ImageData;
import com.spro.pcshop.repository.ConnectionInterfaceRepository;
import com.spro.pcshop.repository.FeatureRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.spro.pcshop.util.ImageUtils.compressImage;
import static com.spro.pcshop.util.ImageUtils.readFile;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProductItemServiceTest {

    @Autowired
    private FeatureRepository featureRepository;
    @Autowired
    private ConnectionInterfaceRepository connectionInterfaceRepository;
    @Autowired
    private ProductItemService underTest;


    @Test
    void addNewProductItemWithImagesFiles_shouldSaveProductItem(){
        List<MultipartFile> multipartFiles = new ArrayList<>();
        List<File> files = List.of(
                new File("src/main/resources/images/monitors/hp_9FM22AA/175135466.jpg"),
                new File("src/main/resources/images/monitors/hp_9FM22AA/175135468.jpg"),
                new File("src/main/resources/images/monitors/hp_9FM22AA/175135469.jpg"),
                new File("src/main/resources/images/monitors/hp_9FM22AA/175135470.jpg")

        );
        List<Image> imageList = new ArrayList<>();
        files.forEach(file -> imageList.add(
                Image.builder()
                        .isPrimary(false)
                        .imageData(new ImageData(compressImage(readFile(file))))
                        .type("jpg")
                        .name(file.getName())
                        .build()
        ));
        imageList.forEach(image -> multipartFiles.add(new MockMultipartFile(
                image.getName(),
                image.getName(),
                image.getType(),
                image.getImageData().getBytes()
        )));

        List<String> interfaces = List.of(
                "VGA", "HDMI", "DisplayPort"
        );
        List<String> features = List.of(
                "Flicker-Free","Безрамковий (Сinema screen)","Вигнутий екран"
        );
        ItemDetailsDto itemDetailsDto = new ItemDetailsDto(
                12, "China", "red", 600, 23.8,
                60,"1920*1080 FullHd", "TN", interfaces, features
        );
        ProductItemRequestPart itemRequestPart = new ProductItemRequestPart(
                4500,"2517ssd", "HP", itemDetailsDto
        );
        String response = underTest.addNewProductItem(itemRequestPart, multipartFiles);

        assertThat(response).startsWith("ProductItem successfully saved to DB with id");
    }

    @Test
    void addNewProductItemWithImagesUrls_shouldSaveProductItem(){
        List<String> interfaces = List.of(
                "VGA", "HDMI", "DisplayPort"
        );
        List<String> features = List.of(
                "Flicker-Free","Безрамковий (Сinema screen)","Вигнутий екран"
        );
        ItemDetailsDto itemDetailsDto = new ItemDetailsDto(
                12, "China", "red", 600, 23.8,
                60,"1920*1080 FullHd", "TN", interfaces, features
        );
        List<String> imagesUrls = List.of(
                "https://content1.rozetka.com.ua/goods/images/original/175135466.jpg",
                "https://content1.rozetka.com.ua/goods/images/original/175135469.jpg",
                "https://content2.rozetka.com.ua/goods/images/original/175135470.jpg"
        );
        ProductItemPostRequest itemPostRequest = new ProductItemPostRequest(
                4500,"2517ssd", "HP", imagesUrls, itemDetailsDto
        );
        String response = underTest.addNewProductItem(itemPostRequest);

        assertThat(response).startsWith("ProductItem successfully saved to DB with id");
    }
    @Test
    void saveImageDataList_shouldSaveList() {
        List<File> files = List.of(
                new File("src/main/resources/images/monitors/hp_9FM22AA/175135466.jpg"),
                new File("src/main/resources/images/monitors/hp_9FM22AA/175135468.jpg"),
                new File("src/main/resources/images/monitors/hp_9FM22AA/175135469.jpg"),
                new File("src/main/resources/images/monitors/hp_9FM22AA/175135470.jpg")

        );
        List<Image> imageList = new ArrayList<>();
        files.forEach(file -> imageList.add(
                Image.builder()
                        .isPrimary(false)
                        .imageData(new ImageData(compressImage(readFile(file))))
                        .type("jpg")
                        .name(file.getName())
                        .build()
        ));
        List<Image> savedImageList = underTest.saveImageDataList(imageList);

        assertThat(savedImageList).hasSize(4);
    }

    @Test
    void saveInterfaces_shouldSaveList() {
        List<ConnectionInterface> interfaces = List.of(
                new ConnectionInterface("VGA"),
                new ConnectionInterface("Display port"),
                new ConnectionInterface("HDMI")
        );
        interfaces.forEach(connectionInterface -> {
            Optional<ConnectionInterface> optionalConnectionInterface =
                    connectionInterfaceRepository.findByName(connectionInterface.getName());
            optionalConnectionInterface.ifPresent(value -> connectionInterfaceRepository.delete(value));
        });
        // When
        List<ConnectionInterface> savedInterfaces = underTest.saveInterfaces(interfaces);

        // Then
        assertThat(savedInterfaces).hasSize(3);
    }

    @Test
    void saveFeatures_shouldSaveNewFeatures() {
        // Given
        List<Feature> features = List.of(
                new Feature("AMD FreeSync Premium"),
                new Feature("Adaptive Sync")
        );
        features.forEach(feature -> {
            Optional<Feature> optionalFeature = featureRepository.findByName(feature.getName());
            optionalFeature.ifPresent(value -> featureRepository.delete(value));
        });
        // When
        List<Feature> savedFeatures = underTest.saveFeatures(features);

        // Then
        assertThat(savedFeatures).hasSize(2);
    }
}