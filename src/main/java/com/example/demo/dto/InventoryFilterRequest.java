package com.example.demo.dto;

// InventoryFilterRequest.java
public class InventoryFilterRequest {
    private String block;
    private String floor;
    private String description;

    // Getters and Setters
    public String getBlock() {
        return block;
    }
    public void setBlock(String block) {
        this.block = block;
    }

    public String getFloor() {
        return floor;
    }
    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}

