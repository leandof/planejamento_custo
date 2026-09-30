package com.example.planejamento_custo.repository;

import com.example.planejamento_custo.entity.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InsumoRepository extends JpaRepository<Insumo, Long> {

    // Resolve o erro 'Cannot resolve method findByCodigoInsumo'
    Optional<Insumo> findByCodigoInsumo(String codigoInsumo);
}