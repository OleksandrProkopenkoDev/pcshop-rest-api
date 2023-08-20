package com.spro.pcshop.repository;

import com.spro.pcshop.entity.ConnectionInterface;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConnectionInterfaceRepository extends
        JpaRepository<ConnectionInterface, Long> {
    Optional<ConnectionInterface> findByName(String name);

    boolean existsByName(String name);
}
