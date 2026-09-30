package com.example.planejamento_custo.service;

import com.example.planejamento_custo.dto.ImportacaoPrecoInsumoDTO;
import com.example.planejamento_custo.entity.Insumo;
import com.example.planejamento_custo.repository.InsumoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InsumoService {

    private final InsumoRepository insumoRepository;

    // Método exigido pelo InsumoController no POST /api/insumos
    @Transactional
    public Insumo salvar(Insumo insumo) {
        if (insumo.getDataAtualizacao() == null) {
            insumo.setDataAtualizacao(LocalDateTime.now());
        }
        return insumoRepository.save(insumo);
    }

    // Método exigido pelo InsumoController no GET /api/insumos
    @Transactional(readOnly = true)
    public List<Insumo> listarTodos() {
        return insumoRepository.findAll();
    }

    // Busca por ID
    @Transactional(readOnly = true)
    public Optional<Insumo> buscarPorId(Long id) {
        return insumoRepository.findById(id);
    }

    // Exclusão de insumo
    @Transactional
    public void deletar(Long id) {
        insumoRepository.deleteById(id);
    }

    // Importação/Atualização por DTO
    @Transactional
    public Insumo importarOuAtualizarPreco(ImportacaoPrecoInsumoDTO dto) {
        Insumo insumo = insumoRepository.findByCodigoInsumo(dto.getCodigoInsumo())
                .orElseGet(() -> Insumo.builder()
                        .codigoInsumo(dto.getCodigoInsumo())
                        .build());

        insumo.setNome(dto.getNome());
        insumo.setPrecoBase(dto.getPrecoBase());
        insumo.setUnidadeMedida(dto.getUnidadeMedida());
        insumo.setFatorConversao(dto.getFatorConversao() != null ? dto.getFatorConversao() : BigDecimal.ONE);
        insumo.setFornecedor(dto.getFornecedor());
        insumo.setDataAtualizacao(LocalDateTime.now());

        return insumoRepository.save(insumo);
    }
}