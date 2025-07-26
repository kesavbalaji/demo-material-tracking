package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;



@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatusUpdateRequest {

    private List<String> segmentIds;

    private String status;

    private String castingDate;

    private String dispatchId;

    private String qaTest;

    public List<String> getSegmentIds() {
        return segmentIds;
    }

    public void setSegmentIds(List<String> segmentIds) {
        this.segmentIds = segmentIds;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCastingDate() {
        return castingDate;
    }

    public void setCastingDate(String castingDate) {
        this.castingDate = castingDate;
    }

    public String getDispatchId() {
        return dispatchId;
    }

    public void setDispatchId(String dispatchId) {
        this.dispatchId = dispatchId;
    }

    public String getQaTest() {
        return qaTest;
    }

    public void setQaTest(String qaTest) {
        this.qaTest = qaTest;
    }
}

