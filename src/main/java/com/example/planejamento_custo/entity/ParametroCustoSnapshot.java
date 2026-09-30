package com.example.planejamento_custo.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.math.BigDecimal;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParametroCustoSnapshot {

    private BigDecimal aliquotaIcms;
    private BigDecimal aliquotaPis;
    private BigDecimal aliquotaCofins;
    private BigDecimal percentualComissao;
    private BigDecimal percentualGgf;
    private BigDecimal percentualMargemLucro;
    private BigDecimal percentualCreditoIcmsInsumo;
    private BigDecimal percentualCreditoPisCofinsInsumo;

    // Taxas Horárias Mão de Obra (R$/h) por Setor
    private BigDecimal taxaHoraEngenharia;
    private BigDecimal taxaHoraCaldeiraria;
    private BigDecimal taxaHoraMontagem;
    private BigDecimal taxaHoraPintura;
    private BigDecimal taxaHoraEletrica;

    public static ParametroCustoSnapshot from(ParametroCusto parametro) {
        return ParametroCustoSnapshot.builder()
                .aliquotaIcms(parametro.getAliquotaIcms())
                .aliquotaPis(parametro.getAliquotaPis())
                .aliquotaCofins(parametro.getAliquotaCofins())
                .percentualComissao(parametro.getPercentualComissao())
                .percentualGgf(parametro.getPercentualGgf())
                .percentualMargemLucro(parametro.getPercentualMargemLucro())
                .percentualCreditoIcmsInsumo(parametro.getPercentualCreditoIcmsInsumo())
                .percentualCreditoPisCofinsInsumo(parametro.getPercentualCreditoPisCofinsInsumo())
                .taxaHoraEngenharia(parametro.getTaxaHoraEngenharia())
                .taxaHoraCaldeiraria(parametro.getTaxaHoraCaldeiraria())
                .taxaHoraMontagem(parametro.getTaxaHoraMontagem())
                .taxaHoraPintura(parametro.getTaxaHoraPintura())
                .taxaHoraEletrica(parametro.getTaxaHoraEletrica())
                .build();
    }
}