package com.example.planejamento_custo.config;

import com.example.planejamento_custo.entity.ParametroCusto;
import com.example.planejamento_custo.repository.ParametroCustoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.Optional;

@Configuration
public class ParametroInicializador {

    @Bean
    CommandLineRunner initParametros(ParametroCustoRepository repo) {
        return args -> {
            salvarSeNaoExistir(repo, "VALOR_HORA_TRABALHO", "40.90", "Custo médio Mão de Obra");
            salvarSeNaoExistir(repo, "CUSTO_PISO_ALUMINIO_M2", "450.00", "Custo do m² de Piso de Alumínio");
            salvarSeNaoExistir(repo, "CUSTO_REVESTIMENTO_ACM_M2", "320.00", "Custo do m² de ACM");
            salvarSeNaoExistir(repo, "CUSTO_TOLDO_FABRICADO_M", "850.00", "Custo metro linear Toldo Truckvan");
            salvarSeNaoExistir(repo, "CUSTO_TOLDO_COMPRADO_M", "1200.00", "Custo metro linear Toldo Terceiros");
            salvarSeNaoExistir(repo, "CUSTO_JANELA", "1500.00", "Custo unitário da Janela");
            salvarSeNaoExistir(repo, "CUSTO_BANHEIRO_COMPLETO", "8500.00", "Custo Banheiro Completo Padrão");
            salvarSeNaoExistir(repo, "CUSTO_HIDROSSANITARIO", "4200.00", "Custo Sistema Hidrossanitário");
            salvarSeNaoExistir(repo, "CUSTO_DIVISORIA", "1100.00", "Custo unitário Divisória");
            salvarSeNaoExistir(repo, "CUSTO_QUADRO_TRASEIRO", "4200.00", "Custo Quadro Traseiro com Portas");
            salvarSeNaoExistir(repo, "CUSTO_KVA_ADICIONAL", "150.00", "Custo de infra elétrica por kVA adicional");
        };
    }

    private void salvarSeNaoExistir(ParametroCustoRepository repo, String chave, String valor, String desc) {
        // Se a chave não existir na Base de Dados, ele cria. Se já existir, ele não substitui o que o gestor editou.
        boolean existe = repo.findAll().stream().anyMatch(p -> p.getChave().equals(chave));
        if (!existe) {
            ParametroCusto p = new ParametroCusto();
            p.setChave(chave);
            p.setValor(new BigDecimal(valor));
            p.setDescricao(desc);
            repo.save(p);
        }
    }
}