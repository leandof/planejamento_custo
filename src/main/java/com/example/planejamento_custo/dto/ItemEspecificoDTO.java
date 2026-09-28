package com.example.planejamento_custo.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ItemEspecificoDTO {
    private String titulo;
    private Integer quantidade;
    private BigDecimal valorUnitario;
}