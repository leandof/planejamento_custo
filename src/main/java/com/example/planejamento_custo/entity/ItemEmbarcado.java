package com.example.planejamento_custo.entity;


import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "item_embarcado")
@Data
public class ItemEmbarcado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;
    private Integer quantidade;
    private BigDecimal valorUnitario;
    private BigDecimal valorTotal;

    @Column(length = 1000)
    private String linkFornecedor; // URL direta do produto/orçamento do fornecedor

    @ManyToOne
    @JoinColumn(name = "orcamento_id")
    private Orcamento orcamento;
}
