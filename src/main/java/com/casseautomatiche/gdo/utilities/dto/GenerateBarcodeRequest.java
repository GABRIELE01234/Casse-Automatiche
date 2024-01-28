package com.casseautomatiche.gdo.utilities.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Data
public class GenerateBarcodeRequest {
     @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
     private Timestamp inizioValidita;
     @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
     private Timestamp fineValidita;
     private UUID idArticolo;
}
