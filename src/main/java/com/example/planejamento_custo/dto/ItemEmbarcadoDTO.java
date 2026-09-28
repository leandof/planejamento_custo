package com.example.planejamento_custo.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ItemEmbarcadoDTO {
    private String titulo;
    private Integer quantidade;
    private BigDecimal valorUnitario;
    private String linkFornecedor; // Opcional, para rastreabilidade de compras
}