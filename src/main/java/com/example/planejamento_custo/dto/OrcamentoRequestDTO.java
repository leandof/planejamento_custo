package com.example.planejamento_custo.dto;

import com.example.planejamento_custo.entity.TipoProduto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrcamentoRequestDTO {

    @NotNull
    private String cliente;

    @NotNull
    private TipoProduto tipoProduto;

    // Parâmetros editáveis enviados no orçamento (se nulo, usa padrão do banco)
    private ParametroCustoDTO parametrosEditaveis;

    // Horas por Setor
    private Integer horasEngenharia;
    private Integer horasCaldeiraria;
    private Integer horasMontagem;
    private Integer horasPintura;
    private Integer horasEletrica;

    @NotEmpty
    @Valid
    private List<ItemRequestDTO> itens;
}