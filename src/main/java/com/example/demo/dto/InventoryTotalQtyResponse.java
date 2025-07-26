package com.example.demo.dto;

// InventoryTotalQtyResponse.java
public class InventoryTotalQtyResponse {
    private int totalQty;

    public InventoryTotalQtyResponse(int totalQty) {
        this.totalQty = totalQty;
    }

    public int getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(int totalQty) {
        this.totalQty = totalQty;
    }
}

