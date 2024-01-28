package com.casseautomatiche.gdo.repository;

import com.casseautomatiche.gdo.entity.Barcodes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface BarcodesRepository extends JpaRepository<Barcodes, UUID> {
    @Query(value = "SELECT b.* FROM barcodes b WHERE b.barcode = :barcode",nativeQuery = true)
    Barcodes findByCodiceBarcode(@Param("barcode") String barcode);
}
