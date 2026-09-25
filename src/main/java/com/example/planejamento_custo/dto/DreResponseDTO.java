package com.example.planejamento_custo.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DreResponseDTO {
    private BigDecimal custoTotal;
    private BigDecimal receitaOperacionalBruta; // O Preço de Venda Final
    private BigDecimal impostos;
    private BigDecimal receitaOperacionalLiquida;
    private BigDecimal lucroLiquido;
    private BigDecimal margemLucroPercentual;
}
