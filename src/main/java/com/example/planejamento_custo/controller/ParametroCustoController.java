package com.example.planejamento_custo.controller;

import com.example.planejamento_custo.entity.ParametroCusto;
import com.example.planejamento_custo.repository.ParametroCustoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parametros")
@CrossOrigin(origins = "*")
public class ParametroCustoController {

    @Autowired
    private ParametroCustoRepository repository;

    @GetMapping("/listar")
    public List<ParametroCusto> listar() {
        return repository.findAll();
    }

    @PostMapping("/atualizar")
    public ParametroCusto atualizar(@RequestBody ParametroCusto request) {
        ParametroCusto p = repository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Parâmetro não encontrado"));
        p.setValor(request.getValor());
        return repository.save(p);
    }
}