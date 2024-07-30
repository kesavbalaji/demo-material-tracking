package com.example.demo.controller;


import com.example.demo.dto.InventoryStockDto;
import com.example.demo.dto.SearchRequest;
import com.example.demo.entity.CastingYardData;
import com.example.demo.service.CastingYardServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MainController {

    @Autowired
    private CastingYardServiceImpl castingYardService;

    @PostMapping("/api/entities/upload")
    public List<CastingYardData> handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty())
            throw new RuntimeException("File is empty...");
        castingYardService.writeIntoDB(file);
        List<CastingYardData> allEntities = castingYardService.getAllEntities();
        return allEntities.stream().sorted(Comparator.comparing(CastingYardData::getId)).collect(Collectors.toList());
    }

    @PostMapping("/api/search")
    public List<CastingYardData> searchRecords(@RequestBody SearchRequest searchRequest) {
        String searchRequestId = searchRequest.getId();
        LocalDate fromDate = searchRequest.getFromDate();
        LocalDate toDate = searchRequest.getToDate();
        return castingYardService.searchRecords(searchRequestId, fromDate, toDate);
    }

    @GetMapping("api/inventorySearch")
    public List<InventoryStockDto> searchInventoryStock(@RequestParam("searchId") String searchId) {
        List<InventoryStockDto> castingYardData = castingYardService.searchByFamilyType(searchId);
        return castingYardData;
    }
}
