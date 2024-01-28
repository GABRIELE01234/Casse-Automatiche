package com.casseautomatiche.gdo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "scontrino",schema = "public")
public class Scontrino {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(name = "data_emissione",nullable = false)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date dataEmissione = new Date();
    @Column(name = "totale")
    private Float totale;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "scontrino")
    private Collection<RigaDettaglioScontrino> righeDettaglioScontrino = new ArrayList<>();

    public Scontrino(Float totale) {
        this.totale = totale;
    }
    public void aggiungiRigaScontrino(Articolo articolo, int quantita, Prezzo prezzo) {
        this.righeDettaglioScontrino.add(new RigaDettaglioScontrino(articolo, quantita,prezzo.getValore() * quantita));
    }
}
