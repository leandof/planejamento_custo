package com.example.planejamento_custo.controller;

import com.example.planejamento_custo.entity.Insumo;
import com.example.planejamento_custo.service.InsumoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/insumos")
public class InsumoController {

    @Autowired
    private InsumoService service;

    @PostMapping
    public ResponseEntity<Insumo> criar(@RequestBody Insumo insumo) {
        Insumo insumoSalvo = service.salvar(insumo);
        return ResponseEntity.status(HttpStatus.CREATED).body(insumoSalvo);
    }

    @GetMapping
    public ResponseEntity<List<Insumo>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }
}