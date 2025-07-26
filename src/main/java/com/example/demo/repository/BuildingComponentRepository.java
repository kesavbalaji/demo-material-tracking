package com.example.demo.repository;

import com.example.demo.entity.BuildingComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BuildingComponentRepository extends JpaRepository<BuildingComponent, Long>, JpaSpecificationExecutor<BuildingComponent> {
}

