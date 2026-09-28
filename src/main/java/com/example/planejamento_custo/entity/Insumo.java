package com.example.planejamento_custo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data // Se estiver usando Lombok (gera getters e setters). Se não, crie-os manualmente.
@Entity
@Table(name = "insumo")
public class Insumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigoSku;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private String unidadeMedida;

    @Column(nullable = false, precision = 12, scale = 4)
    private BigDecimal custoAtual;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoInsumo tipo;
}