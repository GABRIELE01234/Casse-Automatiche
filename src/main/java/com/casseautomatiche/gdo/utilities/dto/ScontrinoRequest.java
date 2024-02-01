package com.casseautomatiche.gdo.utilities.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ScontrinoRequest {

    private BigDecimal totale;
    private String dataParametro;
    private List <ArticoloRequest> articoloRequest;

}
