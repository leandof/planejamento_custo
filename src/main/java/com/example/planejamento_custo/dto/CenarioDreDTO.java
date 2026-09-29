package com.example.planejamento_custo.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CenarioDreDTO {
    private String nomeCenario; // "MÁXIMO", "MÉDIO" ou "MÍNIMO"
    private BigDecimal rob;
    private BigDecimal iss;
    private BigDecimal pis;
    private BigDecimal cofins;
    private BigDecimal rol;
    private BigDecimal custoEstrutural;
    private BigDecimal custoEmbarcados;
    private BigDecimal custoTotal;
    private BigDecimal ggfAdicional;
    private BigDecimal resultadoBruto;
    private BigDecimal despAdm;
    private BigDecimal despFin;
    private BigDecimal lair;
    private BigDecimal ir;
    private BigDecimal lucroLiquido;
    private Double margemPercentual;
}