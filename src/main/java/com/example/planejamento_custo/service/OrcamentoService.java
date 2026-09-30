package com.example.planejamento_custo.service;

import com.example.planejamento_custo.dto.DreResponseDTO;
import com.example.planejamento_custo.dto.OrcamentoRequestDTO;
import com.example.planejamento_custo.entity.*;
import com.example.planejamento_custo.repository.OrcamentoRepository;
import com.example.planejamento_custo.repository.ParametroCustoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrcamentoService {

    private final OrcamentoRepository orcamentoRepository;
    private final ParametroCustoRepository parametroCustoRepository;

    @Transactional
    public Orcamento criarEProcessar(OrcamentoRequestDTO dto) {
        ParametroCusto parametro = parametroCustoRepository.findByTipoProduto(dto.getTipoProduto())
                .orElseGet(() -> criarParametroPadrao(dto.getTipoProduto()));

        ParametroCustoSnapshot snapshot = ParametroCustoSnapshot.from(parametro);

        List<OrcamentoItem> itens = dto.getItens().stream().map(i -> OrcamentoItem.builder()
                .descricao(i.getDescricao())
                .quantidade(i.getQuantidade())
                .precoUnitario(i.getPrecoUnitario())
                .fatorAjuste(i.getFatorAjuste() != null ? i.getFatorAjuste() : BigDecimal.ONE)
                .build()).toList();

        DreResponseDTO dre = calcularDre(itens, dto, snapshot);

        Orcamento orcamento = Orcamento.builder()
                .cliente(dto.getCliente())
                .tipoProduto(dto.getTipoProduto())
                .status(StatusOrcamento.RASCUNHO)
                .parametrosSnapshot(snapshot)
                .itens(itens)
                .precoVendaCalculado(dre.getPrecoVendaBruto())
                .lucroEstimado(dre.getLucroLiquidoEstimado())
                .build();

        itens.forEach(item -> item.setOrcamento(orcamento));
        return orcamentoRepository.save(orcamento);
    }

    public DreResponseDTO calcularDre(List<OrcamentoItem> itens, OrcamentoRequestDTO dto, ParametroCustoSnapshot params) {
        // 1. Custo de Materiais (Insumos * Fator)
        BigDecimal custoMateriais = itens.stream()
                .map(item -> item.getPrecoUnitario()
                        .multiply(item.getQuantidade())
                        .multiply(item.getFatorAjuste()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 2. Custo de Mão de Obra Direta (MOD) por Setor
        BigDecimal taxaEng = params.getTaxaHoraEngenharia() != null ? params.getTaxaHoraEngenharia() : new BigDecimal("65.00");
        BigDecimal taxaCald = params.getTaxaHoraCaldeiraria() != null ? params.getTaxaHoraCaldeiraria() : new BigDecimal("42.00");
        BigDecimal taxaMont = params.getTaxaHoraMontagem() != null ? params.getTaxaHoraMontagem() : new BigDecimal("38.00");
        BigDecimal taxaPint = params.getTaxaHoraPintura() != null ? params.getTaxaHoraPintura() : new BigDecimal("40.00");

        BigDecimal hEng = dto.getHorasEngenharia() != null ? BigDecimal.valueOf(dto.getHorasEngenharia()) : BigDecimal.ZERO;
        BigDecimal hCald = dto.getHorasCaldeiraria() != null ? BigDecimal.valueOf(dto.getHorasCaldeiraria()) : BigDecimal.ZERO;
        BigDecimal hMont = dto.getHorasMontagem() != null ? BigDecimal.valueOf(dto.getHorasMontagem()) : BigDecimal.ZERO;
        BigDecimal hPint = dto.getHorasPintura() != null ? BigDecimal.valueOf(dto.getHorasPintura()) : BigDecimal.ZERO;

        BigDecimal custoMaoDeObra = hEng.multiply(taxaEng)
                .add(hCald.multiply(taxaCald))
                .add(hMont.multiply(taxaMont))
                .add(hPint.multiply(taxaPint));

        BigDecimal custoDiretoTotal = custoMateriais.add(custoMaoDeObra);

        // 3. Crédito Tributário de Entrada
        BigDecimal pctCredito = params.getPercentualCreditoIcmsInsumo().add(params.getPercentualCreditoPisCofinsInsumo());
        BigDecimal creditoTributario = custoMateriais.multiply(pctCredito);

        // 4. Custo Direto Líquido e GGF
        BigDecimal custoDiretoLiquido = custoDiretoTotal.subtract(creditoTributario);
        BigDecimal valorGgf = custoDiretoTotal.multiply(params.getPercentualGgf());
        BigDecimal custoIndustrialTotal = custoDiretoLiquido.add(valorGgf);

        // 5. Formação de Preço de Venda (Markup Divisor)
        BigDecimal somaAliquotasVenda = params.getAliquotaIcms()
                .add(params.getAliquotaPis())
                .add(params.getAliquotaCofins())
                .add(params.getPercentualComissao())
                .add(params.getPercentualMargemLucro());

        BigDecimal divisor = BigDecimal.ONE.subtract(somaAliquotasVenda);
        if (divisor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Soma das alíquotas ultrapassa 100%");
        }

        BigDecimal precoVendaBruto = custoIndustrialTotal.divide(divisor, 2, RoundingMode.HALF_UP);

        return DreResponseDTO.builder()
                .custoMateriais(custoMateriais)
                .custoMaoDeObra(custoMaoDeObra)
                .creditoTributario(creditoTributario)
                .custoDiretoLiquido(custoDiretoLiquido)
                .valorGgf(valorGgf)
                .custoIndustrialTotal(custoIndustrialTotal)
                .precoVendaBruto(precoVendaBruto)
                .valorIcms(precoVendaBruto.multiply(params.getAliquotaIcms()))
                .valorPis(precoVendaBruto.multiply(params.getAliquotaPis()))
                .valorCofins(precoVendaBruto.multiply(params.getAliquotaCofins()))
                .valorComissao(precoVendaBruto.multiply(params.getPercentualComissao()))
                .lucroLiquidoEstimado(precoVendaBruto.multiply(params.getPercentualMargemLucro()))
                .build();
    }

    private ParametroCusto criarParametroPadrao(TipoProduto tipo) {
        return ParametroCusto.builder()
                .tipoProduto(tipo)
                .aliquotaIcms(new BigDecimal("0.18"))
                .aliquotaPis(new BigDecimal("0.0165"))
                .aliquotaCofins(new BigDecimal("0.0760"))
                .percentualComissao(new BigDecimal("0.03"))
                .percentualGgf(new BigDecimal(String.valueOf(tipo.getPercentualGgfPadrao())))
                .percentualMargemLucro(new BigDecimal("0.15"))
                .percentualCreditoIcmsInsumo(new BigDecimal("0.12"))
                .percentualCreditoPisCofinsInsumo(new BigDecimal("0.0925"))
                .taxaHoraEngenharia(new BigDecimal("65.00"))
                .taxaHoraCaldeiraria(new BigDecimal("42.00"))
                .taxaHoraMontagem(new BigDecimal("38.00"))
                .taxaHoraPintura(new BigDecimal("40.00"))
                .taxaHoraEletrica(new BigDecimal("45.00"))
                .build();
    }
}