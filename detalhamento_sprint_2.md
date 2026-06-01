# Detalhamento de Tarefas: Sprint 2
## Projeto: Barraca Tipo (Migração Web)

Este documento detalha o planejamento das atividades, os papéis individuais e fornece os exemplos práticos de código para o **Sprint 2** do projeto **Barraca Tipo**. O objetivo é orientar cada membro do trio (Integrantes A, B e C) na divisão justa das responsabilidades técnicas e acadêmicas para colocar a API REST em funcionamento com Spring Boot e banco de dados H2.

---

## 📅 Sprint 2: CRUD REST com Spring Boot
**Período:** 25/05 a 31/05  
**Foco:** Fazer o banco de dados em memória e as rotas da API funcionarem com persistência real em JPA.

---

### 👤 Integrante A: Configuração do Banco e Domínio do Tipo de Barraca

Este integrante é responsável pela fundação de persistência e pela modelagem e exposição dos tipos de barraca na API REST.

#### Atividades Detalhadas
1. **Configuração do H2 Database:** Configurar a conexão com o banco H2 em memória no arquivo `application.properties`.
2. **Entidade JPA `TipoBarraca.java`:** Criar a classe de domínio mapeada para o banco de dados usando as anotações do JPA (`@Entity`, `@Table`, `@Id`, `@GeneratedValue`).
3. **Interface `TipoBarracaRepository.java`:** Criar a interface que estende `JpaRepository` para automatizar as operações do banco.
4. **DTO de TipoBarraca:** Criar a classe para transferência de dados do Tipo de Barraca.
5. **Serviço `TipoBarracaService.java`:** Implementar as validações (como nome único e busca por ID).
6. **Controlador `TipoBarracaController.java`:** Implementar as rotas HTTP GET, POST e DELETE.

#### Exemplos e Dicas de Código

##### Configuração do H2 (`application.properties`)
```properties
spring.application.name=backend
spring.datasource.url=jdbc:h2:mem:barracadb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.jpa.hibernate.ddl-auto=update
```

##### Entidade `TipoBarraca.java`
```java
package com.barraca.backend.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "tipo_barraca")
public class TipoBarraca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    private String descricao;

    public TipoBarraca() {}

    public TipoBarraca(Long id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
    }

    // Getters and Setters
}
```

##### Interface `TipoBarracaRepository.java`
```java
package com.barraca.backend.repository;

import com.barraca.backend.domain.TipoBarraca;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TipoBarracaRepository extends JpaRepository<TipoBarraca, Long> {
    Optional<TipoBarraca> findByNome(String nome);
    boolean existsByNome(String nome);
}
```

---

### 👤 Integrante B: Modelagem de Barraca, DTOs e Exceções

Este integrante é responsável pela modelagem de dados da Barraca (com seus relacionamentos) e pelas estruturas de transferência de dados e tratamento de exceções básicas.

#### Atividades Detalhadas
1. **Entidade JPA `Barraca.java`:** Criar o domínio de Barraca mapeado com relacionamento `@ManyToOne` para `TipoBarraca`.
2. **Interface `BarracaRepository.java`:** Criar a interface de repositório Spring Data JPA correspondente.
3. **DTOs de Entrada e Saída:** Criar `BarracaRequestDTO` (para recebimento) e `BarracaResponseDTO` (para retorno), garantindo o desacoplamento da entidade do banco.
4. **Classe de Exceção `RegraNegocioException.java`:** Criar a exceção de tempo de execução customizada.

#### Exemplos e Dicas de Código

##### Entidade `Barraca.java`
```java
package com.barraca.backend.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "barraca")
public class Barraca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    private String descricao;
    private String localizacao;
    private boolean ativo = true;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tipo_barraca_id", nullable = false)
    private TipoBarraca tipoBarraca;

    public Barraca() {}

    // Getters and Setters
}
```

##### DTO de Entrada (`BarracaRequestDTO.java`)
```java
package com.barraca.backend.dto;

public class BarracaRequestDTO {
    private String nome;
    private String descricao;
    private String localizacao;
    private boolean ativo;
    private Long tipoBarracaId;

    // Getters and Setters
}
```

##### DTO de Saída (`BarracaResponseDTO.java`)
```java
package com.barraca.backend.dto;

public class BarracaResponseDTO {
    private Long id;
    private String nome;
    private String descricao;
    private String localizacao;
    private boolean ativo;
    private Long tipoBarracaId;
    private String tipoBarracaNome;

    // Getters and Setters
}
```

##### Exceção Customizada (`RegraNegocioException.java`)
```java
package com.barraca.backend.exception;

public class RegraNegocioException extends RuntimeException {
    public RegraNegocioException(String message) {
        super(message);
    }
}
```

---

### 👤 Integrante C: Lógica de Negócio, Rotas de Barraca e Testes

Este integrante é responsável pela inteligência do cadastro de Barracas e pela exposição REST completa das rotas da entidade, além de coordenar a validação do servidor.

#### Atividades Detalhadas
1. **Lógica de Negócio (`BarracaService.java`):**
   - Consumir regras de negócio para assegurar que não haja barracas repetidas por nome.
   - Validar se o tipo de barraca fornecido no DTO existe no banco antes da associação.
   - Traduzir os dados entre DTOs e Entidades.
2. **Controlador REST (`BarracaController.java`):**
   - Expor as rotas `GET /api/barracas` (listar todas) e `GET /api/barracas/{id}` (detalhar por ID).
   - Expor a rota `POST /api/barracas` (cadastrar).
   - Expor a rota `PUT /api/barracas/{id}` (editar).
   - Expor a rota `DELETE /api/barracas/{id}` (remover).
3. **Servidor e Testes Manuais:** Executar o projeto (`mvnw.cmd spring-boot:run`) e realizar a bateria de testes usando o Postman ou Insomnia.

#### Exemplos e Dicas de Código

##### Classe de Serviço (`BarracaService.java`)
```java
package com.barraca.backend.service;

import com.barraca.backend.domain.Barraca;
import com.barraca.backend.domain.TipoBarraca;
import com.barraca.backend.dto.BarracaRequestDTO;
import com.barraca.backend.dto.BarracaResponseDTO;
import com.barraca.backend.exception.RegraNegocioException;
import com.barraca.backend.repository.BarracaRepository;
import com.barraca.backend.repository.TipoBarracaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BarracaService {

    private final BarracaRepository barracaRepository;
    private final TipoBarracaRepository tipoRepository;

    public BarracaService(BarracaRepository barracaRepository, TipoBarracaRepository tipoRepository) {
        this.barracaRepository = barracaRepository;
        this.tipoRepository = tipoRepository;
    }

    @Transactional
    public BarracaResponseDTO criar(BarracaRequestDTO request) {
        if (barracaRepository.existsByNome(request.getNome())) {
            throw new RegraNegocioException("Já existe uma Barraca com este nome.");
        }

        TipoBarraca tipo = tipoRepository.findById(request.getTipoBarracaId())
                .orElseThrow(() -> new RegraNegocioException("TipoBarraca não encontrado com ID: " + request.getTipoBarracaId()));

        Barraca barraca = new Barraca();
        barraca.setNome(request.getNome());
        barraca.setDescricao(request.getDescricao());
        barraca.setLocalizacao(request.getLocalizacao());
        barraca.setAtivo(request.isAtivo());
        barraca.setTipoBarraca(tipo);

        barraca = barracaRepository.save(barraca);
        return converterParaResponseDTO(barraca);
    }

    @Transactional
    public BarracaResponseDTO atualizar(Long id, BarracaRequestDTO request) {
        Barraca barraca = barracaRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Barraca não encontrada com ID: " + id));

        if (barracaRepository.existsByNomeAndIdNot(request.getNome(), id)) {
            throw new RegraNegocioException("Já existe outra Barraca com este nome.");
        }

        TipoBarraca tipo = tipoRepository.findById(request.getTipoBarracaId())
                .orElseThrow(() -> new RegraNegocioException("TipoBarraca não encontrado com ID: " + request.getTipoBarracaId()));

        barraca.setNome(request.getNome());
        barraca.setDescricao(request.getDescricao());
        barraca.setLocalizacao(request.getLocalizacao());
        barraca.setAtivo(request.isAtivo());
        barraca.setTipoBarraca(tipo);

        barraca = barracaRepository.save(barraca);
        return converterParaResponseDTO(barraca);
    }

    public BarracaResponseDTO buscarPorId(Long id) {
        Barraca barraca = barracaRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Barraca não encontrada com ID: " + id));
        return converterParaResponseDTO(barraca);
    }

    public List<BarracaResponseDTO> listarTodas() {
        return barracaRepository.findAll().stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void remover(Long id) {
        if (!barracaRepository.existsById(id)) {
            throw new RegraNegocioException("Barraca não encontrada com ID: " + id);
        }
        barracaRepository.deleteById(id);
    }

    private BarracaResponseDTO converterParaResponseDTO(Barraca barraca) {
        BarracaResponseDTO response = new BarracaResponseDTO();
        response.setId(barraca.getId());
        response.setNome(barraca.getNome());
        response.setDescricao(barraca.getDescricao());
        response.setLocalizacao(barraca.getLocalizacao());
        response.setAtivo(barraca.isAtivo());
        response.setTipoBarracaId(barraca.getTipoBarraca().getId());
        response.setTipoBarracaNome(barraca.getTipoBarraca().getNome());
        return response;
    }
}
```

##### Controlador REST (`BarracaController.java`)
```java
package com.barraca.backend.controller;

import com.barraca.backend.dto.BarracaRequestDTO;
import com.barraca.backend.dto.BarracaResponseDTO;
import com.barraca.backend.service.BarracaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/barracas")
@CrossOrigin(origins = "*")
public class BarracaController {

    private final BarracaService barracaService;

    public BarracaController(BarracaService barracaService) {
        this.barracaService = barracaService;
    }

    @PostMapping
    public ResponseEntity<BarracaResponseDTO> cadastrar(@RequestBody BarracaRequestDTO request) {
        BarracaResponseDTO response = barracaService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<BarracaResponseDTO>> listar() {
        List<BarracaResponseDTO> list = barracaService.listarTodas();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BarracaResponseDTO> buscarPorId(@PathVariable Long id) {
        BarracaResponseDTO response = barracaService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BarracaResponseDTO> atualizar(@PathVariable Long id, @RequestBody BarracaRequestDTO request) {
        BarracaResponseDTO response = barracaService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        barracaService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
```

---

## 🛠️ Roteiro de Testes Manuais (Postman/Insomnia)

### Passo 1: Iniciar o Servidor
Execute o seguinte comando na pasta `backend`:
```powershell
./mvnw.cmd spring-boot:run
```

### Passo 2: Cadastrar Tipos de Barraca (POST /api/tipos-barraca)
* **URL:** `http://localhost:8080/api/tipos-barraca`
* **JSON:**
```json
{
  "nome": "Alimentação",
  "descricao": "Barracas que vendem refeições, salgados e doces"
}
```

### Passo 3: Cadastrar uma Barraca (POST /api/barracas)
* **URL:** `http://localhost:8080/api/barracas`
* **JSON:**
```json
{
  "nome": "Pastelaria do Gabriel",
  "descricao": "Pasteis de feira crocantes",
  "localizacao": "Setor Azul, Barraca 12",
  "ativo": true,
  "tipoBarracaId": 1
}
```

### Passo 4: Listar todas as Barracas (GET /api/barracas)
* **URL:** `http://localhost:8080/api/barracas`

### Passo 5: Atualizar uma Barraca (PUT /api/barracas/1)
* **URL:** `http://localhost:8080/api/barracas/1`
* **JSON:**
```json
{
  "nome": "Pastelaria Gourmet Gabriel",
  "descricao": "Pasteis premium",
  "localizacao": "Setor Azul, Barraca 12A",
  "ativo": true,
  "tipoBarracaId": 1
}
```

### Passo 6: Excluir uma Barraca (DELETE /api/barracas/1)
* **URL:** `http://localhost:8080/api/barracas/1`
