package br.senac.sp.tads.dsw.exemplo5.service;

import br.senac.sp.tads.dsw.exemplo5.controller.DepartamentoController;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.senac.sp.tads.dsw.exemplo5.dto.DepartamentoResponseDTO;
import br.senac.sp.tads.dsw.exemplo5.dto.DepartamentorequestDTO;
import br.senac.sp.tads.dsw.exemplo5.model.Departamento;
import br.senac.sp.tads.dsw.exemplo5.repository.DepartamentoRepository;

@Service
public class DepartamentoService {
    
    
    private final DepartamentoController departamentoController;
    private DepartamentoRepository repository;
    
    public DepartamentoService(DepartamentoRepository repository, DepartamentoController departamentoController) {
        this.repository = repository;
        this.departamentoController = departamentoController;
    }

    public List<DepartamentoResponseDTO> listarTodos() {
        return repository.findAll()
        .stream()
        .map(departamento -> new DepartamentoResponseDTO(
            departamento.getId(), departamento.getNome(),departamento.getOrcamento()))
            .toList();
    }

    public DepartamentoResponseDTO buscarPorId(Long id) {
        Departamento departamento = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "departamento não encontrado"));
        return new DepartamentoResponseDTO(departamento.getId(), departamento.getNome(), departamento.getOrcamento());
}

public DepartamentoResponseDTO criar(DepartamentorequestDTO dto) {
   Departamento departamento = new Departamento();
   departamento.setNome(dto.nome());
   departamento.setOrcamento(dto.orcamento());
   departamento = repository.save(departamento);
   return new DepartamentoResponseDTO(departamento.getId(), departamento.getNome(), departamento.getOrcamento());
}

public DepartamentoResponseDTO atualizar(Long id, DepartamentorequestDTO dto) {
    Departamento departamento = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "departamento não encontrado"));
    departamento.setNome(dto.nome());
    departamento.setOrcamento(dto.orcamento());
    departamento = repository.save(departamento);
    return new DepartamentoResponseDTO(departamento.getId(), departamento.getNome(), departamento.getOrcamento());
}

public void deletar(Long id) {
    Departamento departamento = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "departamento não encontrado"));
    repository.delete(departamento);
}}