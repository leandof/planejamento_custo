package com.example.planejamento_custo.controller;

import com.example.planejamento_custo.entity.ParametroCusto;
import com.example.planejamento_custo.entity.TipoProduto;
import com.example.planejamento_custo.repository.ParametroCustoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parametros")
@RequiredArgsConstructor
public class ParametroCustoController {

    private final ParametroCustoRepository repository;

    @GetMapping("/listar")
    public List<ParametroCusto> listar() {
        return repository.findAll();
    }

    @GetMapping("/{tipoProduto}")
    public ResponseEntity<ParametroCusto> buscarPorTipo(@PathVariable TipoProduto tipoProduto) {
        return repository.findByTipoProduto(tipoProduto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/atualizar")
    public ParametroCusto atualizar(@RequestBody ParametroCusto request) {
        ParametroCusto p = repository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Parâmetro não encontrado"));

        p.setTipoProduto(request.getTipoProduto());
        p.setAliquotaIcms(request.getAliquotaIcms());
        p.setAliquotaPis(request.getAliquotaPis());
        p.setAliquotaCofins(request.getAliquotaCofins());
        p.setPercentualComissao(request.getPercentualComissao());
        p.setPercentualGgf(request.getPercentualGgf());
        p.setPercentualMargemLucro(request.getPercentualMargemLucro());
        p.setPercentualCreditoIcmsInsumo(request.getPercentualCreditoIcmsInsumo());
        p.setPercentualCreditoPisCofinsInsumo(request.getPercentualCreditoPisCofinsInsumo());

        return repository.save(p);
    }
}