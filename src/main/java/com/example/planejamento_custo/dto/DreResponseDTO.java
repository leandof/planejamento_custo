package com.example.planejamento_custo.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DreResponseDTO {

    private BigDecimal custoMateriais;
    private BigDecimal custoMaoDeObra;
    private BigDecimal creditoTributario;
    private BigDecimal custoDiretoLiquido;
    private BigDecimal valorGgf;
    private BigDecimal custoIndustrialTotal;
    private BigDecimal precoVendaBruto;
    private BigDecimal valorIcms;
    private BigDecimal valorPis;
    private BigDecimal valorCofins;
    private BigDecimal valorComissao;
    private BigDecimal lucroLiquidoEstimado;
}