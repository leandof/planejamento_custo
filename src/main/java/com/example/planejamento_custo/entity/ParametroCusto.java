package com.example.planejamento_custo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
public class ParametroCusto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String chave; // Ex: VALOR_HORA_TRABALHO

    @Column(nullable = false, precision = 15, scale = 4)
    private BigDecimal valor; // Ex: 40.90

    private String descricao; // Ex: Custo médio da hora da engenharia/fábrica
}