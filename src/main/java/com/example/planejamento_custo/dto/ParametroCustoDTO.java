package com.example.planejamento_custo.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParametroCustoDTO {

    private BigDecimal aliquotaIcms;
    private BigDecimal aliquotaPis;
    private BigDecimal aliquotaCofins;
    private BigDecimal percentualComissao;
    private BigDecimal percentualGgf;
    private BigDecimal percentualMargemLucro;

    private BigDecimal percentualCreditoIcmsInsumo;
    private BigDecimal percentualCreditoPisCofinsInsumo;

    private BigDecimal taxaHoraEngenharia;
    private BigDecimal taxaHoraCaldeiraria;
    private BigDecimal taxaHoraMontagem;
    private BigDecimal taxaHoraPintura;
    private BigDecimal taxaHoraEletrica;
}