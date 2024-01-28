package com.casseautomatiche.gdo.utilities.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ArticoloRequest {

    private UUID id;
    private String nome;
    private Integer grammatura;
    private String unitaDiMisura;
    private UUID idReparto;
    private Float prezzo;
    private Integer stock;
}
