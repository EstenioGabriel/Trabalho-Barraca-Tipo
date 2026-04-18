# CRUD com Padrões GRASP — Barraca e TipoBarraca

Sistema de gestão de feira livre implementado em Java puro com menu textual no terminal e persistência em arquivo JSON local.

## Integrantes do Grupo A

- Estenio Gabriel 
- Gabriel Brandão

## Como Compilar e Executar

> **Requisito:** JDK 8 ou superior instalado.

### Windows (PowerShell)

```powershell
# 1. Criar pasta de saída
New-Item -ItemType Directory -Force -Path out

# 2. Listar todos os .java e compilar
$arquivos = Get-ChildItem -Recurse -Filter *.java src | Select-Object -ExpandProperty FullName
javac -d out $arquivos

# 3. Executar
java -cp out feira.graspcrud.Main
```

### Linux / Mac

```bash
find src -name "*.java" > sources.txt
javac -d out @sources.txt
java -cp out feira.graspcrud.Main
```

## Estrutura do Projeto

```
Barraca-Tipo/
├── src/
│   └── feira/graspcrud/
│       ├── Main.java
│       ├── controller/
│       │   └── BarracaController.java
│       ├── domain/
│       │   ├── Barraca.java
│       │   └── TipoBarraca.java
│       ├── dto/
│       │   ├── BarracaRequest.java
│       │   └── TipoBarracaRequest.java
│       ├── exception/
│       │   └── RegraNegocioException.java
│       ├── repository/
│       │   ├── BarracaRepository.java
│       │   └── TipoBarracaRepository.java
│       ├── repositoryJson/
│       │   ├── BarracaRepositoryJson.java
│       │   └── TipoBarracaRepositoryJson.java
│       └── util/
│           └── JsonMini.java
└── data/
    ├── barracas.json
    └── tipos-barraca.json
```

## Funcionalidades do Menu

```
1. Cadastrar TipoBarraca
2. Listar TipoBarraca
3. Cadastrar Barraca
4. Listar Barraca
5. Buscar Barraca por id
6. Atualizar Barraca
7. Excluir Barraca
8. Excluir TipoBarraca
9. Sair
```

## Regras de Negócio Implementadas

- Nome de TipoBarraca deve ter ao menos 3 caracteres.
- Não é permitido cadastrar dois TipoBarraca com o mesmo nome.
- Não é permitido remover um TipoBarraca que esteja vinculado a alguma Barraca.
- Nome de Barraca deve ter ao menos 3 caracteres.
- Não é permitido cadastrar duas Barracas com o mesmo nome.
- Toda Barraca deve estar associada a um TipoBarraca válido (existente no cadastro).

## Padrões GRASP Aplicados

| Padrão GRASP | Onde foi aplicado | Como foi aplicado |
|---|---|---|
| **Information Expert** | `Barraca.java`, `TipoBarraca.java` | Método `validar()` em cada entidade — quem tem os dados valida seus próprios dados |
| **Controller** | `BarracaController.java` | Recebe a entrada do terminal e delega para os services, sem lógica de negócio |
| **Creator** | `Main.java`, `BarracaService`, `TipoBarracaService` | `Main` instancia repositórios e services; services instanciam as entidades |
| **Low Coupling** | `BarracaService`, `TipoBarracaService` | Dependem das interfaces `BarracaRepository` e `TipoBarracaRepository`, não das implementações JSON |
| **High Cohesion** | Todas as classes | Cada classe tem uma única responsabilidade bem definida |
| **Pure Fabrication** | `BarracaRepositoryJson`, `TipoBarracaRepositoryJson` | Classes criadas apenas para persistência JSON, sem existir no domínio real da feira |
| **Protected Variations / Indirection** | Interfaces `Repository` | Mudanças na persistência (ex: trocar JSON por outro formato) não afetam services ou domínio |

## Tecnologias

- **Java puro** (JDK 8+)
- **Menu textual** no terminal (console)
- **Persistência** em arquivo JSON local (pasta `data/`)
- **Sem frameworks externos**, sem banco de dados, sem Spring Boot