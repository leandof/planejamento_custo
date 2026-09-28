package com.example.planejamento_custo.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DreResponseDTO {
    private BigDecimal custoTotal;
    private BigDecimal receitaOperacionalBruta; // ROB
    private BigDecimal iss;
    private BigDecimal pis;
    private BigDecimal cofins;
    private BigDecimal impostosTotais;
    private BigDecimal receitaOperacionalLiquida; // ROL
    private BigDecimal despAdm;
    private BigDecimal despFin;
    private BigDecimal lair;
    private BigDecimal ir;
    private BigDecimal lucroLiquido;
    private BigDecimal margemLucroPercentual;
}