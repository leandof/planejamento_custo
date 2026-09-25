package com.example.planejamento_custo.DTOs.;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ItemRequestDTO {
    private Long insumoId;
    private BigDecimal quantidade;
}