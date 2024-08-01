package com.example.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrintStatusUpdateRequest {

    private String segmentBarcodeId;
    private String printStatus;
    private int printCount;
}