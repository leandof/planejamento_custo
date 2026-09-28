package com.example.planejamento_custo.service;

import com.example.planejamento_custo.dto.*;
import com.example.planejamento_custo.entity.Insumo;
import com.example.planejamento_custo.entity.ParametroCusto;
import com.example.planejamento_custo.repository.InsumoRepository;
import com.example.planejamento_custo.repository.OrcamentoRepository;
import com.example.planejamento_custo.repository.ParametroCustoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrcamentoService {

    @Autowired
    private InsumoRepository insumoRepository;

    @Autowired
    private OrcamentoRepository orcamentoRepository;

    @Autowired
    private ParametroCustoRepository parametroCustoRepository; // <-- Novo Repositório Injetado

    public DreResponseDTO simularDre(OrcamentoRequestDTO request) {
        BigDecimal custoFabricacao = BigDecimal.ZERO;
        BigDecimal custoRevenda = BigDecimal.ZERO;

        // ==========================================
        // CARREGAMENTO DOS PARÂMETROS DA BASE DE DADOS
        // ==========================================
        Map<String, BigDecimal> precosBD = parametroCustoRepository.findAll()
                .stream()
                .collect(Collectors.toMap(ParametroCusto::getChave, ParametroCusto::getValor));

        // Função auxiliar para garantir que se o preço for apagado da BD, o sistema não crasha
        java.util.function.Function<String, BigDecimal> getPreco = (chave) -> precosBD.getOrDefault(chave, BigDecimal.ZERO);

        // 1. MÃO DE OBRA (Fabricação)
        int totalHoras = (request.getHorasEngenharia() != null ? request.getHorasEngenharia() : 0) +
                (request.getHorasCaldeiraria() != null ? request.getHorasCaldeiraria() : 0) +
                (request.getHorasMontagem() != null ? request.getHorasMontagem() : 0) +
                (request.getHorasPintura() != null ? request.getHorasPintura() : 0);

        BigDecimal custoMaoDeObra = getPreco.apply("VALOR_HORA_TRABALHO").multiply(new BigDecimal(totalHoras));
        custoFabricacao = custoFabricacao.add(custoMaoDeObra);

        // 2. CÁLCULO GEOMÉTRICO (Metros)
        BigDecimal comprimentoM = new BigDecimal(request.getComprimentoMm() != null ? request.getComprimentoMm() : 0).divide(new BigDecimal("1000"), 4, RoundingMode.HALF_UP);
        BigDecimal larguraM = new BigDecimal(request.getLarguraMm() != null ? request.getLarguraMm() : 0).divide(new BigDecimal("1000"), 4, RoundingMode.HALF_UP);
        BigDecimal alturaM = new BigDecimal(request.getAlturaMm() != null ? request.getAlturaMm() : 0).divide(new BigDecimal("1000"), 4, RoundingMode.HALF_UP);

        BigDecimal areaPisoM2 = comprimentoM.multiply(larguraM);
        BigDecimal areaParedesM2 = comprimentoM.multiply(alturaM).multiply(new BigDecimal("2")).add(larguraM.multiply(alturaM).multiply(new BigDecimal("2")));

        // 3. REVESTIMENTOS E ESTRUTURA (Fabricação)
        if (Boolean.TRUE.equals(request.getPisoAluminioRecalcado())) custoFabricacao = custoFabricacao.add(areaPisoM2.multiply(getPreco.apply("CUSTO_PISO_ALUMINIO_M2")));
        if (Boolean.TRUE.equals(request.getRevestimentoAcm())) custoFabricacao = custoFabricacao.add(areaParedesM2.multiply(getPreco.apply("CUSTO_REVESTIMENTO_ACM_M2")));
        if (Boolean.TRUE.equals(request.getQuadroTraseiroComPortas())) custoFabricacao = custoFabricacao.add(getPreco.apply("CUSTO_QUADRO_TRASEIRO"));
        if (request.getQtdDivisorias() != null && request.getQtdDivisorias() > 0) custoFabricacao = custoFabricacao.add(getPreco.apply("CUSTO_DIVISORIA").multiply(new BigDecimal(request.getQtdDivisorias())));

        if (request.getToldoBasculanteComprimentoMm() != null && request.getToldoBasculanteComprimentoMm() > 0) {
            BigDecimal toldoM = new BigDecimal(request.getToldoBasculanteComprimentoMm()).divide(new BigDecimal("1000"), 4, RoundingMode.HALF_UP);
            custoFabricacao = custoFabricacao.add(toldoM.multiply(getPreco.apply("CUSTO_TOLDO_FABRICADO_M")));
        }

        // 4. ITENS COMPRADOS E ACESSÓRIOS (Revenda)
        if (request.getQtdJanelasLateral() != null && request.getQtdJanelasLateral() > 0) custoRevenda = custoRevenda.add(getPreco.apply("CUSTO_JANELA").multiply(new BigDecimal(request.getQtdJanelasLateral())));
        if (request.getQtdBanheiros() != null && request.getQtdBanheiros() > 0) custoRevenda = custoRevenda.add(getPreco.apply("CUSTO_BANHEIRO_COMPLETO").multiply(new BigDecimal(request.getQtdBanheiros())));
        if (Boolean.TRUE.equals(request.getSistemaHidrossanitario())) custoRevenda = custoRevenda.add(getPreco.apply("CUSTO_HIDROSSANITARIO"));

        if (request.getToldoLateralArticuladoComprimentoMm() != null && request.getToldoLateralArticuladoComprimentoMm() > 0) {
            BigDecimal toldoLatM = new BigDecimal(request.getToldoLateralArticuladoComprimentoMm()).divide(new BigDecimal("1000"), 4, RoundingMode.HALF_UP);
            custoRevenda = custoRevenda.add(toldoLatM.multiply(getPreco.apply("CUSTO_TOLDO_COMPRADO_M")));
        }

        if (Boolean.TRUE.equals(request.getAlimentacaoExternaTrifasica()) && request.getAlimentacaoKva() != null && request.getAlimentacaoKva() > 0) {
            custoRevenda = custoRevenda.add(new BigDecimal("3000.00")).add(getPreco.apply("CUSTO_KVA_ADICIONAL").multiply(new BigDecimal(request.getAlimentacaoKva())));
        }

        // 5. LISTAS DINÂMICAS
        if (request.getItensBase() != null) {
            for (ItemRequestDTO itemDTO : request.getItensBase()) {
                Insumo insumo = insumoRepository.findById(itemDTO.getInsumoId()).orElseThrow(() -> new RuntimeException("Insumo não encontrado"));
                custoFabricacao = custoFabricacao.add(insumo.getCustoAtual().multiply(itemDTO.getQuantidade()));
            }
        }
        if (request.getItensEspecificos() != null) {
            for (ItemEspecificoDTO esp : request.getItensEspecificos()) {
                if (esp.getValorUnitario() != null && esp.getQuantidade() != null) {
                    custoFabricacao = custoFabricacao.add(esp.getValorUnitario().multiply(new BigDecimal(esp.getQuantidade())));
                }
            }
        }
        if (request.getItensEmbarcados() != null) {
            for (ItemEmbarcadoDTO emb : request.getItensEmbarcados()) {
                if (emb.getValorUnitario() != null && emb.getQuantidade() != null) {
                    custoRevenda = custoRevenda.add(emb.getValorUnitario().multiply(new BigDecimal(emb.getQuantidade())));
                }
            }
        }

        // 6. MOTOR FINANCEIRO
        BigDecimal fatorVenda = request.getFatorVenda() != null ? request.getFatorVenda() : BigDecimal.ONE;
        BigDecimal fatorRevenda = request.getFatorRevenda() != null ? request.getFatorRevenda() : BigDecimal.ONE;

        BigDecimal subtotalFabricacao = custoFabricacao.multiply(fatorVenda);
        BigDecimal subtotalRevenda = custoRevenda.multiply(fatorRevenda);
        BigDecimal subtotalVenda = subtotalFabricacao.add(subtotalRevenda);

        BigDecimal fatorIss = request.getPercIss().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
        BigDecimal fatorPis = request.getPercPis().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
        BigDecimal fatorCofins = request.getPercCofins().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
        BigDecimal fatorImpostosBase = fatorIss.add(fatorPis).add(fatorCofins);

        BigDecimal fatorComissao = request.getPercComissao().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP).add(BigDecimal.ONE);

        BigDecimal markupImpostos = subtotalVenda.multiply(fatorImpostosBase);
        BigDecimal rob = subtotalVenda.add(markupImpostos).multiply(fatorComissao);

        BigDecimal iss = rob.multiply(fatorIss);
        BigDecimal pis = rob.multiply(fatorPis);
        BigDecimal cofins = rob.multiply(fatorCofins);
        BigDecimal impostosTotais = iss.add(pis).add(cofins);

        BigDecimal rol = rob.subtract(impostosTotais);
        BigDecimal custoTotalGlobal = custoFabricacao.add(custoRevenda);
        BigDecimal resultadoBruto = rol.subtract(custoTotalGlobal);

        BigDecimal ggfFinal = request.getGgfInformado() != null ? request.getGgfInformado() : new BigDecimal("562500.00");
        BigDecimal fatorDespAdm = request.getPercDespAdm().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
        BigDecimal fatorDespFin = request.getPercDespFin().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
        BigDecimal despAdm = rol.multiply(fatorDespAdm);
        BigDecimal despFin = rol.multiply(fatorDespFin);

        BigDecimal lair = resultadoBruto.subtract(despAdm).subtract(despFin).subtract(ggfFinal);

        BigDecimal fatorIr = request.getPercIr().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
        BigDecimal ir = lair.multiply(fatorIr);
        BigDecimal lucroLiquido = lair.subtract(ir);

        BigDecimal margemPercentual = BigDecimal.ZERO;
        if (rob.compareTo(BigDecimal.ZERO) > 0) {
            margemPercentual = lucroLiquido.divide(rob, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
        }

        DreResponseDTO response = new DreResponseDTO();
        response.setCustoTotal(custoTotalGlobal);
        response.setReceitaOperacionalBruta(rob);
        response.setIss(iss);
        response.setPis(pis);
        response.setCofins(cofins);
        response.setImpostosTotais(impostosTotais);
        response.setReceitaOperacionalLiquida(rol);
        response.setDespAdm(despAdm);
        response.setDespFin(despFin);
        response.setLair(lair);
        response.setIr(ir);
        response.setLucroLiquido(lucroLiquido);
        response.setMargemLucroPercentual(margemPercentual);

        return response;
    }

    public com.example.planejamento_custo.entity.Orcamento salvarOrcamentoDefinitivo(OrcamentoRequestDTO request) {
        DreResponseDTO dre = simularDre(request);
        com.example.planejamento_custo.entity.Orcamento orcamento = new com.example.planejamento_custo.entity.Orcamento();
        orcamento.setComprimentoMm(request.getComprimentoMm());
        orcamento.setLarguraMm(request.getLarguraMm());
        orcamento.setAlturaMm(request.getAlturaMm());
        orcamento.setValorTotalCusto(dre.getCustoTotal());
        orcamento.setValorTotalVenda(dre.getReceitaOperacionalBruta());
        orcamento.setPercentualLucro(dre.getMargemLucroPercentual());
        orcamento.setStatus(com.example.planejamento_custo.entity.StatusOrcamento.RASCUNHO);
        return orcamentoRepository.save(orcamento);
    }
}