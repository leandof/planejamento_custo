package com.example.planejamento_custo.controller;

import com.example.planejamento_custo.dto.DreResponseDTO;
import com.example.planejamento_custo.dto.OrcamentoRequestDTO;
import com.example.planejamento_custo.entity.Orcamento;
import com.example.planejamento_custo.repository.OrcamentoRepository;
import com.example.planejamento_custo.service.OrcamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orcamentos")
@CrossOrigin(origins = "*")
public class OrcamentoController {

    @Autowired
    private OrcamentoService orcamentoService;

    @Autowired
    private OrcamentoRepository orcamentoRepository;

    @PostMapping("/simular")
    public DreResponseDTO simular(@RequestBody OrcamentoRequestDTO request) {
        return orcamentoService.simularDre(request);
    }

    @PostMapping("/salvar")
    public Orcamento salvar(@RequestBody OrcamentoRequestDTO request) {
        return orcamentoService.salvarOrcamentoDefinitivo(request);
    }

    @GetMapping("/listar")
    public List<Orcamento> listar() {
        return orcamentoRepository.findAll();
    }
}