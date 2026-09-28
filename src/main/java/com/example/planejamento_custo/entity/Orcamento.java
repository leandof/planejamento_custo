package com.example.planejamento_custo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "orcamento")
public class Orcamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: No futuro vincularemos a um Usuario e Cliente. Por enquanto focaremos no cálculo.

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusOrcamento status = StatusOrcamento.RASCUNHO;

    // Dimensões Paramétricas do Projeto (vindas da aba da planilha)
    private Integer comprimentoMm;
    private Integer larguraMm;
    private Integer alturaMm;

    // Resultados do Cálculo (DRE)
    @Column(precision = 15, scale = 4)
    private BigDecimal valorTotalCusto;

    @Column(precision = 15, scale = 4)
    private BigDecimal valorTotalVenda;

    @Column(precision = 5, scale = 2)
    private BigDecimal percentualLucro;

    @OneToMany(mappedBy = "orcamento", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<OrcamentoItem> itens = new ArrayList<>();
}