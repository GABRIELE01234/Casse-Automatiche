package com.casseautomatiche.gdo.service;

import com.casseautomatiche.gdo.entity.Reparto;
import com.casseautomatiche.gdo.entity.Scontrino;
import com.casseautomatiche.gdo.utilities.dto.ArticoloRequest;
import com.casseautomatiche.gdo.utilities.dto.GenerateBarcodeRequest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface GdoService {


    //  Dati gli scontrini, calcolare lo stock a fine giornata
    //  Rendere disponibile un endpoint che calcoli l'incasso della giornata
    //  Rendere disponibile un endpoint che calcoli, per ogni articolo, i pezzi venduti e l'incasso dell'articolo, data una giornata.
    //  Rendere disponibile un endpoint che calcoli l'incasso per reparto, data una giornata
    //  Rendere disponibile un endpoint che calcoli l'incasso per reparto, dato un anno

    void inserisciNuovoArticolo(ArticoloRequest articolo);
    void creaNuovoReparto(Reparto reparto);
    void generazioneBarcode (GenerateBarcodeRequest request);
    void aggiungiArticoloAScontrino(List<String> barcodes);
    Map<String,Integer> calcolaStockAFineGiornata(String dataParametro,UUID idArticolo);
    Map<String,Object> objectForResocontoArticolo(String data, String idArticolo);

    Scontrino creaScontrino();
}
