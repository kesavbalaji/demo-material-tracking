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
}
