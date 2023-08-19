package com.spro.pcshop.repository;

import com.spro.pcshop.entity.ConnectionInterface;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnectionInterfaceRepository extends
        JpaRepository<ConnectionInterface, Long> {
}
