package com.example.demo.service;

import com.example.demo.entity.BuildingComponent;
import com.example.demo.repository.BuildingComponentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    @Autowired
    private BuildingComponentRepository repository;

    public int getTotalQty(String block, String floor, String description) {
        Specification<BuildingComponent> spec = BuildingComponentSpecifications.withFilters(block, floor, description);

        return repository.findAll(spec)
                .stream()
                .mapToInt(BuildingComponent::getQty)
                .sum();
    }
}
