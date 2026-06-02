# Padrões de Projeto Identificados

## Backend (Spring Boot)

### MVC (Model-View-Controller)
Separação em camadas: **Controller** recebe requisições HTTP, **Service** contém a lógica de negócio, **Repository** acessa dados e **Domain** representa as entidades.
- `controller/BarracaController.java` e `controller/TipoBarracaController.java`
- `service/BarracaService.java` e `service/TipoBarracaService.java`
- `domain/Barraca.java` e `domain/TipoBarraca.java`

### Repository
Abstrai o acesso ao banco de dados behind interfaces que estendem `JpaRepository`. O Spring gera as implementações automaticamente.
- `repository/BarracaRepository.java` — métodos como `findByNome()` e `existsByNome()`
- `repository/TipoBarracaRepository.java` — método `existsByNome()`

### DTO (Data Transfer Object)
Objetos separados para transferência de dados entre camadas, desacoplando o contrato da API das entidades JPA.
- `dto/BarracaRequestDTO.java` — dados de entrada com validação (`@NotBlank`, `@Size`, `@NotNull`)
- `dto/BarracaResponseDTO.java` — dados de saída com `tipoBarracaNome` achatado
- `dto/TipoBarracaDTO.java` — DTO bidirecional

### Service Layer (Facade de Negócio)
Encapsula todas as regras de negócio: validação de unicidade de nome, integridade referencial e conversão entidade-DTO. Controllers não contêm lógica de negócio.
- `service/BarracaService.java` — CRUD + validações (nome duplicado, tamanho, referência a tipo)
- `service/TipoBarracaService.java` — CRUD + verificação de barracas vinculadas

### Dependency Injection (Injeção por Construtor)
Todas as dependências são injetadas via construtor com campos `final`. O Spring resolve e injeta automaticamente.
- `BarracaService` recebe `BarracaRepository` e `TipoBarracaRepository`
- `BarracaController` recebe `BarracaService`

### Singleton
Todos os beans do Spring (`@RestController`, `@Service`, `@Component`, `@Configuration`) possuem escopo singleton por padrão — uma única instância gerenciada pelo container IoC.

### Front Controller
O `DispatcherServlet` do Spring centraliza todas as requisições HTTP e as roteia para os controllers com base nas anotações `@RequestMapping`, `@GetMapping`, `@PostMapping`, etc.

### Global Exception Handler (Chain of Responsibility)
`@RestControllerAdvice` intercepta exceções de qualquer controller e as trata de forma centralizada.
- `exception/GlobalExceptionHandler.java` — trata `MethodArgumentNotValidException` (HTTP 400) e `RegraNegocioException` (HTTP 422)
- `exception/RegraNegocioException.java` — exceção customizada para violações de regra de negócio

### Proxy
Spring Data JPA gera implementações proxy para as interfaces de repositório em tempo de execução. `@Transactional` usa proxies AOP para envolver métodos em transações de banco de dados.

### Factory Method
A anotação `@Bean` em `CorsConfig.corsConfigurer()` indica ao Spring para chamar o método e registrar o objeto retornado como bean.

### Builder (Fluent API)
`ResponseEntity` usa API fluente para construir respostas HTTP:
```java
ResponseEntity.status(HttpStatus.CREATED).body(response)
ResponseEntity.ok(data)
ResponseEntity.noContent().build()
```

### Decorator (Validação por Anotações)
Anotações do Bean Validation (`@NotBlank`, `@Size`, `@NotNull`) decoram campos dos DTOs com restrições. `@Valid` nos controllers dispara a cadeia de validação.

### Command
`DbLoader` implementa `CommandLineRunner`, encapsulando um comando (popular o banco com dados iniciais) executado automaticamente pelo Spring após o startup.

### Template Method
`JpaRepository` define métodos-template como `findAll()`, `findById()`, `save()`, `deleteById()` — o esqueleto do algoritmo é fixo, o tipo da entidade é o ponto de variação.

---

## Frontend (Angular)

### Component (MVVM)
Cada componente combina uma classe TypeScript (controller/viewmodel), um template HTML (view) e CSS. O decorator `@Component` une as três partes.
- `barraca-list.component.ts` — listagem com tabela e exclusão
- `barraca-form.component.ts` — formulário de criação/edição

### Service (HTTP Proxy)
Serviços encapsulam toda comunicação com a API REST. Componentes nunca chamam `HttpClient` diretamente.
- `barraca.service.ts` — 5 métodos CRUD (`listar`, `buscarPorId`, `criar`, `atualizar`, `deletar`)
- `tipo-barraca.service.ts` — 4 métodos (`listar`, `buscarPorId`, `criar`, `atualizar`)

### Singleton (Angular DI)
`@Injectable({ providedIn: 'root' })` registra cada serviço como singleton no injetor raiz — uma única instância compartilhada por todos os componentes.

### Dependency Injection (`inject()`)
A função moderna `inject()` do Angular resolve dependências do container de DI:
```typescript
inject(BarracaService)
inject(FormBuilder)
inject(ActivatedRoute)
```

### Observer (RxJS Observables)
Serviços retornam `Observable<T>`. Componentes se inscrevem com `.subscribe()` para receber dados ou tratar erros.
```typescript
barracaService.listar().subscribe({
  next: (dados) => { ... },
  error: (err) => { ... }
})
```

### Router (Front Controller)
Angular Router centraliza a navegação client-side. Rotas definidas em `app.routes.ts`:
- `/barracas` → `BarracaListComponent`
- `/barracas/nova` e `/barracas/editar/:id` → `BarracaFormComponent`

### Builder (Reactive Forms)
`FormBuilder.group()` constrói formulários reativos com API fluente:
```typescript
this.form = this.fb.group({
  nome: ['', [Validators.required, Validators.minLength(3)]]
})
```

### Mediator
`BarracaFormComponent` coordena múltiplos colaboradores: `FormBuilder`, `BarracaService`, `TipoBarracaService`, `ActivatedRoute`, `Router` e `ChangeDetectorRef` — nenhum deles precisa conhecer os outros.

---

## Aplicação Console (Legado)

### Repository (Manual)
Interfaces de repositório com implementação concreta baseada em arquivos JSON, sem framework.
- `repository/BarracaRepository.java` (interface)
- `repositoryJson/BarracaRepositoryJson.java` (implementação com cache em memória e persistência em `data/barracas.json`)

### Strategy (Persistência Intercambiável)
A interface `BarracaRepository` define uma família de algoritmos de persistência. `BarracaRepositoryJson` é uma estratégia concreta que pode ser substituída por outra (ex: SQLite) sem alterar services ou controllers.

### Dependency Injection Manual (Composition Root)
Toda a composição é feita manualmente em `Main.java`: repositórios são instanciados, passados para services, e services para o controller.

### Domain Model com Autovalidação
Entidades de domínio contêm sua própria lógica de validação:
- `Barraca.validar()` — verifica tamanho do nome e associação com tipo
- `TipoBarraca.validar()` — verifica nome obrigatório
