package com.example.planejamento_custo.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DreResponseDTO {
    // Campos diretos do DRE esperados pelo Service
    private BigDecimal receitaOperacionalBruta = BigDecimal.ZERO;
    private BigDecimal iss = BigDecimal.ZERO;
    private BigDecimal pis = BigDecimal.ZERO;
    private BigDecimal cofins = BigDecimal.ZERO;
    private BigDecimal impostosTotais = BigDecimal.ZERO;
    private BigDecimal receitaOperacionalLiquida = BigDecimal.ZERO;
    private BigDecimal custoEstrutural = BigDecimal.ZERO;
    private BigDecimal custoEmbarcados = BigDecimal.ZERO;
    private BigDecimal ggfAdicional = BigDecimal.ZERO;
    private BigDecimal custoTotal = BigDecimal.ZERO;
    private BigDecimal resultadoBruto = BigDecimal.ZERO;
    private BigDecimal despAdm = BigDecimal.ZERO;
    private BigDecimal despFin = BigDecimal.ZERO;
    private BigDecimal lair = BigDecimal.ZERO;
    private BigDecimal ir = BigDecimal.ZERO;
    private BigDecimal lucroLiquido = BigDecimal.ZERO;
    private BigDecimal margemLucroPercentual = BigDecimal.ZERO;

    // Comparativo de cenários triplos
    private CenarioDreDTO cenarioMaximo;
    private CenarioDreDTO cenarioMedio;
    private CenarioDreDTO cenarioMinimo;
}