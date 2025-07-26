package com.example.demo.repository;


import com.example.demo.dto.CountDto;
import com.example.demo.dto.InventoryStockDto;
import com.example.demo.entity.CastingYardData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface CastingYardDetailsRepository extends JpaRepository<CastingYardData, Long> {

    @Query(value = "select * from public.casting_yard_details_hostel cyd where cyd.segment_barcode_id = ?1 and date(cyd.casting_date) between ?2 and ?3 ", nativeQuery = true)
    List<CastingYardData> findByIdAndDateBetween(String id, LocalDate fromDate, LocalDate toDate);


    @Query(value = "SELECT DISTINCT family_type as familyType, count(*) as familyCount FROM casting_yard_details group by family_type", nativeQuery = true)
    List<InventoryStockDto> findByFamilyType(String familyType);


    @Query(value = "select * from public.casting_yard_details_hostel cyd where cyd.print_status='PENDING'", nativeQuery = true)
    List<CastingYardData> getEntitiesByStatusPending();

    @Modifying
    @Query("UPDATE CastingYardData SET printStatus = 'PRINTED', printCount = ?2 WHERE segmentBarcodeId = ?1")
    int updateStatusAndCount(String segmentId, Integer printCount);

    @Modifying
    @Query("UPDATE CastingYardData SET printCount = ?2 WHERE segmentBarcodeId = ?1")
    int reprintUpdatePrintStatus(String segmentId, Integer printCount);

    @Modifying
    @Query("UPDATE CastingYardData SET printCount = ?2, printStatus = 'PRINTED' WHERE segmentBarcodeId = ?1")
    int reprintUpdatePrintStatusAndStatus(String segmentId, Integer printCount);

//    @Query(value = "update public.casting_yard_details cyd set print_status = 'PRINTED', print_count = ?2 where segment_barcode_id = ?1", nativeQuery = true)
//    int updateStatusAndCount(String segmentId, Integer printCount);

    @Query(value = "select print_count from public.casting_yard_details_hostel cyd where segment_barcode_id = ?1", nativeQuery = true)
    Integer getPrintCount(String segmentId);

    @Query(value = "select * from public.casting_yard_details_hostel cyd where segment_barcode_id = ?1 and print_status = 'PRINTED'", nativeQuery = true)
    CastingYardData getDataBySegmentId(String segmentId);

    @Query(value = "select * from public.casting_yard_details_hostel cyd where segment_barcode_id = ?1 and location_status = 'ERECTION YARD'", nativeQuery = true)
    CastingYardData getErectionYardData(String segmentId);

    @Query(value = "select * from public.casting_yard_details_hostel cyd where segment_barcode_id = ?1 and dispatch_id is null and qatest = 'Approved'", nativeQuery = true)
    CastingYardData getSegmentDataByQaConfirmation(String segmentId);

    @Query(value = "select * from public.casting_yard_details_hostel cyd where segment_barcode_id = ?1", nativeQuery = true)
    CastingYardData getDispatchIdBySegmentId(String segmentId);

    @Query(value = "select * from public.casting_yard_details_hostel cyd where dispatch_id = ?1", nativeQuery = true)
    List<CastingYardData> getReceiveConfirmationByDispatchId(String dispatchId);

    @Query(value = "update public.casting_yard_details_hostel set print_status = ?2, updated_by = ?3, created_date = now(), casting_date = ?4, qatest = ?5 where segment_barcode_id in (?1)", nativeQuery = true)
    int updateStatusForSegments(List<String> segmentIds, String status, String userName, String castingDate, String qatest);

    @Query(value = "update public.casting_yard_details_hostel set print_status = ?2, updated_by = ?3, created_date = now() where segment_barcode_id in (?1)", nativeQuery = true)
    int updateStatusAsCompleted(List<String> segmentIds, String status, String userName);

//    @Modifying
//    @Query("UPDATE CastingYardData SET dispatchId = :dispatchId , segmentBarcodeId IN :segmentIds")
//    int updateDispatchIdForSegmentId(@Param("segmentIds")  List<String> segmentIds, @Param("dispatchId") String dispatchId);


    @Query(value = "update public.casting_yard_details_hostel set dispatch_id = ?2 where segment_barcode_id in (?1)", nativeQuery = true)
    int updateDispatchIdForSegmentId(List<String> segmentIds,  String dispatchId);

    @Query(value = "update public.casting_yard_details_hostel set location_status = 'ERECTION YARD', updated_by = ?2, created_date = now() where dispatch_id = ?1 ", nativeQuery = true)
    int updateReceiveConfirmation(String dispatchId, String userName);;

    @Query("SELECT d.dispatchId FROM CastingYardData d WHERE d.dispatchId LIKE :prefix% ORDER BY d.dispatchId DESC")
    List<String> findLastDispatchId(@Param("prefix") String prefix);


    @Query(value = "select * from casting_yard_details_hostel where segment_barcode_id = ?1", nativeQuery = true)
    List<CastingYardData> findReportByEntities(String segmentId);

    @Query(value = "select * from casting_yard_details_hostel where dispatch_id = ?1", nativeQuery = true)
    List<CastingYardData> findByDispatchId(String dispatchId);

    @Query(value = "select * from casting_yard_details_hostel where location_status = ?1", nativeQuery = true)
    List<CastingYardData> findByLocation(String location);

    @Query(value = "select * from casting_yard_details_hostel cyd where casting_date between ?1 and ?2", nativeQuery = true)
    List<CastingYardData> findReportsByCastingDate(String fromDate, String toDate);

    @Query(value = "select * from public.casting_yard_details_hostel cyd where cast(created_date as varchar) between ?1 and ?2 and print_status = 'COMPLETED'", nativeQuery = true)
    List<CastingYardData> findReportsByErectedDate(String fromDate, String toDate);

    @Modifying
    @Query("DELETE FROM CastingYardData WHERE id = ?1")
    int deleteSegment(Integer id);

    @Modifying
    @Query("UPDATE CastingYardData SET reprintReason = ?1 WHERE segmentBarcodeId = ?2")
    int updateReprintReason(String reason, String segmentId);

    @Query(value = "select count(*) from casting_yard_details_hostel", nativeQuery = true)
    int getCountForInventory();

//    @Query(value = "select count(*) from casting_yard_details_hostel", nativeQuery = true)
//    int getCountForInventoryBySegmentId();

    @Query(value = "select count(*) from casting_yard_details_hostel where print_status != 'PENDING'", nativeQuery = true)
    int getPrintedCount();

//    @Query(value = "select count(*) from casting_yard_details_hostel where print_status != 'PENDING'", nativeQuery = true)
//    int getPrintedCountBySegmentId();

    @Query(value = "select count(*) from casting_yard_details_hostel where print_status = 'PENDING'", nativeQuery = true)
    int getPendingCount();

//    @Query(value = "select count(*) from casting_yard_details_hostel where print_status = 'PENDING'", nativeQuery = true)
//    int getPendingCountBySegmentId();

    @Query(value = "select * from casting_yard_details_hostel", nativeQuery = true)
    List<CastingYardData> getSegmentIdsForCount();

    @Query(value = "select count(*) from casting_yard_details_hostel where print_status != 'PENDING' and print_status != 'PRINTED'", nativeQuery = true)
    int getQAConfirmedCount();

//    @Query(value = "select count(*) from casting_yard_details_hostel where print_status != 'PENDING' and print_status != 'PRINTED'", nativeQuery = true)
//    int getQAConfirmedCountBySegmentId();

    @Query(value = "select * from casting_yard_details_hostel where print_status != 'PENDING' and print_status != 'PRINTED'", nativeQuery = true)
    List<CastingYardData> getQAConfirmedCountList();

    @Query(value = "select * from casting_yard_details_hostel where print_status != 'PENDING'", nativeQuery = true)
    List<CastingYardData> getPrintedCountList();

    @Query(value = "select * from casting_yard_details_hostel where print_status = 'PENDING'", nativeQuery = true)
    List<CastingYardData> getPendingCountList();

    @Query(value = "select count(*) from casting_yard_details_hostel cyd where dispatch_id is not null", nativeQuery = true)
    int getDispatchCount();

//    @Query(value = "select count(*) from casting_yard_details_hostel cyd where dispatch_id is not null", nativeQuery = true)
//    int getDispatchCountBySegmentId();

    @Query(value = "select * from casting_yard_details_hostel cyd where dispatch_id is not null", nativeQuery = true)
    List<CastingYardData> getDispatchCountList();

    @Query(value = "select count(*) from casting_yard_details_hostel cyd where cyd.print_count > 1", nativeQuery = true)
    int getReprintCount();

//    @Query(value = "select count(*) from casting_yard_details_hostel cyd where cyd.print_count > 1", nativeQuery = true)
//    int getReprintCountBySegmentId();

    @Query(value = "select * from casting_yard_details_hostel cyd where cyd.print_count > 1;", nativeQuery = true)
    List<CastingYardData> getReprintSegmentId();

    @Query(value = "select count(*) from casting_yard_details_hostel where print_status = 'COMPLETED'", nativeQuery = true)
    int getErectionCompletedCount();

//    @Query(value = "select count(*) from casting_yard_details_hostel where print_status = 'COMPLETED'", nativeQuery = true)
//    int getErectionCompletedCountBySegmentId();

    @Query(value = "select * from casting_yard_details_hostel where print_status = 'COMPLETED';", nativeQuery = true)
    List<CastingYardData> getErectionCompleted();

    @Query(value = "select count(*) from casting_yard_details_hostel where location_status = 'ERECTION YARD'", nativeQuery = true)
    int getErectionYardCount();

//    @Query(value = "select count(*) from casting_yard_details_hostel where location_status = 'ERECTION YARD'", nativeQuery = true)
//    int getErectionYardCountBySegmentId();

    @Query(value = "select * from casting_yard_details_hostel where location_status = 'ERECTION YARD';", nativeQuery = true)
    List<CastingYardData> getErectionYardCountList();

    @Query(value = "select print_status from casting_yard_details_hostel where segment_barcode_id = ?1 and print_status = 'PENDING';", nativeQuery = true)
    Optional<String> checkIfStatusIsPending(String segmentBarcodeId);

    @Query(value = "SELECT count(*) FROM casting_yard_details_hostel WHERE (:filter IS NULL OR segment_barcode_id LIKE %:filter%)", nativeQuery = true)
    int getCountForInventoryBySegmentId(@Param("filter") String filter);

    @Query(value = "SELECT count(*) FROM casting_yard_details_hostel WHERE print_status != 'PENDING' AND (:filter IS NULL OR segment_barcode_id LIKE %:filter%)", nativeQuery = true)
    int getPrintedCountBySegmentId(@Param("filter") String filter);

    @Query(value = "SELECT count(*) FROM casting_yard_details_hostel WHERE print_status = 'PENDING' AND (:filter IS NULL OR segment_barcode_id LIKE %:filter%)", nativeQuery = true)
    int getPendingCountBySegmentId(@Param("filter") String filter);

    @Query(value = "SELECT count(*) FROM casting_yard_details_hostel WHERE print_status != 'PENDING' AND print_status != 'PRINTED' AND (:filter IS NULL OR segment_barcode_id LIKE %:filter%)", nativeQuery = true)
    int getQAConfirmedCountBySegmentId(@Param("filter") String filter);

    @Query(value = "SELECT count(*) FROM casting_yard_details_hostel WHERE dispatch_id IS NOT NULL AND (:filter IS NULL OR segment_barcode_id LIKE %:filter%)", nativeQuery = true)
    int getDispatchCountBySegmentId(@Param("filter") String filter);

    @Query(value = "SELECT count(*) FROM casting_yard_details_hostel WHERE location_status = 'ERECTION YARD' AND (:filter IS NULL OR segment_barcode_id LIKE %:filter%)", nativeQuery = true)
    int getErectionYardCountBySegmentId(@Param("filter") String filter);

    @Query(value = "SELECT count(*) FROM casting_yard_details_hostel WHERE print_status = 'COMPLETED' AND (:filter IS NULL OR segment_barcode_id LIKE %:filter%)", nativeQuery = true)
    int getErectionCompletedCountBySegmentId(@Param("filter") String filter);

    @Query(value = "SELECT count(*) FROM casting_yard_details_hostel WHERE print_count > 1 AND (:filter IS NULL OR segment_barcode_id LIKE %:filter%)", nativeQuery = true)
    int getReprintCountBySegmentId(@Param("filter") String filter);

}
