package com.spro.pcshop.repository;

import com.spro.pcshop.entity.Feature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeatureRepository extends JpaRepository<Feature, Long> {
    boolean existsByName(String name);

    Optional<Feature> findByName(String name);
}
