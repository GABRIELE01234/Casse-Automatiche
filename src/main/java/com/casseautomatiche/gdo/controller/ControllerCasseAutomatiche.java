package com.casseautomatiche.gdo.controller;

import com.casseautomatiche.gdo.entity.Articolo;
import com.casseautomatiche.gdo.entity.Barcodes;
import com.casseautomatiche.gdo.entity.Reparto;
import com.casseautomatiche.gdo.repository.ArticoloRepository;
import com.casseautomatiche.gdo.repository.BarcodesRepository;
import com.casseautomatiche.gdo.repository.RepartoRepository;
import com.casseautomatiche.gdo.repository.ScontrinoRepository;
import com.casseautomatiche.gdo.service.GdoServiceImpl;
import com.casseautomatiche.gdo.utilities.JsonResponse;
import com.casseautomatiche.gdo.utilities.dto.ArticoloRequest;
import com.casseautomatiche.gdo.utilities.dto.GenerateBarcodeRequest;
import com.casseautomatiche.gdo.utilities.dto.ScontrinoRequest;
import com.casseautomatiche.gdo.utilities.exception.MyRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ControllerCasseAutomatiche {

    @Autowired
    private GdoServiceImpl service;
    @Autowired
    private ScontrinoRepository scontrinoRepository;
    @Autowired
    private RepartoRepository repartoRepository;
    @Autowired
    private ArticoloRepository articoloRepository;
    @Autowired
    private BarcodesRepository barcodeRepository;


    /*
    - Creare un endpoint che permetta di creare uno scontrino
    - Dato un barcode, registrare un articolo all'interno dello scontrino. è possibile inserire lo stesso articolo più volte
    */
    @PostMapping("/creazione-scontrino")
    public ResponseEntity<Void> creaScontrino(@RequestBody List<String> barcode){
        service.aggiungiArticoloAScontrino(barcode);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    /* - Dati gli scontrini, calcolare lo stock a fine giornata*/
    @GetMapping("/stock-finale")
    public ResponseEntity<Map<String,Integer>> calcolaStock(@RequestParam UUID idArticolo,@RequestParam String dataParametro){
        return ResponseEntity.status(HttpStatus.OK).body(service.calcolaStockAFineGiornata(dataParametro,idArticolo));
    }

    /* - Rendere disponibile un endpoint che calcoli l'incasso della giornata*/
    @GetMapping("/incasso")
    public ResponseEntity<BigDecimal> incassoGiornaliero(){
        return ResponseEntity.status(HttpStatus.OK).body(scontrinoRepository.incassoGiornaliero());
    }

    /* - Rendere disponibile un endpoint che calcoli, per ogni articolo, i pezzi venduti e l'incasso dell'articolo, data una giornata.*/
    @GetMapping("/resoconto-articolo")
    public ResponseEntity<Map<String,Object>> calcoloPezziVendutiEdIncassoByIdArticoloAndByData(@RequestParam UUID idArticolo,@RequestParam String data){
        return ResponseEntity.status(HttpStatus.OK).body(service.objectForResocontoArticolo(data,idArticolo));
    }


    /* - Rendere disponibile un endpoint che calcoli l'incasso per reparto, data una giornata*/
    @GetMapping("/incasso-per-reparto")
    public ResponseEntity<BigDecimal> calcoloIncassoByReparto(@RequestParam String dataParametro,@RequestParam UUID idReparto){
        return ResponseEntity.status(HttpStatus.OK).body(repartoRepository.incassoReparto(dataParametro,idReparto));
    }


    /*- Rendere disponibile un endpoint che calcoli l'incasso per reparto, dato un anno*/
    @GetMapping("/incasso-per-reparto-by-anno")
    public ResponseEntity<BigDecimal> calcoloIncassoByRepartoAndAnno(@RequestParam String a, @RequestParam UUID idReparto){
        return ResponseEntity.status(HttpStatus.OK).body(repartoRepository.incassoByAnnoAndReparto(a,idReparto));
    }
    @PostMapping("/crea-reparto")
    public ResponseEntity<JsonResponse> creaReparto(@RequestBody Reparto reparto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.creaNuovoReparto(reparto));
    }
    @PostMapping("/inserisci-articolo")
    public ResponseEntity<JsonResponse> inserisciArticolo(@RequestBody ArticoloRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.inserisciNuovoArticolo(request));
    }
    @PostMapping("/genera-barcode")
    public ResponseEntity<Void> generaBarcode(@RequestBody GenerateBarcodeRequest request){
        service.generazioneBarcode(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @GetMapping("/allReparts")
    public ResponseEntity<Collection<Reparto>> getAllRepart(){
        return ResponseEntity.status(HttpStatus.OK).body(repartoRepository.findAll());
    }
    @GetMapping("/allArticoli")
    public ResponseEntity<Collection<Articolo>> getAllArticoli(){
        return ResponseEntity.status(HttpStatus.OK).body(articoloRepository.findAll());
    }
    @GetMapping("/allBarcodes")
    public ResponseEntity<Collection<Barcodes>> getAllBarcode(){
        return ResponseEntity.status(HttpStatus.OK).body(barcodeRepository.findAll());
    }



}
