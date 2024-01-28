package com.casseautomatiche.gdo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "barcodes",schema = "public")
public class Barcodes {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(name = "barcode")
    private String codiceBarcode;
    @Column(name = "inizio_validita",nullable = false)
    private Date inizioValidita;
    @Column(name = "fine_validita",nullable = false)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Timestamp fineValidita;
    @ManyToOne( optional = false, targetEntity = Articolo.class)
    @JoinColumn(name = "id_articolo", nullable = false)
    private Articolo articolo;

    public Barcodes(Date inizioValidita, Timestamp fineValidita, Articolo articolo) {
        this.codiceBarcode = generateBarcode();
        this.inizioValidita = inizioValidita;
        this.fineValidita = fineValidita;
        this.articolo = articolo;
    }

    private String generateBarcode() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().substring(0, 8);
    }
}
