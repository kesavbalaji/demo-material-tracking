package com.example.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchReportDto {

    private String segmentId;
    private String castingDateFrom;
    private String castingDateTo;
    private String location;
    private String erectionDateFrom;
    private String erectionDateTo;
    private String dispatchId;

    public String getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(String segmentId) {
        this.segmentId = segmentId;
    }

    public String getCastingDateFrom() {
        return castingDateFrom;
    }

    public void setCastingDateFrom(String castingDateFrom) {
        this.castingDateFrom = castingDateFrom;
    }

    public String getCastingDateTo() {
        return castingDateTo;
    }

    public void setCastingDateTo(String castingDateTo) {
        this.castingDateTo = castingDateTo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getErectionDateFrom() {
        return erectionDateFrom;
    }

    public void setErectionDateFrom(String erectionDateFrom) {
        this.erectionDateFrom = erectionDateFrom;
    }

    public String getErectionDateTo() {
        return erectionDateTo;
    }

    public void setErectionDateTo(String erectionDateTo) {
        this.erectionDateTo = erectionDateTo;
    }

    public String getDispatchId() {
        return dispatchId;
    }

    public void setDispatchId(String dispatchId) {
        this.dispatchId = dispatchId;
    }
}
