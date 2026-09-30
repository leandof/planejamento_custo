package com.example.planejamento_custo.config;

import com.example.planejamento_custo.entity.ParametroCusto;
import com.example.planejamento_custo.entity.TipoProduto;
import com.example.planejamento_custo.repository.ParametroCustoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class ParametroInicializador {

    @Bean
    public CommandLineRunner initParametros(ParametroCustoRepository repository) {
        return args -> {
            for (TipoProduto tipo : TipoProduto.values()) {
                salvarSeNaoExistir(repository, tipo);
            }
        };
    }

    private void salvarSeNaoExistir(ParametroCustoRepository repo, TipoProduto tipo) {
        boolean existe = repo.findByTipoProduto(tipo).isPresent();
        if (!existe) {
            ParametroCusto p = ParametroCusto.builder()
                    .tipoProduto(tipo)
                    .aliquotaIcms(new BigDecimal("0.18"))
                    .aliquotaPis(new BigDecimal("0.0165"))
                    .aliquotaCofins(new BigDecimal("0.0760"))
                    .percentualComissao(new BigDecimal("0.03"))
                    .percentualGgf(new BigDecimal(String.valueOf(tipo.getPercentualGgfPadrao())))
                    .percentualMargemLucro(new BigDecimal("0.15"))
                    .percentualCreditoIcmsInsumo(new BigDecimal("0.12"))
                    .percentualCreditoPisCofinsInsumo(new BigDecimal("0.0925"))
                    .build();

            repo.save(p);
        }
    }
}