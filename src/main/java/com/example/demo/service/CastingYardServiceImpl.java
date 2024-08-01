package com.example.demo.service;


import com.example.demo.dto.InventoryStockDto;
import com.example.demo.dto.PrintStatusUpdateRequest;
import com.example.demo.entity.CastingYardData;
import com.example.demo.repository.CastingYardDetailsRepository;
import io.micrometer.common.util.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Service
public class CastingYardServiceImpl {

    @Autowired
    private CastingYardDetailsRepository castingYardDetailsRepository;

    public List<CastingYardData> getAllEntities() {
        return castingYardDetailsRepository.findAll();
    }

    public List<CastingYardData> getEntitiesByStatusPending() {
        return castingYardDetailsRepository.getEntitiesByStatusPending();
    }

    public void updatePrintStatus(PrintStatusUpdateRequest printStatusUpdateRequest) {
        String segmentBarcodeId = printStatusUpdateRequest.getSegmentBarcodeId();
        int printCount = castingYardDetailsRepository.getPrintCount(segmentBarcodeId);
        int updatedCount = printCount + 1;
        castingYardDetailsRepository.updateStatusAndCount(segmentBarcodeId, updatedCount);
    }

    public Boolean checkPrintCount(String segmentId) {
        Integer printCount = castingYardDetailsRepository.getPrintCount(segmentId);
        if (printCount == 0)
            return Boolean.TRUE;
        return Boolean.FALSE;
    }

    public List<CastingYardData> searchRecords(String id, LocalDate fromDate, LocalDate toDate) {
        return castingYardDetailsRepository.findByIdAndDateBetween(id, fromDate, toDate);
    }

    public List<InventoryStockDto> searchByFamilyType(String familyType) {
        List<InventoryStockDto> byFamilyType = castingYardDetailsRepository.findByFamilyType(familyType);
        return byFamilyType;
    }

    public String writeIntoDB(MultipartFile file) throws IOException {
        String jdbcURL = "jdbc:postgresql://localhost:5432/postgres?currentSchema=public";
        String username = "postgres";
        String password = "postgres";
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream());
             Connection connection = DriverManager.getConnection(jdbcURL, username, password)) {

            Sheet sheet = workbook.getSheetAt(0);
            String sql = "INSERT INTO public.casting_yard_details(Segment_Barcode_ID, Casting_Date, Location,Reference_Level,Family,Family_Type,Description,Mark,Type,Length,Count,LEFT_CORBEL_DISTANCE,RIGHT_CORBEL_DISTANCE,Volume,print_status,print_count) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Skip header row
                }

                try (PreparedStatement statement = connection.prepareStatement(sql)) {
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

                    statement.setString(1, cell1.getStringCellValue());
                    statement.setString(2, cell2.toString());
                    statement.setString(3, cell3.getStringCellValue());
                    statement.setString(4, cell4.getStringCellValue());
                    statement.setString(5, cell5.getStringCellValue());
                    statement.setString(6, cell6.getStringCellValue());
                    statement.setString(7, cell7.getStringCellValue());
                    statement.setString(8, cell8.getStringCellValue());
                    statement.setString(9, cell9.getStringCellValue());
                    statement.setString(10, String.valueOf(cell10.getNumericCellValue()));
                    statement.setString(11, String.valueOf(cell11.getNumericCellValue()));
                    if (cell12 != null && cell12.getCellType() != CellType.BLANK && StringUtils.isNotBlank(cell12.toString()))
                        statement.setString(12, String.valueOf(cell12.getNumericCellValue()));
                    else
                        statement.setString(12, "");
                    if (cell13 != null && cell13.getCellType() != CellType.BLANK && StringUtils.isNotBlank(cell13.toString()))
                        statement.setString(13, String.valueOf(cell13.getNumericCellValue()));
                    else
                        statement.setString(13, "");
                    if (cell14 != null && cell14.getCellType() != CellType.BLANK && StringUtils.isNotBlank(cell14.toString()))
                        statement.setString(14, String.valueOf(cell14.getNumericCellValue()));
                    else
                        statement.setString(14, "");
                    statement.setString(15, "PENDING");
                    statement.setInt(16, 0);

                    statement.executeUpdate();
                } catch (SQLException e) {
                    // Check if the exception is due to a unique key violation
                    if (e.getSQLState().equals("23505")) { // PostgreSQL unique key violation
                        System.out.println("Duplicate entry found: " + row.getCell(0).getStringCellValue() + " - Skipping this row.");
                    } else {
                        throw e; // If it's a different SQL exception, rethrow it
                    }
                }
            }

            workbook.close();
            return "File uploaded and data inserted successfully.";

        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return "File upload failed: " + e.getMessage();
        }

    }
}
