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
    private String reprintReason;

    public String getSegmentBarcodeId() {
        return segmentBarcodeId;
    }

    public void setSegmentBarcodeId(String segmentBarcodeId) {
        this.segmentBarcodeId = segmentBarcodeId;
    }

    public String getReprintReason() {
        return reprintReason;
    }

    public void setReprintReason(String reprintReason) {
        this.reprintReason = reprintReason;
    }

    public int getPrintCount() {
        return printCount;
    }

    public void setPrintCount(int printCount) {
        this.printCount = printCount;
    }

    public String getPrintStatus() {
        return printStatus;
    }

    public void setPrintStatus(String printStatus) {
        this.printStatus = printStatus;
    }
}