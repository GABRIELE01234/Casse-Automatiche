package com.casseautomatiche.gdo.repository;

import com.casseautomatiche.gdo.entity.Articolo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface ArticoloRepository extends JpaRepository<Articolo, UUID> {
    @Query(value = "SELECT a.* FROM barcodes b INNER JOIN articolo a ON a.id = b.id_articolo WHERE b.barcode = :barcode",nativeQuery = true)
    Articolo findArticoloByBarcode(@Param("barcode") String barcode);
    @Query(value = "SELECT EXISTS(SELECT a.*" +
            "FROM barcodes b " +
            "INNER JOIN articolo a ON a.id = b.id_articolo " +
            "WHERE b.barcode = :code)",nativeQuery = true)
    boolean existArticoloByBarcode(String code);
}
