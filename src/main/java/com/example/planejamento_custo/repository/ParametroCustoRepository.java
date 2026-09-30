package com.example.planejamento_custo.repository;

import com.example.planejamento_custo.entity.ParametroCusto;
import com.example.planejamento_custo.entity.TipoProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParametroCustoRepository extends JpaRepository<ParametroCusto, Long> {

    // Adicione esta linha para resolver o erro 'Cannot resolve method findByTipoProduto'
    Optional<ParametroCusto> findByTipoProduto(TipoProduto tipoProduto);
}