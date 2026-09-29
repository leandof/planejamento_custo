package com.example.planejamento_custo.service;

import com.example.planejamento_custo.dto.*;
import com.example.planejamento_custo.entity.GgfCaracteristica;
import com.example.planejamento_custo.entity.Orcamento;
import com.example.planejamento_custo.entity.StatusOrcamento;
import com.example.planejamento_custo.repository.GgfCaracteristicaRepository;
import com.example.planejamento_custo.repository.OrcamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class OrcamentoService {

    @Autowired
    private OrcamentoRepository orcamentoRepository;

    @Autowired
    private GgfCaracteristicaRepository ggfRepository;

    public DreResponseDTO simularOrcamento(OrcamentoRequestDTO dto) {
        // 1. Custo Estrutural (Horas)
        BigDecimal valorHora = BigDecimal.valueOf(40.90);
        int totalHoras = (dto.getHorasEngenharia() != null ? dto.getHorasEngenharia() : 0) +
                (dto.getHorasCaldeiraria() != null ? dto.getHorasCaldeiraria() : 0) +
                (dto.getHorasMontagem() != null ? dto.getHorasMontagem() : 0) +
                (dto.getHorasPintura() != null ? dto.getHorasPintura() : 0);
        BigDecimal custoEstrutural = valorHora.multiply(BigDecimal.valueOf(totalHoras));

        // 2. Pontuação e Cálculo GGF
        BigDecimal pontuacaoTotal = BigDecimal.ZERO;
        if (dto.getIdsCaracteristicasGgf() != null && !dto.getIdsCaracteristicasGgf().isEmpty()) {
            List<GgfCaracteristica> caracteristicas = ggfRepository.findAllById(dto.getIdsCaracteristicasGgf());
            for (GgfCaracteristica c : caracteristicas) {
                if (c.getPontuacaoPadrao() != null) {
                    pontuacaoTotal = pontuacaoTotal.add(c.getPontuacaoPadrao());
                }
            }
        }
        BigDecimal ggfBaseMensal = BigDecimal.valueOf(375000.00);
        BigDecimal custoGgf = ggfBaseMensal.multiply(pontuacaoTotal);

        // 3. Custo Itens Embarcados
        BigDecimal custoEmbarcados = BigDecimal.ZERO;
        if (dto.getItensEmbarcados() != null) {
            for (ItemEmbarcadoDTO item : dto.getItensEmbarcados()) {
                if (item.getValorUnitario() != null && item.getQuantidade() != null) {
                    custoEmbarcados = custoEmbarcados.add(item.getValorUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())));
                }
            }
        }

        BigDecimal custoTotal = custoEstrutural.add(custoGgf).add(custoEmbarcados);

        // 4. Cenários de Markup (Variação de Fatores de Venda)
        BigDecimal fatorMax = dto.getFatorVenda() != null ? dto.getFatorVenda() : BigDecimal.valueOf(2.0);
        BigDecimal fatorMed = fatorMax.multiply(BigDecimal.valueOf(0.85)); // 15% menor que o Máximo
        BigDecimal fatorMin = fatorMax.multiply(BigDecimal.valueOf(0.70)); // 30% menor que o Máximo

        DreResponseDTO response = new DreResponseDTO();
        response.setCenarioMaximo(calcularCenario("MÁXIMO", custoTotal, fatorMax, custoEstrutural, custoEmbarcados, custoGgf, dto));
        response.setCenarioMedio(calcularCenario("MÉDIO", custoTotal, fatorMed, custoEstrutural, custoEmbarcados, custoGgf, dto));
        response.setCenarioMinimo(calcularCenario("MÍNIMO", custoTotal, fatorMin, custoEstrutural, custoEmbarcados, custoGgf, dto));

        // Resumo
        response.setCustoTotal(custoTotal);
        response.setReceitaOperacionalBruta(response.getCenarioMedio().getRob());
        response.setMargemLucroPercentual(BigDecimal.valueOf(response.getCenarioMedio().getMargemPercentual()));

        return response;
    }

    private CenarioDreDTO calcularCenario(String nome, BigDecimal custoTotal, BigDecimal fator, BigDecimal custoEstrutural, BigDecimal custoEmbarcados, BigDecimal custoGgf, OrcamentoRequestDTO dto) {
        BigDecimal rob = custoTotal.multiply(fator);

        BigDecimal percIss = dto.getPercIss() != null ? dto.getPercIss() : BigDecimal.ZERO;
        BigDecimal percPis = dto.getPercPis() != null ? dto.getPercPis() : BigDecimal.ZERO;
        BigDecimal percCofins = dto.getPercCofins() != null ? dto.getPercCofins() : BigDecimal.ZERO;

        BigDecimal iss = rob.multiply(percIss).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal pis = rob.multiply(percPis).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal cofins = rob.multiply(percCofins).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal impostosTotais = iss.add(pis).add(cofins);

        BigDecimal rol = rob.subtract(impostosTotais);
        BigDecimal resultadoBruto = rol.subtract(custoTotal);

        BigDecimal percDespAdm = dto.getPercDespAdm() != null ? dto.getPercDespAdm() : BigDecimal.ZERO;
        BigDecimal percDespFin = dto.getPercDespFin() != null ? dto.getPercDespFin() : BigDecimal.ZERO;

        BigDecimal despAdm = rob.multiply(percDespAdm).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal despFin = rob.multiply(percDespFin).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        BigDecimal lair = resultadoBruto.subtract(despAdm).subtract(despFin);

        BigDecimal percIr = dto.getPercIr() != null ? dto.getPercIr() : BigDecimal.ZERO;
        BigDecimal ir = lair.compareTo(BigDecimal.ZERO) > 0 ? lair.multiply(percIr).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;

        BigDecimal lucroLiquido = lair.subtract(ir);
        BigDecimal margemPercentual = rob.compareTo(BigDecimal.ZERO) > 0 ? lucroLiquido.multiply(BigDecimal.valueOf(100)).divide(rob, 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;

        CenarioDreDTO c = new CenarioDreDTO();
        c.setNomeCenario(nome); c.setRob(rob); c.setIss(iss); c.setPis(pis); c.setCofins(cofins); c.setRol(rol);
        c.setCustoEstrutural(custoEstrutural); c.setCustoEmbarcados(custoEmbarcados); c.setGgfAdicional(custoGgf);
        c.setCustoTotal(custoTotal); c.setResultadoBruto(resultadoBruto); c.setDespAdm(despAdm); c.setDespFin(despFin);
        c.setLair(lair); c.setIr(ir); c.setLucroLiquido(lucroLiquido); c.setMargemPercentual(margemPercentual.doubleValue());

        return c;
    }

    public Orcamento salvarOrcamento(OrcamentoRequestDTO dto) {
        DreResponseDTO dre = simularOrcamento(dto);
        Orcamento orcamento = new Orcamento();
        orcamento.setNomeCliente(dto.getNomeCliente());
        orcamento.setNomeProjeto(dto.getNomeProjeto());
        orcamento.setDiasProducao(dto.getDiasProducao());
        orcamento.setTipoProduto(dto.getTipoProduto());
        orcamento.setValorTotalCusto(dre.getCustoTotal());
        orcamento.setValorTotalVenda(dre.getReceitaOperacionalBruta());
        orcamento.setPercentualLucro(dre.getMargemLucroPercentual().doubleValue());
        orcamento.setStatus(StatusOrcamento.APROVADO); // Status padrão ao salvar
        return orcamentoRepository.save(orcamento);
    }

    // ALIAS (Para evitar erros com o Controller antigo)
    public DreResponseDTO simularDre(OrcamentoRequestDTO dto) { return simularOrcamento(dto); }
    public Orcamento salvarOrcamentoDefinitivo(OrcamentoRequestDTO dto) { return salvarOrcamento(dto); }
}