package com.example.demo.repository;


import com.example.demo.dto.InventoryStockDto;
import com.example.demo.entity.CastingYardData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface CastingYardDetailsRepository extends JpaRepository<CastingYardData, Long> {

    @Query(value = "select * from public.casting_yard_details cyd where cyd.segment_barcode_id = ?1 and date(cyd.casting_date) between ?2 and ?3 ", nativeQuery = true)
    List<CastingYardData> findByIdAndDateBetween(String id, LocalDate fromDate, LocalDate toDate);


    @Query(value = "SELECT DISTINCT family_type as familyType, count(*) as familyCount FROM casting_yard_details group by family_type", nativeQuery = true)
    List<InventoryStockDto> findByFamilyType(String familyType);


    @Query(value = "select * from public.casting_yard_details cyd where cyd.print_status='PENDING'", nativeQuery = true)
    List<CastingYardData> getEntitiesByStatusPending();

    @Query(value = "update public.casting_yard_details cyd set print_status = 'PRINTED', print_count = ?2 where segment_barcode_id = ?1", nativeQuery = true)
    int updateStatusAndCount(String segmentId, Integer printCount);

    @Query(value = "select print_count from public.casting_yard_details cyd where segment_barcode_id = ?1", nativeQuery = true)
    Integer getPrintCount(String segmentId);

    @Query(value = "select * from public.casting_yard_details cyd where segment_barcode_id = ?1 and print_status = 'PRINTED'", nativeQuery = true)
    CastingYardData getDataBySegmentId(String segmentId);

    @Query(value = "select * from public.casting_yard_details cyd where segment_barcode_id = ?1", nativeQuery = true)
    CastingYardData getSegmentDataByQaConfirmation(String segmentId);

    @Query(value = "update public.casting_yard_details set print_status = ?2, updated_by = ?3, created_date = now() where segment_barcode_id in (?1)", nativeQuery = true)
    int updateStatusForSegments(List<String> segmentIds, String status, String userName);

    @Query(value = "update public.casting_yard_details set dispatch_id = ?2 where segment_barcode_id in (?1)", nativeQuery = true)
    int updateDispatchIdForSegmentId(List<String> segmentIds,  String dispatchId);

//    @Query(value = "select dispatch_id from public.casting_yard_details")
//    String findLastDispatchId(String datePrefix);

    @Query("SELECT d.dispatchId FROM CastingYardData d WHERE d.dispatchId LIKE :prefix% ORDER BY d.dispatchId DESC")
    List<String> findLastDispatchId(@Param("prefix") String prefix);
}
