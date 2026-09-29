package com.example.planejamento_custo.dto;

import com.example.planejamento_custo.entity.TipoProduto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrcamentoRequestDTO {
    private String nomeCliente;
    private String nomeProjeto;
    private Integer diasProducao;
    private TipoProduto tipoProduto;

    private BigDecimal fatorVenda;
    private BigDecimal fatorRevenda;

    private Integer comprimentoMm;
    private Integer larguraMm;
    private Integer alturaMm;

    private Integer horasEngenharia;
    private Integer horasCaldeiraria;
    private Integer horasMontagem;
    private Integer horasPintura;

    private BigDecimal ggfInformado;

    // Percentuais em BigDecimal para permitir operações matemáticas (.divide, .multiply)
    private BigDecimal percIss = BigDecimal.ZERO;
    private BigDecimal percPis = BigDecimal.ZERO;
    private BigDecimal percCofins = BigDecimal.ZERO;
    private BigDecimal percIr = BigDecimal.ZERO;
    private BigDecimal percDespAdm = BigDecimal.ZERO;
    private BigDecimal percDespFin = BigDecimal.ZERO;
    private BigDecimal percComissao = BigDecimal.ZERO;

    private List<ItemEmbarcadoDTO> itensEmbarcados = new ArrayList<>();
    private List<ItemRequestDTO> itensBase = new ArrayList<>();
    private List<ItemEspecificoDTO> itensEspecificos = new ArrayList<>();
    private List<Long> idsCaracteristicasGgf = new ArrayList<>();
}