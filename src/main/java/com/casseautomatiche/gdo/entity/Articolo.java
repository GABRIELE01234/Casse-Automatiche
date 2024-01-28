package com.casseautomatiche.gdo.entity;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "articolo",schema = "public")
public class Articolo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(name = "nome",nullable = false)
    private String nome;
    @Column(name = "grammatura",nullable = false)
    private Integer grammatura;
    @Column(name = "unita_di_misura",nullable = false)
    private String unitaDiMisura;
    @ManyToOne( optional = false, targetEntity = Reparto.class)
    @JoinColumn(name = "id_reparto", nullable = false)
    private Reparto reparto;

    public Articolo(String nome, Integer grammatura, String unitaDiMisura, Reparto reparto) {
        this.nome = nome;
        this.grammatura = grammatura;
        this.unitaDiMisura = unitaDiMisura;
        this.reparto = reparto;
    }
}
