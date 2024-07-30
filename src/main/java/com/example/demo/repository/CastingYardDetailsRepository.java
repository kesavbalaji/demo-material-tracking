package com.example.demo.repository;


import com.example.demo.dto.InventoryStockDto;
import com.example.demo.entity.CastingYardData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface CastingYardDetailsRepository extends JpaRepository<CastingYardData, Long> {

    @Query(value = "select * from public.casting_yard_details cyd where cyd.segment_barcode_id = ?1 and date(cyd.casting_date) between ?2 and ?3 ", nativeQuery = true)
    List<CastingYardData> findByIdAndDateBetween(String id, LocalDate fromDate, LocalDate toDate);


    @Query(value = "SELECT DISTINCT family_type as familyType, count(*) as familyCount FROM casting_yard_details group by family_type", nativeQuery = true)
    List<InventoryStockDto> findByFamilyType(String familyType);
}
