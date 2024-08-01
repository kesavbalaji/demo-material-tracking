package com.example.demo.repository;


import com.example.demo.dto.InventoryStockDto;
import com.example.demo.entity.CastingYardData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface CastingYardDetailsRepository extends JpaRepository<CastingYardData, Long> {

    @Query(value = "select * from public.casting_yard_details cyd where cyd.segment_barcode_id = ?1 and date(cyd.casting_date) between ?2 and ?3 ", nativeQuery = true)
    List<CastingYardData> findByIdAndDateBetween(String id, LocalDate fromDate, LocalDate toDate);


    @Query(value = "SELECT DISTINCT family_type as familyType, count(*) as familyCount FROM casting_yard_details group by family_type", nativeQuery = true)
    List<InventoryStockDto> findByFamilyType(String familyType);


    @Query(value = "select * from public.casting_yard_details cyd where cyd.print_status='PENDING'", nativeQuery = true)
    List<CastingYardData> getEntitiesByStatusPending();

    @Query(value = "update public.casting_yard_details cyd set print_status = 'PRINTED', print_count = ?2 where segment_barcode_id = ?1", nativeQuery = true)
    void updateStatusAndCount(String segmentId, Integer printCount);

    @Query(value = "select print_count from public.casting_yard_details cyd where segment_barcode_id = ?1", nativeQuery = true)
    Integer getPrintCount(String segmentId);
}
