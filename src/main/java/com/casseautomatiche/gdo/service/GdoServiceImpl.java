package com.casseautomatiche.gdo.service;

import com.casseautomatiche.gdo.entity.*;
import com.casseautomatiche.gdo.utilities.JsonResponse;
import com.casseautomatiche.gdo.utilities.dto.ScontrinoRequest;
import com.casseautomatiche.gdo.utilities.exception.MyRuntimeException;
import com.casseautomatiche.gdo.repository.*;
import com.casseautomatiche.gdo.utilities.dto.ArticoloRequest;
import com.casseautomatiche.gdo.utilities.dto.GenerateBarcodeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.math.BigDecimal;
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

    @Transactional
    @Override
    public JsonResponse creaNuovoReparto(Reparto reparto) {
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
        return new JsonResponse("OTTIMO. REPARTO '"+reparto.getNomeReparto()+"' AGGIUNTO CON SUCCESSO.");
    }

    @Transactional
    @Override
    public JsonResponse inserisciNuovoArticolo(ArticoloRequest articolo) {
        Reparto reparto = repartoRepository.findById(articolo.getIdReparto())
                .orElseThrow(() -> new MyRuntimeException("ATTENZIONE: IDENTIFICATIVO REPARTO NULLO. CONTROLLA E RIPROVA.", 102));
        if (articolo.getGrammatura() == null
                || articolo.getNome() == null
                || articolo.getUnitaDiMisura() == null
        ) {
            throw new MyRuntimeException("ATTENZIONE: E' POSIIBILE CHE ABBIA DIMENTICATO DI INSERIRE QUALCHE DATO. CONTROLLA E RIPROVA.", 103);
        }
        Articolo art = new Articolo();
        art.setNome(articolo.getNome());
        art.setGrammatura(articolo.getGrammatura());
        art.setUnitaDiMisura(articolo.getUnitaDiMisura());
        art.setReparto(reparto);
        articoloRepository.saveAndFlush(art);

        // ASSOCIA PREZZO AD ARTICOLO
        Prezzo prezzo = new Prezzo();
        prezzo.setArticolo(art);
        if (articolo.getPrezzo().compareTo(BigDecimal.ZERO)< 0){
            throw new MyRuntimeException("ATTENZIONE: STAI PROVANDO AD INSERIRE UN PREZZO NEGATIVO. INSERISCI UN PREZZO MAGGIORE DI 0",110);
        }
        prezzo.setValore(articolo.getPrezzo());
        prezzoRepository.saveAndFlush(prezzo);

        // ASSOCIA STOCK AD ARTICOLO
        Stock stockIniziale = new Stock();
        stockIniziale.setArticolo(art);
        if (articolo.getStock() < 0){
            throw new MyRuntimeException("ATTENZIONE: STAI PROVANDO AD INSERIRE UN VALORE DI STOCK NEGATIVO. INSERISCI UNO STOCK MAGGIORE DI 0",111);
        }
        stockIniziale.setQuantitaIniziale(articolo.getStock());
        stockRepository.saveAndFlush(stockIniziale);
        return new JsonResponse("ARTICOLO: '"+articolo.getNome()+"' AGGIUNTO CON SUCCESSO.");
    }
    @Transactional
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
        BigDecimal tot = new BigDecimal(0);
        Scontrino scontrino = creaScontrino();
        ScontrinoRequest scontrinoRequest = new ScontrinoRequest();
        scontrinoRequest.setDataParametro(String.valueOf(scontrino.getDataEmissione()));
        if (barcodes == null){
            throw new MyRuntimeException("ATTENZIONE: ISERISCI ALMENO UN BARCODE PER POTER AGGIUNGERE UN ARTICOLO ALLO SCONTRINO.",120);
        }
        else
        {
            for (String code : barcodes) {
            RigaDettaglioScontrino rigaDettaglioScontrino = new RigaDettaglioScontrino();
            if (!articoloRepository.existArticoloByBarcode(code)){
                throw new MyRuntimeException("ATTENZIONE: IL BARCODDE INSERITO '"+code+"' NON CORRISPONDE A NESSUN ARTICOLO IN MAGAZZINO",121);
            }
            Articolo articolo = articoloRepository.findArticoloByBarcode(code);
            Stock stock = stockRepository.findByIdArticolo(articolo.getId());
            int disponibile = stock.getQuantitaIniziale();
            Prezzo prezzo = prezzoRepository.findByIdArticolo(articolo.getId());
            rigaDettaglioScontrino.setScontrino(scontrino);
            if (rigaDettaglioScontrinoRepository.existByIdArticoloAndIdScontrino(scontrino.getId(), articolo.getId())) {
                rigaDettaglioScontrino = rigaDettaglioScontrinoRepository.IdArticoloAndIdScontrino(scontrino.getId(), articolo.getId());
                rigaDettaglioScontrino.setQuantita(rigaDettaglioScontrino.getQuantita() + 1);
                rigaDettaglioScontrino.setSub_tot(prezzo.getValore().multiply(new BigDecimal(rigaDettaglioScontrino.getQuantita())));
                verificaStockDisponibile(disponibile,1);
                stock.setQuantitaIniziale(disponibile - 1);
                rigaDettaglioScontrinoRepository.saveAndFlush(rigaDettaglioScontrino);
                stockRepository.saveAndFlush(stock);
                tot = tot.add(new BigDecimal(String.valueOf(prezzo.getValore())));
            } else if (!rigaDettaglioScontrinoRepository.existByIdArticoloAndIdScontrino(scontrino.getId(), articolo.getId())) {
                rigaDettaglioScontrino.setArticolo(articolo);
                rigaDettaglioScontrino.setQuantita(1);
                rigaDettaglioScontrino.setSub_tot(prezzo.getValore());
                verificaStockDisponibile(disponibile,rigaDettaglioScontrino.getQuantita());
                stock.setQuantitaIniziale(disponibile - 1);
                rigaDettaglioScontrinoRepository.saveAndFlush(rigaDettaglioScontrino);
                stockRepository.saveAndFlush(stock);
                tot = tot.add(prezzo.getValore());
            }
        }
        scontrino.setTotale(tot);
        scontrinoRepository.save(scontrino);
        }
    }

    public void verificaStockDisponibile (Integer disponibile,Integer richiesta){
        if (disponibile < richiesta) {
            throw new MyRuntimeException("ATTENZIONE: RIPROVA INSERENDO UNA QUANTITA CHE NON SUPERA QUELLA DISPONIBILE. DISPONIBILE: "+disponibile+" -- RICHIESTA: "+richiesta, 116);
        }
    }
    @Transactional(readOnly = true)
    @Override
    public Map<String,Object> objectForResocontoArticolo(String data,UUID idArticolo) {
        if (idArticolo == null || !articoloRepository.existsById(idArticolo)){
            throw new MyRuntimeException("ATTENZIONE: L'ID INSERITO '"+idArticolo+"' NON CORRISPONDE A NESSUN ARTICOLO IN MAGAZZINO",121);
        }
        Map<String, Object> objectForRead = new HashMap<>();
        try (ResultSet resultSet = rigaDettaglioScontrinoRepository.objectForResocontoArticolo(data, idArticolo)) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    if (metaData.getColumnName(i).equalsIgnoreCase("pezzi_venduti")) {
                        objectForRead.put(metaData.getColumnName(i), resultSet.getObject("pezzi_venduti"));
                    }
                    else
                    {
                        objectForRead.put(metaData.getColumnName(i), resultSet.getObject("incasso_articolo"));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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

