package com.example.planejamento_custo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_insumo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // Resolve o erro 'Cannot resolve method builder'
public class Insumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigoInsumo;

    private String nome;

    private BigDecimal precoBase;

    private String unidadeMedida;

    private BigDecimal fatorConversao;

    private String fornecedor;

    private LocalDateTime dataAtualizacao;
}