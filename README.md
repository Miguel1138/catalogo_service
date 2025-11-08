Com base na análise dos arquivos do projeto `catalogo_service`, aqui está uma proposta de arquivo `README.md` que descreve o projeto.

-----

# Serviço de Catálogo (catalogo\_service)

> Serviço de catálogo para API de biblioteca virtual de sistema distribuído.

## Visão Geral

Este projeto é um microserviço Spring Boot responsável pelo gerenciamento do catálogo de livros de uma biblioteca virtual. Ele foi desenhado para funcionar como parte de um sistema distribuído, atuando como um **OAuth2 Resource Server**. Sua principal função é gerenciar a entidade `Livro`, controlar seu estoque e permitir a integração com APIs externas (como o Google Books) para importação de dados.

O serviço está configurado para rodar na porta `8082`.

## Status do Projeto

⚠️ **Em Desenvolvimento.**

A estrutura do projeto está definida, mas a implementação principal ainda está pendente. A maioria dos arquivos de serviço, configuração e controle contém comentários `TODO` que descrevem a lógica de negócios e a implementação esperada.

## Arquitetura

O projeto utiliza uma abordagem baseada em **Arquitetura Limpa (Clean Architecture) / Hexagonal**, separando claramente o domínio das preocupações de infraestrutura:

  * **`application/domain`**: Contém o modelo de domínio puro (`Livro`, `LivroBuilder`).
  * **`application/gateways/service`**: Interfaces (Ports) que definem os casos de uso e contratos de serviço (`LivroService`).
  * **`controller`**: Adaptadores Primários (API REST) que recebem requisições HTTP e as traduzem para chamadas de serviço.
  * **`repository`**: Adaptadores Secundários (Database) que implementam as interfaces de serviço, tratando da persistência.
  * **`api`**: Adaptadores Secundários (Web) para comunicação com APIs externas (ex: Google Books).
  * **`infra`**: Camada de persistência de baixo nível (JPA Entities, Repositórios Spring Data).

## Funcionalidades Pretendidas

  * CRUD completo de livros (Criar, Ler, Atualizar, Deletar).
  * Busca de livros por título ou autor.
  * Controle de estoque (incremento e decremento).
  * Importação de livros por ISBN através de uma API externa (Google Books).
  * Tratamento de exceções de negócio (Ex: `LivroNaoEncontrado`, `LivroJaCadastrado`, `EstoqueInsuficiente`).

## Tecnologias

  * **Java 21**
  * **Spring Boot 3.5.7**
  * **Persistência:** Spring Data JPA / Hibernate
  * **Banco de Dados:** PostgreSQL (requerido)
  * **Segurança:** Spring Security (OAuth2 Resource Server, JWT)
  * **API Client:** Spring WebFlux (`WebClient`)
  * **Build:** Apache Maven
  * **Utilitários:** Lombok

## Segurança (Endpoints)

O serviço é protegido como um **OAuth2 Resource Server** e espera um JWT válido em todas as requisições (exceto as públicas). A configuração de segurança lê a claim `roles` do token, esperando que os papéis já venham com o prefixo (ex: `ROLE_BIBLIOTECARIO`).

Conforme os `TODO`s em `SecurityConfig.java`, as regras de autorização pretendidas são:

  * **Públicos (`permitAll`)**
      * `GET /livros`
      * `GET /livros/{id}`
      * `GET /livros/buscar`
  * **Papel: `BIBLIOTECARIO`**
      * `POST /livros` (Criar)
      * `PUT /livros/**` (Atualizar)
      * `DELETE /livros/**` (Deletar)
      * `POST /livros/importar/**` (Importar)
  * **Autenticado (Qualquer Papel)**
      * `PUT /livros/{id}/estoque/decrementar`
      * `PUT /livros/{id}/estoque/incrementar`

## Configuração e Execução

### Pré-requisitos

1.  Java 21 (ou JDK compatível).
2.  Maven.
3.  Um banco de dados PostgreSQL em execução.
4.  Um serviço de autenticação (externo a este projeto) que emita JWTs.

### Variáveis de Ambiente

Para executar a aplicação, as seguintes variáveis de ambiente são necessárias (conforme `application.properties`):

  * `DB_CATALOG`: O nome do banco de dados no PostgreSQL.
  * `POSTGRE_USERNAME`: Usuário do banco de dados.
  * `POSTGRE_PASSWORD`: Senha do banco de dados.
  * `JWT_SECRECT_KEY`: A chave secreta (exatamente a mesma usada pelo serviço de autenticação) para validar a assinatura do JWT.

Adicionalmente, a configuração do `WebClient` (`WebClientConfig.java`) espera uma propriedade `api.externa.livros.url` para se conectar à API de livros, embora esta não esteja definida no `application.properties` fornecido.

### Execução

1.  Clone o repositório.
2.  Configure as variáveis de ambiente.
3.  Execute o build do Maven:
    ```bash
    ./mvnw clean install
    ```
4.  Inicie a aplicação:
    ```bash
    ./mvnw spring-boot:run
    ```
