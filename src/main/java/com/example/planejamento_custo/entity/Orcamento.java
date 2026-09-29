package com.example.planejamento_custo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orcamento")
@Data
public class Orcamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeCliente;
    private String nomeProjeto;
    private Integer diasProducao;

    @Enumerated(EnumType.STRING)
    private TipoProduto tipoProduto;

    private Integer comprimentoMm;
    private Integer larguraMm;
    private Integer alturaMm;

    private BigDecimal valorTotalCusto;
    private BigDecimal valorTotalVenda;
    private Double percentualLucro;

    @Enumerated(EnumType.STRING)
    private StatusOrcamento status;

    private LocalDateTime dataCriacao;

    @OneToMany(mappedBy = "orcamento", cascade = CascadeType.ALL)
    private List<ItemEmbarcado> itensEmbarcados;

    @PrePersist
    public void prePersist() {
        this.dataCriacao = LocalDateTime.now();
        if (this.status == null) {
            this.status = StatusOrcamento.EM_ANALISE;
        }
    }
}