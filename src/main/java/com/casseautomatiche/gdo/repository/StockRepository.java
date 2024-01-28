package com.casseautomatiche.gdo.repository;

import com.casseautomatiche.gdo.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StockRepository extends JpaRepository<Stock, UUID> {
    @Query(value = "SELECT s.quantita_iniziale FROM stock s WHERE s.id_articolo = :idArticolo",nativeQuery = true)
    Integer returnStockInizialeByIdArticolo(@Param("idArticolo") UUID idArticolo);
    @Query(value = "SELECT s.* FROM stock s WHERE s.id_articolo = :idArticolo",nativeQuery = true)
    Stock findByIdArticolo(@Param("idArticolo") UUID idArticolo);
}
