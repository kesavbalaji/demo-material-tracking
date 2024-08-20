package com.example.demo.service;


import com.example.demo.dto.*;
import com.example.demo.entity.CastingYardData;
import com.example.demo.entity.User;
import com.example.demo.repository.CastingYardDetailsRepository;
import com.example.demo.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CastingYardServiceImpl {

    @Autowired
    private CastingYardDetailsRepository castingYardDetailsRepository;

    @Autowired
    private UserRepository userRepository;

    public List<CastingYardData> getAllEntities() {
        return castingYardDetailsRepository.findAll();
    }

    public List<CastingYardData> getEntitiesByStatusPending() {
        return castingYardDetailsRepository.getEntitiesByStatusPending();
    }

    @Transactional
    public void updatePrintStatus(PrintStatusUpdateRequest printStatusUpdateRequest) {
        String segmentBarcodeId = printStatusUpdateRequest.getSegmentBarcodeId();
        int printCount = castingYardDetailsRepository.getPrintCount(segmentBarcodeId);
        int updatedCount = printCount + 1;
        castingYardDetailsRepository.updateStatusAndCount(segmentBarcodeId, updatedCount);
    }

    @Transactional
    public void reprintUpdatePrintStatus(PrintStatusUpdateRequest printStatusUpdateRequest) {
        String segmentBarcodeId = printStatusUpdateRequest.getSegmentBarcodeId();
        int printCount = castingYardDetailsRepository.getPrintCount(segmentBarcodeId);
        int updatedCount = printCount + 1;
        castingYardDetailsRepository.reprintUpdatePrintStatus(segmentBarcodeId, updatedCount);
    }

    @Transactional
    public Boolean checkPrintCount(String segmentId, String reason) {
        Integer printCount = castingYardDetailsRepository.getPrintCount(segmentId);
        String currentUsername = getCurrentUsername();
        Optional<User> user = userRepository.getUser(currentUsername);
        if (user.get().getAccessRights().equals("Admin")) {
            int updateReprintReason = castingYardDetailsRepository.updateReprintReason(reason, segmentId);
            return Boolean.TRUE;
        } else if (printCount == 0) {
            int updateReprintReason = castingYardDetailsRepository.updateReprintReason(reason, segmentId);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public List<CastingYardData> searchRecords(String id, LocalDate fromDate, LocalDate toDate) {
        return castingYardDetailsRepository.findByIdAndDateBetween(id, fromDate, toDate);
    }

    public List<InventoryStockDto> searchByFamilyType(String familyType) {
        return castingYardDetailsRepository.findByFamilyType(familyType);
    }

    public CastingYardData getSegmentIntoBySegmentId(String segmentId) {
        return castingYardDetailsRepository.getDataBySegmentId(segmentId);
    }

    public CastingYardData getErectionYardData(String segmentId) {
        return castingYardDetailsRepository.getErectionYardData(segmentId);
    }

    public CastingYardData getDispatchIdBySegmentId(String segmentId) {
        return castingYardDetailsRepository.getDispatchIdBySegmentId(segmentId);
    }

    public CastingYardData getSegmentDataByQaConfirmation(String segmentId) {
        return castingYardDetailsRepository.getSegmentDataByQaConfirmation(segmentId);
    }

    public List<CastingYardData> getReceiveConfirmationByDispatchId(String dispatchId) {
        return castingYardDetailsRepository.getReceiveConfirmationByDispatchId(dispatchId);
    }

    public boolean updateStatus(List<String> segmentIds, String status, String castingDate, String qaTest) {
        String formattedDateStr = null;
        if (castingDate != null) {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
            LocalDate date = LocalDate.parse(castingDate, inputFormatter);
            formattedDateStr = date.format(outputFormatter);
        }
        String currentUsername = getCurrentUsername();
        int updatedCount = castingYardDetailsRepository.updateStatusForSegments(segmentIds, status, currentUsername, formattedDateStr, qaTest);
        return updatedCount == segmentIds.size();
    }

    public boolean updateStatusAsCompleted(List<String> segmentIds, String status) {
        String currentUsername = getCurrentUsername();
        int updatedCount = castingYardDetailsRepository.updateStatusAsCompleted(segmentIds, status, currentUsername);
        return updatedCount == segmentIds.size();
    }

    public boolean updateDispatchId(List<String> segmentIds, String dispatchId) {
        int updatedCount = castingYardDetailsRepository.updateDispatchIdForSegmentId(segmentIds, dispatchId);
        return updatedCount == segmentIds.size();
    }

    public boolean receiveConfirmation(List<String> segmentIds, String dispatchId) {
        String currentUsername = getCurrentUsername();
        int updatedCount = castingYardDetailsRepository.updateReceiveConfirmation(dispatchId, currentUsername);
        return updatedCount == segmentIds.size();
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                return ((UserDetails) principal).getUsername();
            } else {
                return principal.toString();
            }
        }
        return null;
    }

    public boolean registerUser(RegisterDto registerDto) {
        Optional<User> user = userRepository.getUser(registerDto.getAdminUsername());
        if (user.isPresent()) {
            User user1 = new User();
            user1.setUsername(registerDto.getUsername());
            user1.setAccessRights("User");
            user1.setPassword(registerDto.getPassword());
            userRepository.save(user1);
            return true;
        } else
            return false;
    }

    public String writeIntoDB(MultipartFile file) throws IOException {
        String jdbcURL = "jdbc:postgresql://localhost:5432/postgres?currentSchema=public";
        String username = "postgres";
        String password = "postgres";

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream());
             Connection connection = DriverManager.getConnection(jdbcURL, username, password)) {

            Sheet sheet = workbook.getSheetAt(0);
            String insertSql = "INSERT INTO public.casting_yard_details(Segment_Barcode_ID, Casting_Date, Location,Reference_Level,Family,Family_Type,Description,Mark,Type,Length,Count,LEFT_CORBEL_DISTANCE,RIGHT_CORBEL_DISTANCE,Volume,print_status,print_count, location_status) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            String updateSql = "UPDATE public.casting_yard_details SET Segment_Barcode_ID = ?, Casting_Date = ?, Location = ?, Reference_Level = ?, Family = ?, Family_Type = ?, Description = ?, Mark = ?, Type = ?, Length = ?, Count = ?, LEFT_CORBEL_DISTANCE = ?, RIGHT_CORBEL_DISTANCE = ?, Volume = ?, print_status = ?, print_count = ?, location_status = ? WHERE Segment_Barcode_ID = ?";

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Skip header row
                }

                String segmentBarcodeId = row.getCell(0).getStringCellValue();
                try (PreparedStatement insertStatement = connection.prepareStatement(insertSql);
                     PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {

                    // Fill in the insert statement parameters
                    fillPreparedStatement(insertStatement, row);
                    insertStatement.executeUpdate();

                } catch (SQLException e) {
                    // Check if the exception is due to a unique key violation
                    if (e.getSQLState().equals("23505")) { // PostgreSQL unique key violation
                        System.out.println("Duplicate entry found: " + segmentBarcodeId + " - Updating this row instead.");
                        PreparedStatement updateStatement = connection.prepareStatement(updateSql);
                        // Fill in the update statement parameters
                        fillPreparedStatement(updateStatement, row);
                        updateStatement.setString(17, "CASTING YARD");
                        updateStatement.setString(18, segmentBarcodeId);// Set the Segment_Barcode_ID for the WHERE clause
                        updateStatement.executeUpdate();
                    } else {
                        throw e; // If it's a different SQL exception, rethrow it
                    }
                }
            }

            workbook.close();
            return "File uploaded and data inserted/updated successfully.";

        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return "File upload failed: " + e.getMessage();
        }
    }

    private void fillPreparedStatement(PreparedStatement statement, Row row) throws SQLException {
        Cell cell1 = row.getCell(0);
        Cell cell2 = row.getCell(1);
        Cell cell3 = row.getCell(2);
        Cell cell4 = row.getCell(3);
        Cell cell5 = row.getCell(4);
        Cell cell6 = row.getCell(5);
        Cell cell7 = row.getCell(6);
        Cell cell8 = row.getCell(7);
        Cell cell9 = row.getCell(8);
        Cell cell10 = row.getCell(9);
        Cell cell11 = row.getCell(10);
        Cell cell12 = row.getCell(11);
        Cell cell13 = row.getCell(12);
        Cell cell14 = row.getCell(13);

        statement.setString(1, String.valueOf(cell1));
        statement.setString(2, cell2 != null && cell2.getCellType() != CellType.BLANK && StringUtils.isNotBlank(cell2.toString()) ? cell2.toString() : "");
        statement.setString(3,  String.valueOf(cell3));
        statement.setString(4,  String.valueOf(cell4));
        statement.setString(5,  String.valueOf(cell5));
        statement.setString(6,  String.valueOf(cell6));
        statement.setString(7, cell7 != null && cell7.getCellType() != CellType.BLANK && StringUtils.isNotBlank(cell7.toString()) ? String.valueOf(cell7) : "");
        statement.setString(8,  String.valueOf(cell8));
        statement.setString(9,  String.valueOf(cell9));
        statement.setString(10, String.valueOf(cell10));
        statement.setString(11, String.valueOf(cell11));

        statement.setString(12, cell12 != null && cell12.getCellType() != CellType.BLANK && StringUtils.isNotBlank(cell12.toString()) ? String.valueOf(cell12.getNumericCellValue()) : "");
        statement.setString(13, cell13 != null && cell13.getCellType() != CellType.BLANK && StringUtils.isNotBlank(cell13.toString()) ? String.valueOf(cell13.getNumericCellValue()) : "");
        statement.setString(14, cell14 != null && cell14.getCellType() != CellType.BLANK && StringUtils.isNotBlank(cell14.toString()) ? String.valueOf(cell14.getNumericCellValue()) : "");

        statement.setString(15, "PENDING");

        // For print_count, change to setInt instead of setString
        statement.setInt(16, 0);

        statement.setString(17, "CASTING YARD");
    }


    public String getNextDispatchId() {

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMyy");
        String datePrefix = today.format(formatter);
        List<String> ids = castingYardDetailsRepository.findLastDispatchId("DISP" + datePrefix);

        String lastId = ids.isEmpty() ? null : ids.get(0);
        int nextNumber = 1;
        if (lastId != null) {
            String numberPart = lastId.substring(lastId.length() - 3);
            nextNumber = Integer.parseInt(numberPart) + 1;
        }

        return String.format("DISP%s-%03d", datePrefix, nextNumber);
    }

    public List<CastingYardData> findEntities(SearchReportDto searchReportDto) {
        SimpleDateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormatter = new SimpleDateFormat("dd-MMM-yyyy");

        try {
            if (searchReportDto.getCastingDateFrom() != null && !searchReportDto.getCastingDateFrom().isEmpty()) {
                Date castingDateFrom = inputFormatter.parse(searchReportDto.getCastingDateFrom());
                Date castingDateTo = inputFormatter.parse(searchReportDto.getCastingDateTo());
                String formattedCastingDateFrom = outputFormatter.format(castingDateFrom);
                String formattedCastingDateTo = outputFormatter.format(castingDateTo);
                return castingYardDetailsRepository.findReportsByCastingDate(formattedCastingDateFrom, formattedCastingDateTo);
            } else if (searchReportDto.getErectionDateFrom() != null && !searchReportDto.getErectionDateFrom().isEmpty()) {
                return castingYardDetailsRepository.findReportsByErectedDate(searchReportDto.getErectionDateFrom(), searchReportDto.getErectionDateTo());
            } else if (searchReportDto.getSegmentId() != null && !searchReportDto.getSegmentId().isEmpty()) {
                return castingYardDetailsRepository.findReportByEntities(searchReportDto.getSegmentId());
            } else if (searchReportDto.getDispatchId() != null && !searchReportDto.getDispatchId().isEmpty()) {
                return castingYardDetailsRepository.findByDispatchId(searchReportDto.getDispatchId());
            } else if (searchReportDto.getLocation() != null && !searchReportDto.getLocation().isEmpty()) {
                return castingYardDetailsRepository.findByLocation(searchReportDto.getLocation());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public List<User> addUsers(List<User> userDto) {
        String currentUsername = getCurrentUsername();
        Optional<User> user = userRepository.getUser(currentUsername);
        if (user.get().getAccessRights().equals("Admin")) {
            return userRepository.saveAll(userDto);
        }
        return new ArrayList<>();
    }

    @Transactional
    public List<User> updateUser(Integer id, User user) {
        String currentUsername = getCurrentUsername();
        Optional<User> users = userRepository.getUser(currentUsername);
        if (users.get().getAccessRights().equals("Admin")) {
            int updateUserModifying = userRepository.updateUserModifying(user.getUsername(), user.getPassword(), user.getAccessRights(), id);
            return userRepository.findAll();
        }
        return new ArrayList<>();
    }

    @Transactional
    public List<User> deleteUser(Integer id) {
        String currentUsername = getCurrentUsername();
        Optional<User> users = userRepository.getUser(currentUsername);
        if (users.get().getAccessRights().equals("Admin")) {
            int updateUserModifying = userRepository.deleteUser(id);
            return userRepository.findAll();
        }
        return new ArrayList<>();
    }

    @Transactional
    public void deleteSegmentId(Integer id) {
        int updateUserModifying = castingYardDetailsRepository.deleteSegment(id);
    }

    public User getAccessRights() {
        String currentUsername = getCurrentUsername();
        Optional<User> user = userRepository.getUser(currentUsername);
        return user.get();
    }

    public CountDto getCountForInventory(String selectType) {
        CountDto countDto = new CountDto();
        if (selectType.equals("Total Segment Count")) {
            int countForInventory = castingYardDetailsRepository.getCountForInventory();
            return setCountReport(countDto, countForInventory);
        } else if (selectType.equals("Total Printed Count")) {
            int countForInventory = castingYardDetailsRepository.getPrintedCount();
            return setCountReport(countDto, countForInventory);
        } else if (selectType.equals("Total Pending Count")) {
            int countForInventory = castingYardDetailsRepository.getPendingCount();
            return setCountReport(countDto, countForInventory);
        } else if (selectType.equals("Total QA Confirmed")) {
            int countForInventory = castingYardDetailsRepository.getQAConfirmedCount();
            return setCountReport(countDto, countForInventory);
        } else if (selectType.equals("Total Dispatch Count")) {
            int countForInventory = castingYardDetailsRepository.getDispatchCount();
            return setCountReport(countDto, countForInventory);
        } else if (selectType.equals("Erection Confirm Count")) {
            int countForInventory = castingYardDetailsRepository.getErectionYardCount();
            return setCountReport(countDto, countForInventory);
        } else if (selectType.equals("Erection Completed Count")) {
            int countForInventory = castingYardDetailsRepository.getErectionCompletedCount();
            return setCountReport(countDto, countForInventory);
        } else if (selectType.equals("RePrinted Segment")) {
            int countForInventory = castingYardDetailsRepository.getReprintCount();
            return setCountReport(countDto, countForInventory);
        }
        return new CountDto();
    }

    private static CountDto setCountReport(CountDto countDto, int countForInventory) {
        countDto.setCount(countForInventory);
        return countDto;
    }

    public List<CountDto> getSegmentIdForCount(String selectType) {
        List<CountDto> segmentIds = new ArrayList<>();
        if (selectType.equals("Total Segment Count")) {
            List<CastingYardData> segmentIdsForCount = castingYardDetailsRepository.getSegmentIdsForCount();
            populateValue(segmentIdsForCount, segmentIds);
        } else if (selectType.equals("Total Printed Count")) {
            List<CastingYardData> segmentIdsForCount = castingYardDetailsRepository.getPrintedCountList();
            populateValue(segmentIdsForCount, segmentIds);
        } else if (selectType.equals("Total Pending Count")) {
            List<CastingYardData> segmentIdsForCount = castingYardDetailsRepository.getPendingCountList();
            populateValue(segmentIdsForCount, segmentIds);
        } else if (selectType.equals("Total QA Confirmed")) {
            List<CastingYardData> segmentIdsForCount = castingYardDetailsRepository.getQAConfirmedCountList();
            populateValue(segmentIdsForCount, segmentIds);
        } else if (selectType.equals("Total Dispatch Count")) {
            List<CastingYardData> segmentIdsForCount = castingYardDetailsRepository.getDispatchCountList();
            populateValue(segmentIdsForCount, segmentIds);
        } else if (selectType.equals("Erection Confirm Count")) {
            List<CastingYardData> segmentIdsForCount = castingYardDetailsRepository.getErectionYardCountList();
            populateValue(segmentIdsForCount, segmentIds);
        } else if (selectType.equals("Erection Completed Count")) {
            List<CastingYardData> segmentIdsForCount = castingYardDetailsRepository.getErectionCompleted();
            populateValue(segmentIdsForCount, segmentIds);
        } else if (selectType.equals("RePrinted Segment")) {
            List<CastingYardData> segmentIdsForCount = castingYardDetailsRepository.getReprintSegmentId();
            populateValue(segmentIdsForCount, segmentIds);
        }

        return segmentIds;
    }

    private static void populateValue(List<CastingYardData> segmentIdsForCount, List<CountDto> segmentIds) {
        for (CastingYardData data : segmentIdsForCount) {
            CountDto countDto = new CountDto();
            countDto.setSegmentId(data.getSegmentBarcodeId());
            countDto.setStatus(data.getPrintStatus());
            segmentIds.add(countDto);
        }
    }

}
