package com.example.planejamento_custo.service.strategy;

import com.example.planejamento_custo.entity.TipoProduto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CalculoProdutoFactory {

    private final Map<TipoProduto, CalculoProdutoStrategy> strategies;

    @Autowired
    public CalculoProdutoFactory(List<CalculoProdutoStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(CalculoProdutoStrategy::getTipoProduto, Function.identity()));
    }

    public CalculoProdutoStrategy getStrategy(TipoProduto tipoProduto) {
        CalculoProdutoStrategy strategy = strategies.get(tipoProduto);
        if (strategy == null) {
            // Caso não haja strategy específica implementada, utiliza a genérica/default
            return strategies.get(TipoProduto.SEMIRREBOQUE);
        }
        return strategy;
    }
}