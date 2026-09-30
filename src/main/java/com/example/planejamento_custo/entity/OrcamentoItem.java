package com.example.planejamento_custo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_orcamento_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrcamentoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String descricao;

    @NotNull @Positive
    private BigDecimal quantidade;

    @NotNull @Positive
    private BigDecimal precoUnitario;

    @NotNull @Positive
    private BigDecimal fatorAjuste;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orcamento_id")
    private Orcamento orcamento;
}