package com.barraca.backend.controller;

import com.barraca.backend.dto.BarracaRequestDTO;
import com.barraca.backend.dto.BarracaResponseDTO;
import com.barraca.backend.service.BarracaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/barracas")
@CrossOrigin(origins = "*")
public class BarracaController {

    private final BarracaService service;

    public BarracaController(BarracaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<BarracaResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BarracaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<BarracaResponseDTO> cadastrar(@Valid @RequestBody BarracaRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BarracaResponseDTO> atualizar(@PathVariable Long id,
                                                         @Valid @RequestBody BarracaRequestDTO request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
