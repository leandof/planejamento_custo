package com.example.planejamento_custo.repository;

import com.example.planejamento_custo.entity.GgfCaracteristica;
import com.example.planejamento_custo.entity.TipoProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GgfCaracteristicaRepository extends JpaRepository<GgfCaracteristica, Long> {
    List<GgfCaracteristica> findByTipoProduto(TipoProduto tipoProduto);
}