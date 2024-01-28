package com.casseautomatiche.gdo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reparto",schema = "public")
public class Reparto {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(name = "nome_reparto",nullable = false)
    private String nomeReparto;

    public Reparto(String nomeReparto) {
        this.nomeReparto = nomeReparto;
    }
}
