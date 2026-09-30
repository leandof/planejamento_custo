package com.example.planejamento_custo.controller;

import com.example.planejamento_custo.dto.OrcamentoRequestDTO;
import com.example.planejamento_custo.entity.Orcamento;
import com.example.planejamento_custo.service.OrcamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orcamentos")
@RequiredArgsConstructor
public class OrcamentoController {

    private final OrcamentoService orcamentoService;

    @PostMapping
    public ResponseEntity<Orcamento> criar(@RequestBody @Valid OrcamentoRequestDTO dto) {
        Orcamento orcamento = orcamentoService.criarEProcessar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(orcamento);
    }
}