package com.example.planejamento_custo.repository;

import com.example.planejamento_custo.entity.ParametroCusto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParametroCustoRepository extends JpaRepository<ParametroCusto, Long> {
    // Não é necessário escrever código aqui, o Spring Boot já traz os métodos de busca prontos
}
