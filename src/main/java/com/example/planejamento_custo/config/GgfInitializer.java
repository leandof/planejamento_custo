package com.example.planejamento_custo.config;

import com.example.planejamento_custo.entity.GgfCaracteristica;
import com.example.planejamento_custo.entity.TipoProduto;
import com.example.planejamento_custo.repository.GgfCaracteristicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class GgfInitializer implements CommandLineRunner {

    @Autowired
    private GgfCaracteristicaRepository repository;

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() == 0) {
            for (TipoProduto tp : TipoProduto.values()) {
                criar(tp, "Maleiro", 0.10);
                criar(tp, "1 Porta Palco", 0.10);
                criar(tp, "2 Portas Palco", 0.20);
                criar(tp, "1 Avanço", 0.10);
                criar(tp, "2 Avanços", 0.20);
                criar(tp, "Teto Deck", 0.10);
                criar(tp, "PNE", 0.10);
                criar(tp, "Interior Padrão", 0.20);
                criar(tp, "Interior Master", 0.40);
                criar(tp, "Suspensão Inloader", 0.40);
                criar(tp, "Double Deck", 0.80);
                criar(tp, "Aviônica Hidráulica", 0.10);
                criar(tp, "Toldo", 0.10);
                criar(tp, "Porta Roll Up", 0.10);
                criar(tp, "Elevador Double Deck", 0.10);
            }
        }
    }

    private void criar(TipoProduto tp, String nome, double pontos) {
        GgfCaracteristica c = new GgfCaracteristica();
        c.setTipoProduto(tp);
        c.setNomeCaracteristica(nome);
        c.setPontuacaoPadrao(BigDecimal.valueOf(pontos));
        c.setObrigatorioBase(false);
        repository.save(c);
    }
}