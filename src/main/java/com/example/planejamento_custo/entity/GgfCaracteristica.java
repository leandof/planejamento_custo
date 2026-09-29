package com.example.planejamento_custo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "ggf_caracteristica")
@Data
public class GgfCaracteristica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoProduto tipoProduto; // SOBRECHASSI, SEMIRREBOQUE, VUC, REBOQUE, CONTEINER

    private String nomeCaracteristica; // ex: "MALEIRO", "1 PORTA PALCO"
    private BigDecimal pontuacaoPadrao; // ex: 0.10, 0.35
    private Boolean obrigatorioBase; // Define se é um item base do veículo
}