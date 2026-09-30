package com.example.planejamento_custo.entity;

public enum TipoProduto {
    SEMIRREBOQUE(0.14),      // GGF 14%
    SOBRECHASSI(0.12),       // GGF 12%
    VUC(0.10),               // GGF 10%
    REBOQUE(0.11),           // GGF 11%
    MODULO_CONTEINER(0.15);  // GGF 15%

    private final double percentualGgfPadrao;

    TipoProduto(double percentualGgfPadrao) {
        this.percentualGgfPadrao = percentualGgfPadrao;
    }

    public double getPercentualGgfPadrao() {
        return percentualGgfPadrao;
    }
}