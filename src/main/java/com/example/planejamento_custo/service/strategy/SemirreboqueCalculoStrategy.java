package com.example.planejamento_custo.service.strategy;

import com.example.planejamento_custo.dto.DreResponseDTO;
import com.example.planejamento_custo.dto.OrcamentoRequestDTO;
import com.example.planejamento_custo.entity.TipoProduto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SemirreboqueCalculoStrategy implements CalculoProdutoStrategy {

    @Override
    public TipoProduto getTipoProduto() {
        return TipoProduto.SEMIRREBOQUE;
    }

    @Override
    public BigDecimal calcularMaoDeObra(OrcamentoRequestDTO dto) {
        // Lógica de cálculo de horas de Semirreboque
        int totalHoras = (dto.getHorasEngenharia() != null ? dto.getHorasEngenharia() : 0) +
                (dto.getHorasCaldeiraria() != null ? dto.getHorasCaldeiraria() : 0) +
                (dto.getHorasMontagem() != null ? dto.getHorasMontagem() : 0) +
                (dto.getHorasPintura() != null ? dto.getHorasPintura() : 0);

        // Exemplo: R$ 40,90 valor hora base
        return BigDecimal.valueOf(totalHoras).multiply(BigDecimal.valueOf(40.90));
    }

    @Override
    public BigDecimal calcularGgf(OrcamentoRequestDTO dto, BigDecimal ggfBaseMensal) {
        // Regra de pontuação do Semirreboque base (ex: 0.50 + adicionais)
        BigDecimal pontuacaoBase = BigDecimal.valueOf(0.50);
        return ggfBaseMensal.multiply(pontuacaoBase);
    }

    @Override
    public DreResponseDTO calcularDreCompleto(OrcamentoRequestDTO dto) {
        // O orquestrador chamará este método para montar a DRE completa
        return new DreResponseDTO();
    }
}