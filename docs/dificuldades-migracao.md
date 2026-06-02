# Dificuldades Encontradas na Migração

## 1. Serialização JSON com relacionamento JPA

O relacionamento `@ManyToOne` entre `Barraca` e `TipoBarraca` causava recursão infinita na serialização JSON. A solução foi criar DTOs separados (`BarracaResponseDTO`) com o nome do tipo achatado (`tipoBarracaNome`), evitando expor a entidade JPA diretamente na API.

## 2. Configuração de CORS

A combinação de `allowCredentials(true)` com wildcard `*` em `allowedOrigins` não é permitida pelo navegador. Foi necessário especificar a origem `http://localhost:4200` explicitamente em `CorsConfig.java`.

## 3. Mudança de paradigma no acesso a dados

A aplicação console usava repositórios manuais com persistência em arquivos JSON (`JsonMini.java`), incluindo geração manual de IDs. A migração para Spring Data JPA exigiu aprender um modelo declarativo, onde as implementações são geradas automaticamente e os IDs são auto-incrementados pelo banco H2.

## 4. Validação distribuída entre camadas

Na aplicação console, a validação ficava concentrada no método `validar()` das entidades de domínio. No Spring Boot, a validação foi dividida entre anotações nos DTOs (`@NotBlank`, `@Size`, `@NotNull`) e verificações manuais nos services, gerando duplicação em alguns pontos.

## 5. Controller console para API REST

O controller console era uma única classe com 165 linhas que gerenciava menu, entrada via `Scanner`, formatação de saída e tratamento de erros. A migração exigiu separar em dois controllers REST (`BarracaController` e `TipoBarracaController`), adotar códigos HTTP (200, 201, 204, 400, 422) e criar um `GlobalExceptionHandler` centralizado.

## 6. Formulário híbrido de criação e edição no Angular

O componente `BarracaFormComponent` precisou detectar o modo (criação ou edição) com base no parâmetro `:id` da rota, carregar dados existentes via `buscarPorId` e popular o formulário reativo — tudo isso mantendo a mesma lógica de validação e envio.

## 7. Change detection zoneless no Angular

A configuração `provideZonelessChangeDetection()` em `app.config.ts` exigiu a injeção manual de `ChangeDetectorRef` e chamadas a `markForCheck()` em todos os componentes após cada atualização de estado.

## 8. Mapeamento entre entidades e DTOs

Na aplicação console, os DTOs eram passados diretamente aos construtores das entidades. No Spring Boot, foi necessário criar métodos de conversão manual (`preencherBarraca` e `converterParaResponse`) para mapear entre `BarracaRequestDTO` ↔ `Barraca` e `Barraca` ↔ `BarracaResponseDTO`.

## 9. URLs da API hardcoded no frontend

Os services do Angular possuem a URL da API fixa (`http://localhost:8080/api/...`), sem uso de variáveis de ambiente (`environment.ts`). Isso impede o deploy em ambientes diferentes sem alteração manual no código.

## 10. Ausência de testes automatizados

O projeto não possui testes unitários ou de integração significativos — apenas um `contextLoads()` no backend e um teste boilerplate quebrado no frontend. A cobertura de services, controllers, validações e componentes ficou pendente.
