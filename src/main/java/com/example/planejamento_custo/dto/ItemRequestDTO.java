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
public class ItemRequestDTO {

    @NotNull(message = "Descrição é obrigatória")
    private String descricao;

    @NotNull(message = "Quantidade é obrigatória")
    @Positive(message = "Quantidade deve ser maior que zero")
    private BigDecimal quantidade;

    @NotNull(message = "Preço unitário é obrigatório")
    @Positive(message = "Preço unitário deve ser maior que zero")
    private BigDecimal precoUnitario;

    private BigDecimal fatorAjuste;
}