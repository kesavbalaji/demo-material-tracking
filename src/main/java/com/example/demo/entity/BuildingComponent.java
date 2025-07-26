package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "building_components")
public class BuildingComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long slNo;

    private String block;
    private String blockShortCode;
    private String floor;
    private String floorShortCode;
    private String description;
    private String descriptionShortCode;
    private Integer qty;

    public Long getSlNo() {
        return slNo;
    }

    public void setSlNo(Long slNo) {
        this.slNo = slNo;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getBlockShortCode() {
        return blockShortCode;
    }

    public void setBlockShortCode(String blockShortCode) {
        this.blockShortCode = blockShortCode;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getFloorShortCode() {
        return floorShortCode;
    }

    public void setFloorShortCode(String floorShortCode) {
        this.floorShortCode = floorShortCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionShortCode() {
        return descriptionShortCode;
    }

    public void setDescriptionShortCode(String descriptionShortCode) {
        this.descriptionShortCode = descriptionShortCode;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
}

