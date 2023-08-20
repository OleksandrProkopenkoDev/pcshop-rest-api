package com.spro.pcshop.repository;

import com.spro.pcshop.entity.Feature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FeatureRepositoryTest {

    @Autowired
    private FeatureRepository underTest;

    @Test
    void save_shouldSaveNewFeature(){
        String name = "new feature";
        Feature feature = new Feature(name);
        Optional<Feature> optionalFeature = underTest.findByName(name);
        optionalFeature.ifPresent(value -> underTest.delete(value));
        Feature saved = underTest.save(feature);
        assertThat(saved).isNotNull();
    }
    @Test
    void existByName_shouldReturnTrue(){
        String name = "Flicker-Free";
        Optional<Feature> optionalFeature = underTest.findByName(name);
        if(optionalFeature.isEmpty()){
            underTest.save(new Feature(name));
        }
        boolean existsByName = underTest.existsByName(name);
        assertTrue(existsByName);
    }
    @Test
    void existByName_shouldReturnFalse(){
        String name = "not exists";
        boolean existsByName = underTest.existsByName(name);
        assertFalse(existsByName);

    }
}