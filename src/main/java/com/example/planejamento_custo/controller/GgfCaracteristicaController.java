package com.example.planejamento_custo.controller;

import com.example.planejamento_custo.entity.GgfCaracteristica;
import com.example.planejamento_custo.entity.TipoProduto;
import com.example.planejamento_custo.repository.GgfCaracteristicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ggf-caracteristicas")
@CrossOrigin(origins = "*")
public class GgfCaracteristicaController {

    @Autowired
    private GgfCaracteristicaRepository repository;

    @GetMapping
    public List<GgfCaracteristica> listarPorTipo(@RequestParam TipoProduto tipoProduto) {
        return repository.findByTipoProduto(tipoProduto);
    }
}