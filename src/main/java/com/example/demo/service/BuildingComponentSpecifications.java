package com.example.demo.service;

import com.example.demo.entity.BuildingComponent;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class BuildingComponentSpecifications {

    public static Specification<BuildingComponent> withFilters(String block, String floor, String description) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (block != null && !block.isEmpty()) {
                predicates.add(builder.equal(root.get("blockShortCode"), block));
            }
            if (floor != null && !floor.isEmpty()) {
                predicates.add(builder.equal(root.get("floorShortCode"), floor));
            }
            if (description != null && !description.isEmpty()) {
                predicates.add(builder.equal(root.get("description"), description));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
