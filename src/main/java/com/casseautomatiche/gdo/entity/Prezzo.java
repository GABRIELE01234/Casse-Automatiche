package com.casseautomatiche.gdo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "prezzo",schema = "public")
public class Prezzo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(name = "data_prezzo")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Timestamp dataPrezzo = Timestamp.valueOf(LocalDateTime.now());
    @Column(name = "valore")
    private BigDecimal valore;
    @ManyToOne( optional = false, targetEntity = Articolo.class)
    @JoinColumn(name = "id_articolo", nullable = false)
    private Articolo articolo;

}
