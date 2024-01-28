package com.casseautomatiche.gdo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "stock",schema = "public")
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(name = "quantita_iniziale")
    private Integer quantitaIniziale;
    @ManyToOne( optional = false, targetEntity = Articolo.class)
    @JoinColumn(name = "id_articolo", nullable = false)
    private Articolo articolo;

    public Stock(Integer quantitaIniziale, Articolo articolo) {
        this.quantitaIniziale = quantitaIniziale;
        this.articolo = articolo;
    }
}
