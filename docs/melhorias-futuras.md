# Sugestões de Melhorias Futuras

## 1. Testes automatizados

Adicionar testes unitários com JUnit 5 e Mockito no backend (services, controllers, validações) e testes com Jasmine/Karma no frontend (services, componentes, formulários). Priorizar os fluxos críticos de CRUD e as regras de negócio.

## 2. Verificação de integridade referencial na exclusão de TipoBarraca

O método `existsByTipoBarracaId` já está declarado no `BarracaRepository` mas nunca é chamado. Implementar a verificação no `TipoBarracaService.remover()` para impedir a exclusão de tipos que possuem barracas vinculadas, retornando uma mensagem amigável ao invés de erro 500 do banco.

## 3. Variáveis de ambiente para URLs da API

Substituir as URLs hardcoded nos services do Angular (`http://localhost:8080/api/...`) por configuração baseada em `environment.ts`, permitindo apontar para diferentes ambientes (desenvolvimento, homologação, produção) sem alterar o código.

## 4. Paginação e ordenação

Implementar paginação com `Pageable` do Spring Data nos endpoints de listagem e suporte a ordenação dinâmica. No frontend, adicionar componentes de paginação e seleção de coluna/ordem nas tabelas.

## 5. Autenticação e autorização

Adicionar Spring Security com JWT para proteger os endpoints da API. No frontend, implementar interceptors para envio do token e telas de login/registro com controle de acesso por rotas.

## 6. Remover duplicação de validação

A validação de tamanho do nome (`@Size(min=3)`) existe tanto no DTO quanto no método `validarNome()` do `BarracaService`. Remover a duplicação e confiar nas anotações do Bean Validation como única fonte de validação de campos.

## 7. Banco de dados persistente para produção

Substituir o H2 em memória por um banco persistente (PostgreSQL ou MySQL), configurar profiles do Spring (`application-dev.properties`, `application-prod.properties`) e usar Flyway ou Liquibase para versionamento do schema.

## 8. Documentação da API com Swagger/OpenAPI

Adicionar a dependência `springdoc-openapi` e anotar os controllers para gerar documentação interativa da API automaticamente, acessível em `/swagger-ui.html`.

## 9. Mapeamento de entidades com MapStruct

Substituir os métodos manuais de conversão (`preencherBarraca`, `converterParaResponse`) por MapStruct, reduzindo boilerplate e evitando erros de mapeamento quando novos campos forem adicionados.

## 10. Tratamento global de erros no frontend

Criar um `HttpInterceptor` no Angular para capturar erros HTTP de forma centralizada, exibindo notificações toast ao invés de tratar erros individualmente em cada componente.

## 11. Validação de integridade referencial no frontend

Antes de excluir um TipoBarraca, verificar no frontend se existem barracas vinculadas e exibir um modal de confirmação ou impedimento, melhorando a experiência do usuário.
