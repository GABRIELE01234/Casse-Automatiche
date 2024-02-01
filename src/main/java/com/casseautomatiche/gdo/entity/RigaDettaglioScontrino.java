package com.casseautomatiche.gdo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "dettaglio_scontrino",schema = "public")
public class RigaDettaglioScontrino {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @ManyToOne( optional = false, targetEntity = Scontrino.class)
    @JoinColumn(name = "id_scontrino", nullable = false)
    private Scontrino scontrino;
    @ManyToOne( optional = false, targetEntity = Articolo.class)
    @JoinColumn(name = "id_articolo", nullable = false)
    private Articolo articolo;
    @Column(name = "quantita",nullable = false)
    private Integer quantita;
    @Column(name = "sub_tot", nullable = false)
    private BigDecimal sub_tot;

    public RigaDettaglioScontrino(Articolo articolo, Integer quantita, BigDecimal sub_tot) {
        this.articolo = articolo;
        this.quantita = quantita;
        this.sub_tot = sub_tot;
    }
}
