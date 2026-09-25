package com.example.planejamento_custo.DTOs.;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrcamentoRequestDTO {
    private Integer comprimentoMm;
    private Integer larguraMm;
    private Integer alturaMm;
    private BigDecimal fatorVenda;
    private List<ItemRequestDTO> itens;
}