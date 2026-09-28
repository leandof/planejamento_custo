package com.example.planejamento_custo.service;

import com.example.planejamento_custo.entity.Insumo;
import com.example.planejamento_custo.repository.InsumoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InsumoService {

    @Autowired
    private InsumoRepository repository;

    public Insumo salvar(Insumo insumo) {
        return repository.save(insumo);
    }

    public List<Insumo> listarTodos() {
        return repository.findAll();
    }
}
