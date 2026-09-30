package com.example.planejamento_custo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_orcamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // Resolve o erro 'Cannot resolve method builder'
public class Orcamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cliente;

    @Enumerated(EnumType.STRING)
    private TipoProduto tipoProduto;

    @Enumerated(EnumType.STRING)
    private StatusOrcamento status;

    @Embedded
    private ParametroCustoSnapshot parametrosSnapshot;

    @OneToMany(mappedBy = "orcamento", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrcamentoItem> itens = new ArrayList<>();

    private BigDecimal precoVendaCalculado;
    private BigDecimal lucroEstimado;
}