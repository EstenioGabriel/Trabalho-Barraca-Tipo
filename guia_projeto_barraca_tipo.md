# Guia Detalhado do Projeto: Barraca Tipo (Migração para Web)

Este documento foi criado para ajudar você a entender melhor a divisão do trabalho e as etapas necessárias para migrar o projeto "Barraca Tipo" (atualmente em modo console) para uma aplicação web moderna com Spring Boot (Backend) e Angular (Frontend).

---

## 🎯 Objetivos Principais
1. **Mudar a Arquitetura:** Sair de uma aplicação que roda no terminal (console) para uma aplicação web dividida em Frontend (tela) e Backend (servidor).
2. **Backend (Spring Boot):** Criar uma API RESTful organizada em camadas lógicas (Controller, Service, Repository, Domain, DTO).
3. **Frontend (Angular):** Criar telas interativas que consomem os dados do seu backend.
4. **Metodologia Ágil:** Utilizar Scrum e Kanban via GitHub Projects para organizar as tarefas.

---

## 🛠️ Parte 1: Configuração Inicial e Kanban (Antes de Codar)
Antes de escrever qualquer código, você precisa preparar o ambiente de gestão do projeto.

1. **Repositório:** Criar um repositório público no GitHub com sua equipe (3 pessoas).
2. **Clonar Material Base:** Baixar os scripts do professor (`gabarito-crud-grasp-produto`).
3. **Automação do GitHub:** Rodar o script `setup-github.ps1` usando o GitHub CLI. Isso criará automaticamente as *Issues* (tarefas), *Labels* (etiquetas) e *Milestones* (marcos de entrega).
4. **Configurar Quadro:** Organizar o GitHub Projects em 4 colunas: `Backlog`, `Em andamento`, `Em revisão`, `Concluído`.

---

## 🏃‍♂️ Parte 2: O Roteiro de Sprints e Divisão para o Trio

O projeto está dividido em 4 Sprints (ciclos de trabalho). Cada sprint tem uma divisão justa e clara de tarefas para os **3 Integrantes (A, B e C)** para garantir que todos programem e evitem conflitos no GitHub.

---

### 🏁 Sprint 1: Ambiente e Backend Inicial (18/05 a 24/05)
* **Foco:** Deixar a fundação do backend pronta para receber código.
* **Divisão de Trabalho:**
  * **👤 Integrante A:** Cria o projeto Spring Boot básico no Spring Initializr com as dependências (Web, JPA, H2, Validation) e descompacta no repositório.
  * **👤 Integrante B:** Cria a estrutura de pacotes vazios no Java (`domain`, `repository`, `service`, `controller`, `dto`, `exception`).
  * **👤 Integrante C:** Realiza a análise das classes do console antigo para mapear os campos e as regras que serão criadas no Spring Boot.

---

### ⚙️ Sprint 2: CRUD REST com Spring Boot (25/05 a 31/05)
* **Foco:** Fazer o banco de dados e as rotas da API funcionarem.
* **Divisão de Trabalho:**
  * **👤 Integrante A (Foco no Tipo de Barraca):**
    * Configura o banco H2 em `application.properties`.
    * Cria a entidade JPA `TipoBarraca.java` e a interface `TipoBarracaRepository.java`.
    * Cria o `TipoBarracaDTO.java` e implementa o `TipoBarracaService.java` com as lógicas e buscas.
    * Cria o `TipoBarracaController.java` com as rotas `GET`, `POST` e `DELETE`.
  * **👤 Integrante B (Foco em Estruturas e DTOs de Barraca):**
    * Cria a entidade JPA `Barraca.java` adicionando o relacionamento `@ManyToOne` com `TipoBarraca`.
    * Cria a interface `BarracaRepository.java`.
    * Cria os arquivos de transferência de dados: `BarracaRequestDTO.java` e `BarracaResponseDTO.java`.
    * Cria a classe de exceção customizada `RegraNegocioException.java`.
  * **👤 Integrante C (Foco em Lógica e Rotas de Barraca):**
    * Cria o `BarracaService.java` consumindo as regras de negócio de nome único, validação de existência do tipo de barraca, etc.
    * Cria o controlador REST `BarracaController.java` com as rotas `GET`, `POST`, `PUT` e `DELETE`.
    * Executa o servidor e lidera a bateria de testes manuais dos endpoints no Postman ou Insomnia com a ajuda do grupo.

---

### 🛡️ Sprint 3: Validações, CORS e Fundação do Frontend (01/06 a 07/06)
* **Foco:** Proteger a API contra dados inválidos e iniciar a comunicação com o Angular.
* **Divisão de Trabalho:**
  * **👤 Integrante A (Tratamento de Erros no Backend):**
    * Adiciona as anotações do Bean Validation nos DTOs (`@NotBlank`, `@NotNull`, `@Size`, etc.).
    * Insere `@Valid` nos Controllers para barrar dados inválidos.
    * Cria o `GlobalExceptionHandler.java` com `@RestControllerAdvice` para mapear erros de validação (HTTP 400) e erros de regras de negócio (HTTP 422).
  * **👤 Integrante B (Inicialização do Frontend e CORS):**
    * Configura o CORS no backend com `@CrossOrigin` nos controladores para permitir chamadas locais do Angular.
    * Inicializa o projeto Angular na pasta do repositório (`ng new nome-projeto --routing --style=css`).
    * Configura o módulo de requisições HTTP (`HttpClientModule`) no Angular.
  * **👤 Integrante C (Serviços e Modelagem no Frontend):**
    * Cria as interfaces TypeScript para espelhar as entidades do backend (`Barraca` e `TipoBarraca`).
    * Desenvolve os serviços Angular `TipoBarracaService` e `BarracaService` que disparam os comandos HTTP GET/POST/DELETE.
    * Faz um teste simples exibindo os dados da API no `console.log()` do navegador para provar que a integração funciona.

---

### 🎨 Sprint 4: Telas Angular, Integração Final e Entrega (08/06 a 14/06)
* **Foco:** Desenvolver as telas, juntar as partes e documentar.
* **Divisão de Trabalho:**
  * **👤 Integrante A (Tela de Exibição e Exclusão):**
    * Cria o componente Angular de listagem de Barracas em uma tabela.
    * Adiciona o botão de exclusão que chama a API.
    * Mostra avisos visuais amigáveis se a exclusão falhar (ex: quando um tipo de barraca estiver associado e não puder ser deletado).
  * **👤 Integrante B (Tela de Formulário e Cadastro):**
    * Cria o componente do formulário de cadastro de Barracas.
    * Adiciona um campo de seleção (`<select>`) alimentado dinamicamente com os tipos de barraca buscados do backend.
    * Configura o envio do formulário de criação/edição e as mensagens de sucesso.
  * **👤 Integrante C (Documentação e Relatório Final):**
    * Organiza os roteamentos das páginas no Angular para navegar entre a lista e o cadastro.
    * Escreve o arquivo `README.md` detalhado ensinando passo a passo como rodar tanto o Backend (Spring Boot) quanto o Frontend (Angular).
    * Redige o relatório final (1 a 2 páginas) identificando os Padrões de Projeto aplicados (MVC, Repository, Service Layer, DTO, Observer/RxJS) com exemplos práticos do código do grupo.

---

## 🚨 Dicas de Ouro e Cuidados (Para não perder nota)
* **Regra de Negócio no Service:** **NUNCA** coloque regras de negócio dentro do `Controller`. O Controller só recebe a requisição e repassa para o `Service`. (Perda de 10 pontos).
* **Uso de DTOs:** Não devolva ou receba suas entidades de banco de dados (`@Entity`) diretamente nos endpoints. Sempre converta Entidade <-> DTO. (Perda de 5 pontos).
* **Uso do Kanban:** Atualize as colunas no GitHub todos os dias. Os professores avaliam se houve progresso gradual. Se você mover todos os cards no último dia, perde ponto.
* **Execução Limpa:** O projeto precisa rodar liso na máquina de terceiros. Sempre teste baixando o seu próprio projeto em uma pasta limpa para ver se não faltou comitar algo.

---

## 🙋 Perguntas Frequentes da Apresentação (Prepare-se!)
Se prepare para responder a essas perguntas com o seu grupo:
1. *Onde fica a regra de negócio?* (R: Na camada de Service. Mostre a classe).
2. *O que é um DTO?* (R: Data Transfer Object. Serve para trafegar apenas os dados necessários entre cliente e servidor, escondendo atributos sensíveis do banco).
3. *Como o frontend conversa com o backend?* (R: Através de requisições HTTP REST, que o Angular faz usando o módulo `HttpClient`).
4. *O que acontece ao deletar um TipoProduto em uso?* (R: Deve ser tratado no Service e lançar uma exceção amigável tratada pelo GlobalExceptionHandler, não deixando estourar erro de banco no usuário).
