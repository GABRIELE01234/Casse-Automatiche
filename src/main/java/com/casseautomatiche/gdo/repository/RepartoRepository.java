package com.casseautomatiche.gdo.repository;

import com.casseautomatiche.gdo.entity.Reparto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface RepartoRepository extends JpaRepository<Reparto, UUID> {
    @Query(value = """
            SELECT sum(ds.sub_tot)\s
            FROM reparto r\s
            INNER JOIN articolo a ON a.id_reparto = r.id
            INNER JOIN dettaglio_scontrino ds ON ds.id_articolo = a.id
            INNER JOIN scontrino s ON s.id = ds.id_scontrino
            WHERE to_char(s.data_emissione,'dd/MM/YYYY') = :dataParametro
            AND
            r.id = :idReparto""",nativeQuery = true)
    Float incassoReparto(@Param("dataParametro") String dataParametro,@Param("idReparto") UUID idReparto);
    @Query(value = """
            SELECT
            sum(ds.sub_tot)\s
            FROM reparto r\s
            INNER JOIN articolo a ON r.id = a.id_reparto\s
            INNER JOIN dettaglio_scontrino ds ON a.id = ds.id_articolo\s
            INNER JOIN scontrino s ON s.id = ds.id_scontrino\s
            WHERE to_char(s.data_emissione,'YYYY') = :a
            AND r.id = :idReparto""",nativeQuery = true)
    Float incassoByAnnoAndReparto(String a, UUID idReparto);
}
