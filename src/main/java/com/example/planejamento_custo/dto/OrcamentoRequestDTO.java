package com.example.planejamento_custo.dto;

import com.example.planejamento_custo.entity.TipoProduto;
import lombok.Data;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrcamentoRequestDTO {
    // 1. Definição do Produto e Dimensões do Baú
    private TipoProduto tipoProduto;
    private Integer comprimentoMm = 0;
    private Integer larguraMm = 0;
    private Integer alturaMm = 0;

    // 2. Parâmetros Fiscais e Duplo Fator de Preço
    private BigDecimal fatorVenda = new BigDecimal("2.0");
    private BigDecimal fatorRevenda = new BigDecimal("2.5"); // Markup menor para itens comprados
    private BigDecimal ggfInformado;
    private Integer diasProducao = 0; // Para futuro rateio de GGF

    // 3. Mão de Obra (Horas Estimadas)
    private Integer horasEngenharia = 0;
    private Integer horasCaldeiraria = 0;
    private Integer horasMontagem = 0;
    private Integer horasPintura = 0;

    // 4. Parâmetros Fiscais e Despesas (%)
    private BigDecimal percIss = new BigDecimal("5.00");
    private BigDecimal percPis = new BigDecimal("1.65");
    private BigDecimal percCofins = new BigDecimal("7.60");
    private BigDecimal percDespAdm = new BigDecimal("4.00");
    private BigDecimal percDespFin = new BigDecimal("1.30");
    private BigDecimal percIr = new BigDecimal("34.00");
    private BigDecimal percComissao = new BigDecimal("3.00");

    // 5. Estrutura e Acessórios
    private Integer qtdJanelasLateral = 0;
    private Boolean quadroTraseiroComPortas = false;
    private Integer qtdDivisorias = 0;
    private Integer qtdBanheiros = 0;
    private Boolean sistemaHidrossanitario = false;

    // 6. Revestimentos e Acabamentos
    private Boolean revestimentoAcm = false;
    private Boolean pisoAluminioRecalcado = false;

    // 7. Área Externa e Toldos
    private Integer toldoBasculanteComprimentoMm = 0;
    private Integer toldoLateralArticuladoComprimentoMm = 0;

    // 8. Elétrica
    private Boolean alimentacaoExternaTrifasica = false;
    private Integer alimentacaoKva = 0;

    // 9. Listas Dinâmicas de Itens
    private List<ItemRequestDTO> itensBase = new ArrayList<>();
    private List<ItemEspecificoDTO> itensEspecificos = new ArrayList<>();
    private List<ItemEmbarcadoDTO> itensEmbarcados = new ArrayList<>();
}