package com.barraca.backend.controller;

import com.barraca.backend.dto.TipoBarracaDTO;
import com.barraca.backend.service.TipoBarracaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-barraca")
@CrossOrigin(origins = "*")
public class TipoBarracaController {

    private final TipoBarracaService service;

    public TipoBarracaController(TipoBarracaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TipoBarracaDTO> cadastrar(@Valid @RequestBody TipoBarracaDTO request) {
        TipoBarracaDTO response = service.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TipoBarracaDTO>> listar() {
        List<TipoBarracaDTO> list = service.listarTodos();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoBarracaDTO> buscarPorId(@PathVariable Long id) {
        TipoBarracaDTO response = service.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
