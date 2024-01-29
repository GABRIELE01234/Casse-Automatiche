package com.casseautomatiche.gdo.controller;

import com.casseautomatiche.gdo.entity.Reparto;
import com.casseautomatiche.gdo.repository.RepartoRepository;
import com.casseautomatiche.gdo.repository.ScontrinoRepository;
import com.casseautomatiche.gdo.service.GdoServiceImpl;
import com.casseautomatiche.gdo.utilities.dto.ArticoloRequest;
import com.casseautomatiche.gdo.utilities.dto.GenerateBarcodeRequest;
import com.casseautomatiche.gdo.utilities.dto.RigaScontrinoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Float> incassoGiornaliero(){
        return ResponseEntity.status(HttpStatus.OK).body(scontrinoRepository.incassoGiornaliero());
    }

    /* - Rendere disponibile un endpoint che calcoli, per ogni articolo, i pezzi venduti e l'incasso dell'articolo, data una giornata.*/
    @GetMapping("/resoconto-articolo")
    public ResponseEntity<Map<String,Object>> calcoloPezziVendutiEdIncassoByIdArticoloAndByData(@RequestParam String idArticolo,@RequestParam String data){
        return ResponseEntity.status(HttpStatus.OK).body(service.objectForResocontoArticolo(data,idArticolo));
    }


    /* - Rendere disponibile un endpoint che calcoli l'incasso per reparto, data una giornata*/
    @GetMapping("/incasso-per-reparto")
    public ResponseEntity<Float> calcoloIncassoByReparto(@RequestParam String dataParametro,@RequestParam UUID idReparto){
        return ResponseEntity.status(HttpStatus.OK).body(repartoRepository.incassoReparto(dataParametro,idReparto));
    }


    /*- Rendere disponibile un endpoint che calcoli l'incasso per reparto, dato un anno*/
    @GetMapping("/incasso-per-reparto-by-anno")
    public ResponseEntity<Float> calcoloIncassoByRepartoAndAnno(@RequestParam String a, @RequestParam UUID idReparto){
        return ResponseEntity.status(HttpStatus.OK).body(repartoRepository.incassoByAnnoAndReparto(a,idReparto));
    }
    @PostMapping("/crea-reparto")
    public ResponseEntity<Void> creaReparto(@RequestBody Reparto reparto){
        service.creaNuovoReparto(reparto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PostMapping("/inserisci-articolo")
    public ResponseEntity<Void> inserisciArticolo(@RequestBody ArticoloRequest request){
        service.inserisciNuovoArticolo(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PostMapping("/genera-barcode")
    public ResponseEntity<Void> generaBarcode(@RequestBody GenerateBarcodeRequest request){
        service.generazioneBarcode(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
