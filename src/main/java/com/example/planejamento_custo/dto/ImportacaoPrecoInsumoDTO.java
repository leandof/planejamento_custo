package com.example.planejamento_custo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportacaoPrecoInsumoDTO {

    @NotNull(message = "O código do insumo é obrigatório")
    private String codigoInsumo;

    @NotNull(message = "O nome do insumo é obrigatório")
    private String nome;

    @NotNull(message = "O preço base é obrigatório")
    @Positive(message = "O preço base deve ser maior que zero")
    private BigDecimal precoBase;

    private String unidadeMedida;
    private BigDecimal fatorConversao;
    private String fornecedor;
}