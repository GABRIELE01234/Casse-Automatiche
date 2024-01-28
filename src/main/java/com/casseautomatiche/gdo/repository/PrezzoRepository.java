package com.casseautomatiche.gdo.repository;

import com.casseautomatiche.gdo.entity.Prezzo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface PrezzoRepository extends JpaRepository<Prezzo, UUID> {
    @Query("SELECT p FROM Prezzo p WHERE p.articolo.id = ?1")
    Prezzo findByIdArticolo(UUID id);
}
