package com.casseautomatiche.gdo.repository;

import com.casseautomatiche.gdo.entity.RigaDettaglioScontrino;
import com.casseautomatiche.gdo.entity.Scontrino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.UUID;
@Repository
public interface RigaDettaglioScontrinoRepository extends JpaRepository<RigaDettaglioScontrino, UUID> {
    @Query(value = "SELECT ds.* " +
            "FROM dettaglio_scontrino ds " +
            "WHERE ds.id_scontrino = :idScontrino ",nativeQuery = true)
    Collection<RigaDettaglioScontrino> findAllByIdScontrino(UUID idScontrino);

    @Query(value = """
            SELECT sum(ds.quantita)
            FROM dettaglio_scontrino ds
            INNER JOIN scontrino s ON s.id = ds.id_scontrino
            INNER JOIN articolo a ON a.id = ds.id_articolo
            INNER JOIN stock st ON st.id_articolo = a.id
            WHERE a.id = :idArticolo
            AND
            to_char(s.data_emissione,'dd/MM/YYYY') = :dataParametro""",nativeQuery = true)
    Integer stockIniziale(@Param("dataParametro") String dataParametro,@Param("idArticolo") UUID idArticolo);
    @Query(value = """
            SELECT EXISTS (SELECT ds.*
            FROM dettaglio_scontrino ds\s
            INNER JOIN scontrino s ON s.id = ds.id_scontrino\s
            INNER JOIN articolo a ON a.id = ds.id_articolo
            WHERE s.id = :idScontrino
            AND
            a.id = :idArticolo)""",nativeQuery = true)
    boolean existByIdArticoloAndIdScontrino(UUID idScontrino, UUID idArticolo);

    @Query(value = """
            SELECT ds.*
            FROM dettaglio_scontrino ds\s
            INNER JOIN scontrino s ON s.id = ds.id_scontrino\s
            INNER JOIN articolo a ON a.id = ds.id_articolo
            WHERE s.id = :idScontrino
            AND
            a.id = :idArticolo""",nativeQuery = true)
    RigaDettaglioScontrino IdArticoloAndIdScontrino(UUID idScontrino, UUID idArticolo);

    @Query(value = """
                    SELECT DISTINCT
                                  (
                                  SELECT
                                  sum(ds.quantita)
                                  FROM dettaglio_scontrino ds
                                  INNER JOIN  scontrino s ON s.id = ds.id_scontrino
                                  INNER JOIN articolo a ON a.id = ds.id_articolo
                                  WHERE to_char(s.data_emissione::date,'YYYY-MM-dd') = to_char(:dataParametro::date,'YYYY-MM-dd')
                                  AND
                                  a.id = :idArticolo
                                  ) AS pezzi_venduti,
                                  (
                                  SELECT
                                  sum(ds.sub_tot)
                                  FROM dettaglio_scontrino ds
                                  INNER JOIN  scontrino s ON s.id = ds.id_scontrino
                                  INNER JOIN articolo a ON a.id = ds.id_articolo
                                  WHERE to_char(s.data_emissione::date,'YYYY-MM-dd') = to_char(:dataParametro::date,'YYYY-MM-dd')
                                  AND
                                  a.id = :idArticolo
                                  ) AS incasso_articolo
                                  FROM dettaglio_scontrino ds
                                  INNER JOIN  scontrino s ON s.id = ds.id_scontrino
                                  INNER JOIN articolo a ON a.id = ds.id_articolo
                                  WHERE to_char(s.data_emissione::date,'YYYY-MM-dd') = to_char(:dataParametro::date,'YYYY-MM-dd')
                                  AND
                                  a.id = :idArticolo
                    """,nativeQuery = true)
    ResultSet objectForResocontoArticolo(String dataParametro,UUID idArticolo);
}
