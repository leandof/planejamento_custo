package com.example.planejamento_custo.repository;

import com.example.planejamento_custo.entity.OrcamentoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrcamentoItemRepository extends JpaRepository<OrcamentoItem, Long> {
}