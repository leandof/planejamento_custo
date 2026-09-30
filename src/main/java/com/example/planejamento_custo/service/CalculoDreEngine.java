package com.example.planejamento_custo.service;

import com.example.planejamento_custo.dto.DreResponseDTO;
import com.example.planejamento_custo.dto.ItemRequestDTO;
import com.example.planejamento_custo.dto.ParametroCustoDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class CalculoDreEngine {

    public static DreResponseDTO calcular(
            List<ItemRequestDTO> itens,
            Integer hEng, Integer hCald, Integer hMont, Integer hPint, Integer hElet,
            ParametroCustoDTO p) {

        // 1. Custo Materiais (Insumos * Fator)
        BigDecimal custoMateriais = BigDecimal.ZERO;
        if (itens != null) {
            for (ItemRequestDTO item : itens) {
                BigDecimal qtd = item.getQuantidade() != null ? item.getQuantidade() : BigDecimal.ZERO;
                BigDecimal preco = item.getPrecoUnitario() != null ? item.getPrecoUnitario() : BigDecimal.ZERO;
                BigDecimal fator = item.getFatorAjuste() != null ? item.getFatorAjuste() : BigDecimal.ONE;
                custoMateriais = custoMateriais.add(qtd.multiply(preco).multiply(fator));
            }
        }

        // 2. Custo Mão de Obra Direta (MOD) por Setor
        BigDecimal valEng = p.getTaxaHoraEngenharia().multiply(BigDecimal.valueOf(hEng != null ? hEng : 0));
        BigDecimal valCald = p.getTaxaHoraCaldeiraria().multiply(BigDecimal.valueOf(hCald != null ? hCald : 0));
        BigDecimal valMont = p.getTaxaHoraMontagem().multiply(BigDecimal.valueOf(hMont != null ? hMont : 0));
        BigDecimal valPint = p.getTaxaHoraPintura().multiply(BigDecimal.valueOf(hPint != null ? hPint : 0));
        BigDecimal valElet = p.getTaxaHoraEletrica().multiply(BigDecimal.valueOf(hElet != null ? hElet : 0));

        BigDecimal custoMaoDeObra = valEng.add(valCald).add(valMont).add(valPint).add(valElet);
        BigDecimal custoDiretoTotal = custoMateriais.add(custoMaoDeObra);

        // 3. Créditos Tributários Entrada (sobre Insumos)
        BigDecimal pctCreditoTotal = p.getPercentualCreditoIcmsInsumo().add(p.getPercentualCreditoPisCofinsInsumo());
        BigDecimal creditoTributario = custoMateriais.multiply(pctCreditoTotal);

        // 4. Custo Direto Líquido & GGF
        BigDecimal custoDiretoLiquido = custoDiretoTotal.subtract(creditoTributario);
        BigDecimal valorGgf = custoDiretoTotal.multiply(p.getPercentualGgf());
        BigDecimal custoIndustrialTotal = custoDiretoLiquido.add(valorGgf);

        // 5. Formação de Preço por Dentro (Markup Divisor)
        BigDecimal somaAliquotasVenda = p.getAliquotaIcms()
                .add(p.getAliquotaPis())
                .add(p.getAliquotaCofins())
                .add(p.getPercentualComissao())
                .add(p.getPercentualMargemLucro());

        BigDecimal divisor = BigDecimal.ONE.subtract(somaAliquotasVenda);
        if (divisor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("A soma das alíquotas e margem ultrapassa 100%");
        }

        BigDecimal precoVendaBruto = custoIndustrialTotal.divide(divisor, 2, RoundingMode.HALF_UP);

        BigDecimal valorIcms = precoVendaBruto.multiply(p.getAliquotaIcms());
        BigDecimal valorPis = precoVendaBruto.multiply(p.getAliquotaPis());
        BigDecimal valorCofins = precoVendaBruto.multiply(p.getAliquotaCofins());
        BigDecimal valorComissao = precoVendaBruto.multiply(p.getPercentualComissao());
        BigDecimal lucroLiquido = precoVendaBruto.multiply(p.getPercentualMargemLucro());

        return DreResponseDTO.builder()
                .custoMateriais(custoMateriais)
                .custoMaoDeObra(custoMaoDeObra)
                .creditoTributario(creditoTributario)
                .custoDiretoLiquido(custoDiretoLiquido)
                .valorGgf(valorGgf)
                .custoIndustrialTotal(custoIndustrialTotal)
                .precoVendaBruto(precoVendaBruto)
                .valorIcms(valorIcms)
                .valorPis(valorPis)
                .valorCofins(valorCofins)
                .valorComissao(valorComissao)
                .lucroLiquidoEstimado(lucroLiquido)
                .build();
    }
}