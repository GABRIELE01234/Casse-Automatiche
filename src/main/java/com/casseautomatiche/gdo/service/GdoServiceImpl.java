package com.casseautomatiche.gdo.service;

import com.casseautomatiche.gdo.entity.*;
import com.casseautomatiche.gdo.utilities.dto.RigaScontrinoRequest;
import com.casseautomatiche.gdo.utilities.exception.MyRuntimeException;
import com.casseautomatiche.gdo.repository.*;
import com.casseautomatiche.gdo.utilities.dto.ArticoloRequest;
import com.casseautomatiche.gdo.utilities.dto.GenerateBarcodeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;


@Service
public class GdoServiceImpl implements GdoService {

    @Autowired
    private BarcodesRepository barcodesRepository;
    @Autowired
    private ArticoloRepository articoloRepository;
    @Autowired
    private RepartoRepository repartoRepository;
    @Autowired
    private PrezzoRepository prezzoRepository;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private ScontrinoRepository scontrinoRepository;
    @Autowired
    private RigaDettaglioScontrinoRepository rigaDettaglioScontrinoRepository;
    @Autowired
    private DataSource dataSource;


    @Override
    public void creaNuovoReparto(Reparto reparto) {
        if (reparto == null) {
            throw new MyRuntimeException("ATTENZIONE: CAMPO NULLO INSERISCI IL NOME-REPARTO.", 105);
        }
        Reparto nuovoReparto = new Reparto();
        nuovoReparto.setNomeReparto(reparto.getNomeReparto());
        Collection<Reparto> reparti = repartoRepository.findAll();
        if (reparti.stream()
                .anyMatch(r -> r.getNomeReparto().equalsIgnoreCase(nuovoReparto.getNomeReparto()))) {
            throw new MyRuntimeException("ATTENZIONE: IL REPARTO '" + nuovoReparto.getNomeReparto().toUpperCase() + "' ESISTE GIA' ", 106);
        }
        repartoRepository.saveAndFlush(nuovoReparto);
    }

    @Override
    public void inserisciNuovoArticolo(ArticoloRequest articolo) {
        Reparto reparto = repartoRepository.findById(articolo.getIdReparto())
                .orElseThrow(() -> new MyRuntimeException("ATTENZIONE: IDENTIFICATIVO REPARTO NULLO. CONTROLLA E RIPROVA.", 102));
        if (articolo.getGrammatura() == null
                || articolo.getNome() == null
                || articolo.getUnitaDiMisura() == null
        ) {
            throw new MyRuntimeException("ATTENZIONE: E' POSIIBILE CHE ABBIA DIMENTICATO DI INSERIRE QUALCHE DATO. CONTROLLA E RIPROVA.", 103);
        }
        Articolo a = new Articolo();
        a.setNome(articolo.getNome());
        a.setGrammatura(articolo.getGrammatura());
        a.setUnitaDiMisura(articolo.getUnitaDiMisura());
        a.setReparto(reparto);
        articoloRepository.saveAndFlush(a);

        // ASSOCIA PREZZO AD ARTICOLO
        Prezzo prezzo = new Prezzo();
        prezzo.setArticolo(a);
        if (articolo.getPrezzo() < 0){
            throw new MyRuntimeException("ATTENZIONE: STAI PROVANDO AD INSERIRE UN PREZZO NEGATIVO. INSERISCI UN PREZZO MAGGIORE DI 0",110);
        }
        prezzo.setValore(articolo.getPrezzo());
        prezzoRepository.saveAndFlush(prezzo);

        // ASSOCIA STOCK AD ARTICOLO
        Stock stockIniziale = new Stock();
        stockIniziale.setArticolo(a);
        if (articolo.getStock() < 0){
            throw new MyRuntimeException("ATTENZIONE: STAI PROVANDO AD INSERIRE UN VALORE DI STOCK NEGATIVO. INSERISCI UNO STOCK MAGGIORE DI 0",111);
        }
        stockIniziale.setQuantitaIniziale(articolo.getStock());
        stockRepository.saveAndFlush(stockIniziale);
    }

    @Override
    public void generazioneBarcode(GenerateBarcodeRequest request) {
        if (request.getInizioValidita() == null
                || request.getFineValidita() == null) {
            throw new MyRuntimeException("ATTENZIONE: E' POSSIBILE CHE ABBIA DIMENTICATO DI INSERIRE QUALCHE DATO. CONTROLLA E RIPROVA.", 103);
        }
        Articolo articolo = articoloRepository.findById(request.getIdArticolo())
                .orElseThrow(() -> new MyRuntimeException("ATTENZIONE: IDENTIFICATIVO ARTICOLO NON CORRETTO. CONTROLLA E RIPROVA", 107));
        Barcodes barcodes = new Barcodes(request.getInizioValidita(), request.getFineValidita(), articolo);
        barcodesRepository.saveAndFlush(barcodes);
    }

    @Transactional
    @Override
    public Scontrino creaScontrino() {
        Scontrino nuovoScontrino = new Scontrino();
        return scontrinoRepository.save(nuovoScontrino);
    }

    @Transactional
    @Override
    public void aggiungiArticoloAScontrino(List<String> barcodes) {
        float tot = 0;
        Scontrino scontrino = creaScontrino();
        for (String code : barcodes) {
            RigaDettaglioScontrino rigaDettaglioScontrino = new RigaDettaglioScontrino();
            Articolo articolo = articoloRepository.findArticoloByBarcode(code);
            Stock stock = stockRepository.findByIdArticolo(articolo.getId());
            int disponibile = stock.getQuantitaIniziale();
            Prezzo prezzo = prezzoRepository.findByIdArticolo(articolo.getId());
            rigaDettaglioScontrino.setScontrino(scontrino);
            if (rigaDettaglioScontrinoRepository.existByIdArticoloAndIdScontrino(scontrino.getId(), articolo.getId())) {
                rigaDettaglioScontrino = rigaDettaglioScontrinoRepository.IdArticoloAndIdScontrino(scontrino.getId(), articolo.getId());
                rigaDettaglioScontrino.setQuantita(rigaDettaglioScontrino.getQuantita() + 1);
                rigaDettaglioScontrino.setSub_tot(rigaDettaglioScontrino.getQuantita() * prezzo.getValore());
                verificaStockDisponibile(disponibile,1);
                stock.setQuantitaIniziale(disponibile - 1);
                rigaDettaglioScontrinoRepository.saveAndFlush(rigaDettaglioScontrino);
                stockRepository.saveAndFlush(stock);
                tot += rigaDettaglioScontrino.getSub_tot();
            } else if (!rigaDettaglioScontrinoRepository.existByIdArticoloAndIdScontrino(scontrino.getId(), articolo.getId())) {
                rigaDettaglioScontrino.setArticolo(articolo);
                rigaDettaglioScontrino.setQuantita(1);
                rigaDettaglioScontrino.setSub_tot(prezzo.getValore());
                verificaStockDisponibile(disponibile,rigaDettaglioScontrino.getQuantita());
                stock.setQuantitaIniziale(disponibile - 1);
                rigaDettaglioScontrinoRepository.saveAndFlush(rigaDettaglioScontrino);
                stockRepository.saveAndFlush(stock);
                tot += rigaDettaglioScontrino.getSub_tot();
            }
        }
        scontrino.setTotale(tot);
        scontrinoRepository.save(scontrino);
    }
    public void verificaStockDisponibile (Integer disponibile,Integer richiesta){
        if (disponibile < richiesta) {
            throw new MyRuntimeException("ATTENZIONE: RIPROVA INSERENDO UNA QUANTITA CHE NON SUPERA QUELLA DISPONIBILE. DISPONIBILE: "+disponibile+" -- RICHIESTA: "+richiesta, 116);
        }
    }
    @Transactional(readOnly = true)
    @Override
    public Map<String,Object> objectForResocontoArticolo(String data,String idArticolo) {
        Map<String, Object> objectForRead = new HashMap<>();
        try (Connection conn = dataSource.getConnection()) {
            String sqlQuery = """
                    SELECT DISTINCT
                                  (
                                  SELECT
                                  sum(ds.quantita)
                                  FROM dettaglio_scontrino ds
                                  INNER JOIN  scontrino s ON s.id = ds.id_scontrino
                                  INNER JOIN articolo a ON a.id = ds.id_articolo
                                  WHERE to_char(s.data_emissione::date,'YYYY-MM-dd') = to_char(?::date,'YYYY-MM-dd')
                                  AND
                                  a.id = ?::uuid
                                  ) AS pezzi_venduti,
                                  (
                                  SELECT
                                  sum(ds.sub_tot)
                                  FROM dettaglio_scontrino ds
                                  INNER JOIN  scontrino s ON s.id = ds.id_scontrino
                                  INNER JOIN articolo a ON a.id = ds.id_articolo
                                  WHERE to_char(s.data_emissione::date,'YYYY-MM-dd') = to_char(?::date,'YYYY-MM-dd')
                                  AND
                                  a.id = ?::uuid
                                  ) AS incasso_articolo
                                  FROM dettaglio_scontrino ds
                                  INNER JOIN  scontrino s ON s.id = ds.id_scontrino
                                  INNER JOIN articolo a ON a.id = ds.id_articolo
                                  WHERE to_char(s.data_emissione::date,'YYYY-MM-dd') = to_char(?::date,'YYYY-MM-dd')
                                  AND
                                  a.id = ?::uuid
                    """;
            PreparedStatement statement = conn.prepareStatement(sqlQuery);
            statement.setString(1,data);
            statement.setString(2,idArticolo);
            statement.setString(3,data);
            statement.setString(4,idArticolo);
            statement.setString(5,data);
            statement.setString(6,idArticolo);

            ResultSet resultSet = statement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                for (int i=1; i<=columnCount; i++){
                    if (metaData.getColumnName(i).equalsIgnoreCase("pezzi_venduti")){
                        objectForRead.put(metaData.getColumnName(i),resultSet.getObject("pezzi_venduti"));
                    } else {
                        objectForRead.put(metaData.getColumnName(i),resultSet.getObject("incasso_articolo"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return objectForRead;
    }
    @Transactional(readOnly = true)
    @Override
    public Map<String,Integer> calcolaStockAFineGiornata(String dataParametro,UUID idArticolo) {
        if (dataParametro == null || dataParametro.isBlank() ){
            throw new MyRuntimeException("ATTENZIONE: INSERISCI UNA DATA PER CALCOLARE LO STOCK FINALE",117);
        } else if (idArticolo == null ){
            throw new MyRuntimeException("ATTENZIONE: INSERISCI L'ARTICOLO PER CALCOLARE LO STOCK FINALE",118);
        }
        Map<String,Integer> mapStock = new HashMap<>();
        int stockDisponibile = stockRepository.returnStockInizialeByIdArticolo(idArticolo);
        int stockIniziale = rigaDettaglioScontrinoRepository.stockIniziale(dataParametro,idArticolo) + stockDisponibile;
        mapStock.put("stockIniziale: ",stockIniziale);
        mapStock.put("stockDisponibile: ",stockDisponibile);
        return mapStock;
    }
}

