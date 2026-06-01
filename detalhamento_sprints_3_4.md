# Detalhamento de Tarefas: Sprints 3 & 4
## Projeto: Barraca Tipo (Migração Web)

Este documento detalha o planejamento das atividades, os papéis individuais e fornece exemplos práticos de código para os **Sprints 3 e 4** do projeto **Barraca Tipo**. O objetivo é orientar cada membro do trio (Integrantes A, B e C) na divisão justa das responsabilidades técnicas e acadêmicas.

---

## 📅 Sprint 3: Validações, CORS e Fundação do Frontend
**Período:** 01/06 a 07/06  
**Foco:** Garantir segurança de entrada na API REST (validações), habilitar comunicação cross-origin (CORS) e criar a estrutura base do frontend Angular.

### 👤 Integrante A: Tratamento de Erros e Validação no Backend
Este integrante é responsável por proteger a integridade dos dados que entram pela API e estruturar um retorno elegante e inteligível para o cliente no caso de falhas ou violações de regras.

#### Atividades Detalhadas
1. **Bean Validation nos DTOs:** Adicionar anotações de validação do Jakarta Bean Validation nos DTOs de entrada (`TipoBarracaDTO.java` e `BarracaRequestDTO.java`).
2. **Ativação da Validação nos Controllers:** Inserir a anotação `@Valid` antes do corpo da requisição (`@RequestBody`) nos métodos de cadastro (`create`) e edição (`update`) em `TipoBarracaController.java` e `BarracaController.java`.
3. **Criação do Handler Global de Exceções:** Implementar uma classe `GlobalExceptionHandler` anotada com `@RestControllerAdvice` para capturar exceções automaticamente e retornar respostas JSON padronizadas com códigos HTTP corretos.

#### Exemplos e Dicas de Código

##### Exemplo de DTO Validado (`BarracaRequestDTO.java`)
```java
package br.com.barracatipo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class BarracaRequestDTO {

    @NotBlank(message = "O nome da barraca é obrigatório.")
    @Size(min = 3, max = 100, message = "O nome deve conter entre 3 e 100 caracteres.")
    private String nome;

    @NotBlank(message = "A localização da barraca é obrigatória.")
    private String localizacao;

    @NotNull(message = "O tipo da barraca deve ser informado.")
    private Long tipoBarracaId;

    // Getters, Setters e Construtores
}
```

##### Exemplo de GlobalExceptionHandler (`GlobalExceptionHandler.java`)
```java
package br.com.barracatipo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Captura erros do Bean Validation (HTTP 400 - Bad Request)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Erro de Validação");
        response.put("fields", errors);

        return ResponseEntity.badRequest().body(response);
    }

    // 2. Captura exceções de Regra de Negócio Customizadas (HTTP 422 - Unprocessable Entity)
    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<Map<String, Object>> handleRegraNegocio(RegraNegocioException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());
        response.put("error", "Violação de Regra de Negócio");
        response.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }
}
```

---

### 👤 Integrante B: Inicialização do Frontend Angular e Configuração de CORS
Este integrante preparará o ecossistema Angular e configurará o backend para aceitar requisições originadas do ambiente de desenvolvimento do frontend (geralmente `http://localhost:4200`).

#### Atividades Detalhadas
1. **Configuração de CORS no Backend:** Criar uma classe global de configuração ou adicionar anotações nos controllers para evitar erros de bloqueio de requisições de outras origens.
2. **Inicialização do Projeto Frontend:** Executar a criação da pasta Angular na raiz do repositório (`ng new frontend --routing --style=css` ou similar).
3. **Configuração do Client HTTP:** Configurar o Angular para habilitar requisições HTTP e estruturar o arquivo de configuração para centralizar a URL base da API.

#### Exemplos e Dicas de Código

##### Configuração Global de CORS no Spring Boot (`CorsConfig.java`)
```java
package br.com.barracatipo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4200") // Permite chamadas do Angular
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
```

##### Habilitar HttpClient no Angular Moderno (`app.config.ts` ou `app.module.ts`)
```typescript
// Se estiver usando o Angular moderno orientado a Standalone Components (app.config.ts):
import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { routes } from './app.routes';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient() // Essencial para injetar o HttpClient nos serviços
  ]
};
```

---

### 👤 Integrante C: Serviços, Modelagem e Teste de Integração no Frontend
Este integrante conectará a base estrutural criada pelo Integrante B com as rotas REST do backend através de arquivos de serviços baseados em Observables (RxJS).

#### Atividades Detalhadas
1. **Definição de Interfaces TypeScript (Models):** Criar interfaces que representem os dados mapeados no backend para garantir a tipagem estática no Angular.
2. **Criação de Serviços Angular:** Implementar classes de serviço decoradas com `@Injectable` que usem o `HttpClient` para realizar requisições REST (GET, POST, PUT, DELETE).
3. **Teste de Integração Inicial:** Injetar os serviços no componente principal (`AppComponent`) e listar os resultados no terminal do console do desenvolvedor (`console.log`) para garantir que os dados do banco H2 estão chegando ao Angular.

#### Exemplos e Dicas de Código

##### Modelo de Dados em TypeScript (`barraca.model.ts`)
```typescript
export interface TipoBarraca {
  id?: number;
  nome: string;
}

export interface Barraca {
  id?: number;
  nome: string;
  localizacao: string;
  tipoBarracaId: number;      // Usado ao cadastrar/enviar dados (Request DTO)
  tipoBarracaNome?: string;   // Retornado pelo backend para exibição (Response DTO)
}
```

##### Serviço Angular (`barraca.service.ts`)
```typescript
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Barraca } from '../models/barraca.model';

@Injectable({
  providedIn: 'root'
})
export class BarracaService {
  private apiUrl = 'http://localhost:8080/api/barracas'; // Ajustar de acordo com a API

  constructor(private http: HttpClient) {}

  listarTodos(): Observable<Barraca[]> {
    return this.http.get<Barraca[]>(this.apiUrl);
  }

  obterPorId(id: number): Observable<Barraca> {
    return this.http.get<Barraca>(`${this.apiUrl}/${id}`);
  }

  salvar(barraca: Barraca): Observable<Barraca> {
    if (barraca.id) {
      return this.http.put<Barraca>(`${this.apiUrl}/${barraca.id}`, barraca);
    }
    return this.http.post<Barraca>(this.apiUrl, barraca);
  }

  excluir(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
```

---

## 🎨 Sprint 4: Telas Angular, Integração Final e Entrega
**Período:** 08/06 a 14/06  
**Foco:** Construir a interface visual do usuário, implementar as telas de consulta, cadastro e edição, estabelecer fluxos de erro visuais e documentar padrões arquiteturais.

### 👤 Integrante A: Tela de Listagem, Exclusão e Feedbacks
Focado na parte de visualização geral dos dados e na remoção física dos registros da aplicação.

#### Atividades Detalhadas
1. **Componente de Listagem:** Criar um componente Angular (`BarracaListComponent`) com uma tabela HTML estilizada contendo campos como Nome, Localização, Tipo da Barraca e Ações de manipulação.
2. **Lógica de Exclusão com Confirmação:** Implementar um botão de exclusão que confirme a ação (por ex., via `confirm` ou diálogo customizado) e invoque o método do serviço criado no Sprint 3.
3. **Feedback de Erros na Exclusão:** Tratar cenários onde a exclusão falhe devido a restrições de integridade referencial (por exemplo: tentar apagar um *Tipo de Barraca* que possui *Barracas* associadas). Exibir notificações coloridas (sucesso/erro).

#### Exemplos e Dicas de Código

##### Lógica do Componente (`barraca-list.component.ts`)
```typescript
import { Component, OnInit } from '@angular/core';
import { BarracaService } from '../../services/barraca.service';
import { Barraca } from '../../models/barraca.model';

@Component({
  selector: 'app-barraca-list',
  templateUrl: './barraca-list.component.html',
  styleUrls: ['./barraca-list.component.css']
})
export class BarracaListComponent implements OnInit {
  barracas: Barraca[] = [];
  mensagemSucesso: string = '';
  mensagemErro: string = '';

  constructor(private barracaService: BarracaService) {}

  ngOnInit(): void {
    this.carregarBarracas();
  }

  carregarBarracas(): void {
    this.barracaService.listarTodos().subscribe({
      next: (dados) => this.barracas = dados,
      error: (err) => this.mensagemErro = 'Não foi possível carregar a lista de barracas.'
    });
  }

  deletar(id: number): void {
    if (confirm('Tem certeza de que deseja remover esta barraca?')) {
      this.barracaService.excluir(id).subscribe({
        next: () => {
          this.mensagemSucesso = 'Barraca removida com sucesso!';
          this.carregarBarracas();
          setTimeout(() => this.mensagemSucesso = '', 3000);
        },
        error: (err) => {
          // Trata erros de restrição de integridade referencial do banco de dados
          this.mensagemErro = err.error?.message || 'Falha ao excluir a barraca.';
          setTimeout(() => this.mensagemErro = '', 4000);
        }
      });
    }
  }
}
```

##### Template do Componente (`barraca-list.component.html`)
```html
<div class="container">
  <h2>Barracas Cadastradas</h2>

  <!-- Alertas de Feedback -->
  <div *ngIf="mensagemSucesso" class="alert alert-success">{{ mensagemSucesso }}</div>
  <div *ngIf="mensagemErro" class="alert alert-danger">{{ mensagemErro }}</div>

  <table class="table-barracas">
    <thead>
      <tr>
        <th>Nome</th>
        <th>Localização</th>
        <th>Tipo</th>
        <th>Ações</th>
      </tr>
    </thead>
    <tbody>
      <tr *ngFor="let b of barracas">
        <td>{{ b.nome }}</td>
        <td>{{ b.localizacao }}</td>
        <td>{{ b.tipoBarracaNome }}</td>
        <td>
          <button [routerLink]="['/barracas/editar', b.id]" class="btn btn-edit">Editar</button>
          <button (click)="deletar(b.id!)" class="btn btn-delete">Excluir</button>
        </td>
      </tr>
    </tbody>
  </table>
</div>
```

---

### 👤 Integrante B: Tela de Cadastro, Edição e Validação de Formulários
Focado em prover um formulário reativo seguro que atenda tanto ao fluxo de criação quanto ao de atualização (edição) de dados.

#### Atividades Detalhadas
1. **Formulários Reativos (Reactive Forms):** Utilizar `FormGroup` e `FormBuilder` para validar campos obrigatórios diretamente na tela do usuário antes de submeter ao backend.
2. **Carregamento Dinâmico de FK (Foreign Key):** Carregar a lista de tipos de barraca via `TipoBarracaService` e popular um campo `<select>` HTML de forma assíncrona.
3. **Fluxo Híbrido (Cadastro/Edição):** Identificar se a rota atual possui um parâmetro de `:id` (via `ActivatedRoute`), ler os dados existentes e preencher o formulário para edição.

#### Exemplos e Dicas de Código

##### Código do Componente de Formulário (`barraca-form.component.ts`)
```typescript
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { BarracaService } from '../../services/barraca.service';
import { TipoBarracaService } from '../../services/tipo-barraca.service';
import { TipoBarraca } from '../../models/barraca.model';

@Component({
  selector: 'app-barraca-form',
  templateUrl: './barraca-form.component.html'
})
export class BarracaFormComponent implements OnInit {
  formBarraca!: FormGroup;
  tiposBarraca: TipoBarraca[] = [];
  modoEdicao = false;
  idEdicao?: number;

  constructor(
    private fb: FormBuilder,
    private barracaService: BarracaService,
    private tipoService: TipoBarracaService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.inicializarFormulario();
    this.carregarTipos();
    this.verificarModoEdicao();
  }

  inicializarFormulario(): void {
    this.formBarraca = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(3)]],
      localizacao: ['', Validators.required],
      tipoBarracaId: ['', Validators.required]
    });
  }

  carregarTipos(): void {
    this.tipoService.listarTodos().subscribe(dados => this.tiposBarraca = dados);
  }

  verificarModoEdicao(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.modoEdicao = true;
      this.idEdicao = +id;
      this.barracaService.obterPorId(this.idEdicao).subscribe(dados => {
        this.formBarraca.patchValue({
          nome: dados.nome,
          localizacao: dados.localizacao,
          tipoBarracaId: dados.tipoBarracaId
        });
      });
    }
  }

  salvar(): void {
    if (this.formBarraca.invalid) return;

    const dados = { id: this.idEdicao, ...this.formBarraca.value };
    this.barracaService.salvar(dados).subscribe({
      next: () => this.router.navigate(['/barracas']),
      error: (err) => alert(err.error?.message || 'Falha ao salvar.')
    });
  }
}
```

##### Template do Componente de Formulário (`barraca-form.component.html`)
```html
<div class="form-container">
  <h2>{{ modoEdicao ? 'Editar' : 'Nova' }} Barraca</h2>

  <form [formGroup]="formBarraca" (ngSubmit)="salvar()">
    <div class="form-group">
      <label>Nome:</label>
      <input formControlName="nome" type="text" class="form-control" />
      <span *ngIf="formBarraca.get('nome')?.touched && formBarraca.get('nome')?.errors?.['required']" class="error">
        Nome é obrigatório.
      </span>
    </div>

    <div class="form-group">
      <label>Localização:</label>
      <input formControlName="localizacao" type="text" class="form-control" />
    </div>

    <div class="form-group">
      <label>Tipo de Barraca:</label>
      <select formControlName="tipoBarracaId" class="form-control">
        <option value="">Selecione um Tipo...</option>
        <option *ngFor="let tipo of tiposBarraca" [value]="tipo.id">
          {{ tipo.nome }}
        </option>
      </select>
    </div>

    <button type="submit" [disabled]="formBarraca.invalid" class="btn btn-save">
      Salvar
    </button>
    <button type="button" routerLink="/barracas" class="btn btn-cancel">Cancelar</button>
  </form>
</div>
```

---

### 👤 Integrante C: Rotas, Navegação Geral e Produção Acadêmica
Responsável pela infraestrutura do frontend (rotas/navegação) e por assegurar que toda a documentação esteja à altura da avaliação acadêmica.

#### Atividades Detalhadas
1. **Configuração de Rotas e Navbar:** Organizar o `AppRoutingModule` e criar uma barra de menu no `app.component.html` para permitir navegação rápida.
2. **Escrita do README Técnico:** Instruções claras de pré-requisitos (Java SDK, Maven, Node, npm) e comandos necessários para rodar as aplicações localmente.
3. **Relatório de Padrões de Projeto (Acadêmico):** Elaborar a descrição detalhada e o mapeamento dos padrões aplicados na solução para a entrega do professor.

#### Configuração de Rotas no Angular (`app-routing.module.ts`)
```typescript
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BarracaListComponent } from './components/barraca-list/barraca-list.component';
import { BarracaFormComponent } from './components/barraca-form/barraca-form.component';

const routes: Routes = [
  { path: 'barracas', component: BarracaListComponent },
  { path: 'barracas/nova', component: BarracaFormComponent },
  { path: 'barracas/editar/:id', component: BarracaFormComponent },
  { path: '', redirectTo: '/barracas', pathMatch: 'full' },
  { path: '**', redirectTo: '/barracas' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
```

---

## 📘 Guia para o Relatório Técnico: Mapeamento de Padrões (Integrante C)
O professor de faculdade avalia rigorosamente a correlação entre a prática e os conceitos de Engenharia de Software. Aqui está um guia para fundamentar o relatório com o código do projeto:

| Padrão de Projeto | Como foi Aplicado no Projeto Barraca Tipo | Exemplo no Código |
| :--- | :--- | :--- |
| **MVC (Model-View-Controller)** | Divisão clara das responsabilidades. O Spring Boot atua como Controller/Model RESTful, enquanto o Angular atua como View dinâmica. | Classes de controle (`BarracaController.java`), modelos TS e templates HTML/CSS. |
| **Controller (GRASP)** | Atribuído às classes `Service` e `Controller`. O Controller delega as ações do usuário e as requisições de rede para a camada de serviços sem tratar regras de negócio. | O controlador REST repassa o comando `salvar()` para `BarracaService.java`. |
| **Service Layer** | Centraliza todas as regras de validação lógica do negócio em classes exclusivas do backend. | A verificação de "Nome Único" para a barraca é feita exclusivamente dentro de `BarracaService`. |
| **DTO (Data Transfer Object)** | Usado para desacoplar as classes persistidas no Banco de Dados (`@Entity`) da payload das requisições JSON. | Uso de `BarracaRequestDTO` para entrada de dados e `BarracaResponseDTO` no retorno do backend. |
| **Repository** | Abstrai as operações de banco de dados SQL através de interfaces Java. | `BarracaRepository` herdando de `JpaRepository<Barraca, Long>`. |
| **Observer (RxJS)** | O Angular se inscreve (*subscribe*) nas chamadas HTTP assíncronas do backend, reagindo quando os dados chegam ou quando ocorrem falhas. | `this.barracaService.listarTodos().subscribe(...)` dentro do component Angular. |

---

## 🚨 Checklist de Entrega e Apresentação
* [ ] As validações do Bean Validation no backend estão gerando erros HTTP 400 amigáveis.
* [ ] A tentativa de deletar um tipo de barraca com barracas associadas gera um erro HTTP 422 tratado com mensagem no frontend.
* [ ] O frontend possui validação reativa impedindo o clique no botão "Salvar" se os dados forem inválidos.
* [ ] O CORS está habilitado e a tela não apresenta erros de comunicação ao rodar no navegador.
* [ ] As colunas do Kanban no GitHub Projects estão organizadas e condizem com a evolução das atividades do grupo.
* [ ] O relatório técnico mapeia todas as classes principais e explica seus respectivos padrões de projeto com clareza.
