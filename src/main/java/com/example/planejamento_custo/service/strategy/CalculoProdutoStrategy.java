package com.example.planejamento_custo.service.strategy;

import com.example.planejamento_custo.dto.DreResponseDTO;
import com.example.planejamento_custo.dto.OrcamentoRequestDTO;
import com.example.planejamento_custo.entity.TipoProduto;

import java.math.BigDecimal;

public interface CalculoProdutoStrategy {

    TipoProduto getTipoProduto();

    BigDecimal calcularMaoDeObra(OrcamentoRequestDTO dto);

    BigDecimal calcularGgf(OrcamentoRequestDTO dto, BigDecimal ggfBaseMensal);

    DreResponseDTO calcularDreCompleto(OrcamentoRequestDTO dto);
}