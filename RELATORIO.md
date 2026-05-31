# Relatório Técnico — Barraca Tipo

## Migração de Aplicação Console para Web

**Disciplina:** Padrões de Projeto / Engenharia de Software  
**Grupo:** Estenio Gabriel, Gabriel Brandão, Guilherme

---

## 1. Padrões de Projeto Aplicados

### MVC (Model-View-Controller)

O projeto segue o padrão MVC de forma distribuída entre backend e frontend.

- **Model:** As entidades JPA (`Barraca.java`, `TipoBarraca.java`) representam os dados persistidos no banco H2.
- **Controller:** As classes `BarracaController.java` e `TipoBarracaController.java` recebem as requisições HTTP, delegam para os Services e devolvem a resposta ao cliente.
- **View:** O Angular atua como a camada de visualização, renderizando os componentes `BarracaListComponent` e `BarracaFormComponent` com base nos dados recebidos da API.

**Exemplo no código:**
```java
// BarracaController.java — recebe a requisição e delega ao Service
@PostMapping
public ResponseEntity<BarracaResponseDTO> cadastrar(@Valid @RequestBody BarracaRequestDTO request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
}
```

---

### Repository

O padrão Repository abstrai o acesso ao banco de dados, isolando a lógica de persistência das regras de negócio.

- `BarracaRepository` e `TipoBarracaRepository` estendem `JpaRepository`, fornecendo operações CRUD sem nenhum SQL manual.
- Métodos derivados como `existsByNome(String nome)` são gerados automaticamente pelo Spring Data JPA.

**Exemplo no código:**
```java
// BarracaRepository.java
public interface BarracaRepository extends JpaRepository<Barraca, Long> {
    boolean existsByNome(String nome);
    Optional<Barraca> findByNome(String nome);
}
```

---

### Service Layer

Toda a lógica de negócio está centralizada nos Services. Controllers não tomam nenhuma decisão de negócio — apenas recebem e respondem requisições.

Regras implementadas no `BarracaService`:
- Nome obrigatório com ao menos 3 caracteres.
- Nome único: ao criar, verifica se já existe outro com o mesmo nome; ao editar, exclui o próprio registro da verificação.
- O `TipoBarraca` informado deve existir no banco.

**Exemplo no código:**
```java
// BarracaService.java — regra de nome único
if (barracaRepository.existsByNome(dto.getNome())) {
    throw new RegraNegocioException("Já existe uma Barraca com o nome: " + dto.getNome());
}
```

---

### DTO (Data Transfer Object)

DTOs separam as entidades JPA da camada de comunicação HTTP. Isso evita expor estrutura interna do banco, permite validar entrada sem contaminar a entidade e controla quais campos são retornados na resposta.

- `BarracaRequestDTO` — entrada: contém os campos que o cliente envia.
- `BarracaResponseDTO` — saída: inclui o nome do tipo de barraca já resolvido, evitando que o frontend faça requisições extras.

**Exemplo no código:**
```java
// BarracaResponseDTO.java — retorna tipoBarracaNome diretamente
public BarracaResponseDTO(Long id, String nome, String descricao,
    String localizacao, boolean ativo, Long tipoBarracaId, String tipoBarracaNome) { ... }
```

---

### Observer — RxJS (Frontend Angular)

O Angular usa RxJS `Observables` para lidar com chamadas HTTP assíncronas. O componente se inscreve (`subscribe`) no Observable retornado pelo service e reage quando os dados chegam ou quando ocorre um erro, sem bloquear a interface.

**Exemplo no código:**
```typescript
// barraca-list.component.ts
this.barracaService.listar().subscribe({
  next: (dados) => this.barracas = dados,
  error: () => this.mensagemErro = 'Não foi possível carregar a lista.'
});
```

---

## 2. Principal Mudança na Migração

A maior diferença entre a versão console e a versão web é a **separação entre interface e lógica**. No console, tudo rodava no mesmo processo: o menu exibia dados e chamava os services diretamente. Na versão web, o frontend Angular e o backend Spring Boot são processos independentes que se comunicam via HTTP/JSON. Isso exigiu a criação dos DTOs, do tratamento de CORS e do `GlobalExceptionHandler` para padronizar erros.

---

## 3. Principais Dificuldades

- **Relacionamento JPA `@ManyToOne`:** Configurar o relacionamento entre `Barraca` e `TipoBarraca` sem gerar recursão infinita na serialização JSON exigiu o uso de DTOs no lugar das entidades diretamente.
- **CORS:** A configuração de `allowCredentials(true)` com wildcard `*` não é permitida pelo navegador; foi necessário especificar a origem `http://localhost:4200` explicitamente no `CorsConfig`.
- **Formulário híbrido no Angular:** Detectar se a rota possui um parâmetro `:id` para alternar entre modo de criação e edição no mesmo componente exigiu o uso do `ActivatedRoute`.

---

## 4. Melhorias Futuras

- Autenticação com Spring Security e JWT para proteger os endpoints.
- Paginação e filtros na listagem de barracas.
- Tela de gerenciamento de Tipos de Barraca no frontend.
- Troca do banco H2 (em memória) por PostgreSQL para persistência permanente.
- Testes automatizados com JUnit no backend e Jasmine/Karma no frontend.
