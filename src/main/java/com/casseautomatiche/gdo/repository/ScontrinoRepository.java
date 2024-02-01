package com.casseautomatiche.gdo.repository;

import com.casseautomatiche.gdo.entity.Scontrino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
@Repository
public interface ScontrinoRepository extends JpaRepository<Scontrino, UUID> {
    @Query(value = "SELECT s.* FROM scontrino s WHERE data_emissione = current_date",nativeQuery = true)
    List<Scontrino> findByCurrentData();
    @Query(value = """
            SELECT
            sum(s.totale)
            FROM scontrino s WHERE to_char(s.data_emissione,'DD/MM/YYYY')  =  to_char(current_date, 'DD/MM/YYYY')""",nativeQuery = true)
    BigDecimal incassoGiornaliero();


}
