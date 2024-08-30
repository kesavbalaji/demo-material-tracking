package com.example.demo.controller;


import com.example.demo.dto.*;
import com.example.demo.entity.CastingYardData;
import com.example.demo.entity.User;
import com.example.demo.service.CastingYardServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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
        List<CastingYardData> allEntities = castingYardService.getEntitiesByStatusPending();
        return allEntities.stream().sorted(Comparator.comparing(CastingYardData::getId)).collect(Collectors.toList());
    }

    @PostMapping("/api/entities/updatePrintStatus")
    public ResponseEntity<String> updateStatusAndCount(@RequestBody PrintStatusUpdateRequest entityService) throws IOException {
        castingYardService.updatePrintStatus(entityService);
        return ResponseEntity.ok("Print status updated successfully");
    }

    @PostMapping("/api/entities/reprintUpdatePrintStatus")
    public ResponseEntity<String> reprintUpdatePrintStatus(@RequestBody PrintStatusUpdateRequest entityService) throws IOException {
        castingYardService.reprintUpdatePrintStatus(entityService);
        return ResponseEntity.ok("Print status updated successfully");
    }

    @PostMapping("api/entities/checkPrintCount")
    public Boolean checkPrintCount(@RequestBody PrintStatusUpdateRequest printStatusUpdateRequest) throws IOException {
        return castingYardService.checkPrintCount(printStatusUpdateRequest.getSegmentBarcodeId(), printStatusUpdateRequest.getReprintReason());
    }

    @PostMapping("/api/search")
    public List<CastingYardData> searchRecords(@RequestBody SearchRequest searchRequest) {
        String searchRequestId = searchRequest.getId();
        LocalDate fromDate = searchRequest.getFromDate();
        LocalDate toDate = searchRequest.getToDate();
        return castingYardService.searchRecords(searchRequestId, fromDate, toDate);
    }

    @GetMapping("/api/inventorySearch")
    public List<InventoryStockDto> searchInventoryStock(@RequestParam("searchId") String searchId) {
        List<InventoryStockDto> castingYardData = castingYardService.searchByFamilyType(searchId);
        return castingYardData;
    }

    @GetMapping("/api/getSegmentData/{barcode}")
    public ResponseEntity<CastingYardData> getSegmentData(@PathVariable String barcode) {
        try {
            CastingYardData segmentData = castingYardService.getSegmentIntoBySegmentId(barcode);
            if (segmentData != null) {
                return ResponseEntity.ok(segmentData);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/api/getErectionYardData/{barcode}")
    public ResponseEntity<CastingYardData> getErectionYardData(@PathVariable String barcode) {
        try {
            CastingYardData segmentData = castingYardService.getErectionYardData(barcode);
            if (segmentData != null) {
                return ResponseEntity.ok(segmentData);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/api/getDispatchIdBySegmentId/{barcode}")
    public ResponseEntity<CastingYardData> getDispatchIdBySegmentId(@PathVariable String barcode) {
        try {

            CastingYardData dispatchIdBySegmentId = castingYardService.getDispatchIdBySegmentId(barcode);
            if (dispatchIdBySegmentId != null) {
                return ResponseEntity.ok(dispatchIdBySegmentId);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/api/getSegmentDataByQaConfirmation/{barcode}")
    public ResponseEntity<CastingYardData> getSegmentDataByQaConfirmation(@PathVariable String barcode) {
        try {
            CastingYardData segmentData = castingYardService.getSegmentDataByQaConfirmation(barcode);
            if (segmentData != null) {
                return ResponseEntity.ok(segmentData);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/api/getReceiveConfirmationByDispatchId/{dispatchId}")
    public ResponseEntity<List<CastingYardData>> getReceiveConfirmationByDispatchId(@PathVariable String dispatchId) {
        try {
            List<CastingYardData> segmentData = castingYardService.getReceiveConfirmationByDispatchId(dispatchId);
            if (segmentData != null) {
                return ResponseEntity.ok(segmentData);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/api/updateStatus")
    public ResponseEntity<String> updateStatus(@RequestBody StatusUpdateRequest request) {
        try {
            castingYardService.updateStatus(request.getSegmentIds(), request.getStatus(), request.getCastingDate(), request.getQaTest());
            return ResponseEntity.ok("Status updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while updating status");
        }
    }

    @PostMapping("/api/updateStatusAsCompleted")
    public ResponseEntity<String> updateStatusAsCompleted(@RequestBody StatusUpdateRequest request) {
        castingYardService.updateStatusAsCompleted(request.getSegmentIds(), request.getStatus());
        return ResponseEntity.ok("Status updated successfully");
    }

    @PostMapping("/api/updateDispatchId")
    public ResponseEntity<String> updateDispatchId(@RequestBody StatusUpdateRequest request) {
        try {
            castingYardService.updateDispatchId(request.getSegmentIds(), request.getDispatchId());
            return ResponseEntity.ok("Status updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while updating status");
        }
    }

    @PostMapping("/api/receiveConfirmation")
    public ResponseEntity<String> receiveConfirmation(@RequestBody StatusUpdateRequest request) {
        try {
            castingYardService.receiveConfirmation(request.getSegmentIds(), request.getDispatchId());
            return ResponseEntity.ok("Status updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while updating status");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Boolean> registerUser(@RequestBody RegisterDto registerDto) {
        boolean registerUser = castingYardService.registerUser(registerDto);
        return ResponseEntity.ok(registerUser);
    }

    @GetMapping("/api/getNextDispatchId")
    public ResponseEntity<String> getNextDispatchId() {
        String nextDispatchId = castingYardService.getNextDispatchId(); // Implement this service method
        return ResponseEntity.ok(nextDispatchId);
    }

    @PostMapping("/api/entities/search")
    public List<CastingYardData> getSegmentData(@RequestBody SearchReportDto searchReportDto) {
        return castingYardService.findEntities(searchReportDto);
    }

    @GetMapping("/api/users")
    public List<User> getAllUsers() {
        return castingYardService.findAllUsers();
    }

    @PostMapping("/api/users")
    public List<User> addUsers(@RequestBody List<User> userDto) {
        return castingYardService.addUsers(userDto);
    }

    @PutMapping("/api/users/{userId}")
    public List<User> addUsers(@PathVariable(value = "userId") Integer userId,
                               @RequestBody User users) {
        return castingYardService.updateUser(userId, users);
    }

    @DeleteMapping("/api/users/{userId}")
    public List<User> addUsers(@PathVariable(value = "userId") Integer userId) {
        return castingYardService.deleteUser(userId);
    }

    @PostMapping("/api/entities/delete")
    public void deleteSegmentId(@RequestBody CastingYardData castingYardData) {
        castingYardService.deleteSegmentId(castingYardData.getId());
    }

    @GetMapping("/api/user/access")
    public User checkAccess() {
        return castingYardService.getAccessRights();
    }

    @PostMapping("/api/getCount")
    public CountDto getCountForInventory(@RequestBody CountDto countDto) {
        return castingYardService.getCountForInventory(countDto.getCountType());
    }

    @GetMapping("/api/getCounts")
    public List<CountInfo> getAllInventoryCount() {
        return castingYardService.getAllCounts();
    }
    @PostMapping("/api/getSegmentIds")
    public List<CountDto> getSegmentIdForCount(@RequestBody CountDto countDto) {
        return castingYardService.getSegmentIdForCount(countDto.getCountType());
    }

    @PostMapping("/api/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return ResponseEntity.ok().build();
    }

}
