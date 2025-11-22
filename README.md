# (Projeto) catalogo_service

Este projeto é o microsserviço de catálogo de livros para a API "Scripta" de biblioteca universitária. Ele é responsável pelo gerenciamento do acervo, controle de estoque e integração com APIs externas (Google Books) para importação de metadados.

A aplicação é construída em **Java 21** com **Spring Boot 3.5.7** e atua como um **OAuth2 Resource Server**, utilizando **Spring WebFlux** para integrações reativas e **Spring Data JPA** para persistência.

## Stack de Tecnologias

| Categoria | Tecnologia | Justificativa / Uso |
| :--- | :--- | :--- |
| **Core** | Java 21 | Linguagem principal da aplicação. |
| **Framework** | Spring Boot 3.5.7 | Framework base (Web, Data, Security). |
| **Segurança** | Spring Security | Configurado como OAuth2 Resource Server. |
| | Nimbus JOSE + JWT | Decodificação e validação de tokens JWT. |
| **Integração** | Spring WebFlux | Cliente HTTP (`WebClient`) para consumir a Google Books API. |
| **Persistência** | Spring Data JPA | Camada de acesso a dados. |
| **Banco de Dados** | PostgreSQL | Banco de dados relacional para produção. |
| | H2 Database | Banco de dados em memória para testes. |
| **Utilitários** | Lombok | Redução de código boilerplate (ex: @Data, @Builder). |
| | Spring Validation | Validação de DTOs de entrada (ex: @NotBlank, @Min). |

## Arquitetura (Design)

A arquitetura do projeto segue os princípios da **Clean Architecture**. O modelo de **Domínio** (`Livro`) é isolado da camada de infraestrutura (`LivroEntity`).

* **Domínio**: Contém as regras de negócio e entidades puras (`Livro`, `LivroBuilder`).
* **Gateways**: Interfaces que definem as portas de entrada e saída (`LivroService`).
* **Infraestrutura**: Implementações concretas, como o `LivroRepository` (que atua como Adapter para o `LivroEntityRepository` do JPA) e o `GoogleBooksApiService` (Adapter para integração externa).
* **Mapper**: A classe `LivroMapper` é responsável por converter entre as entidades de banco e o domínio, garantindo o desacoplamento.

A segurança opera no modelo **Stateless** como um Resource Server, validando a assinatura dos JWTs recebidos através da chave secreta compartilhada.

## Configuração e Execução (IntelliJ IDEA)

### 1. Pré-requisitos

* Java 21 (JDK)
* Uma instância do PostgreSQL rodando.

### 2. Configurando o IntelliJ IDEA

1.  **Abra o projeto:**
    * Abra o projeto (a pasta `catalogo_service`) no IntelliJ IDEA.
    * A IDE detectará o arquivo `pom.xml` e deve baixar automaticamente todas as dependências do Maven.

2.  **Configure as Variáveis de Ambiente:**
    * A aplicação precisa de variáveis de ambiente para se conectar ao banco e validar a assinatura dos tokens (definidas em `application.properties`).
    * No canto superior direito do IntelliJ, clique em `Edit Configurations...`.
    * Na janela que abrir, localize a aplicação `CatalogoServiceApplication`.
    * No campo **"Environment variables"**, adicione as seguintes chaves, substituindo pelos seus valores:

    ```bash
    DB_CATALOG=nome_do_seu_banco_de_dados;
    POSTGRE_USERNAME=seu_usuario_postgre;
    POSTGRE_PASSWORD=sua_senha_postgre;
    JWT_SECRECT_KEY=sua_chave_secreta_aqui
    ```

    * *Opcional*: A URL da API do Google Books já possui um valor padrão, mas pode ser sobrescrita com `api.google.books.url`.
    * Clique em "OK" para salvar.

3.  **Execute a Aplicação:**
    * Navegue até o arquivo `src/main/java/br/com/scripta_api/catalogo_service/CatalogoServiceApplication.java`.
    * Clique no ícone verde "Play" ao lado da declaração da classe.
    * A aplicação iniciará no console, rodando na porta **8082**.

## API Endpoints (Contrato da API)

### Módulo: Livros (`/livros`)

#### `GET /livros`

Lista todos os livros do acervo.

* **Acesso**: Público.
* **Response 200 OK** (`List<LivroResponse>`):

    ```json
    [
      {
        "id": 1,
        "titulo": "Domain-Driven Design",
        "autor": "Eric Evans",
        "isbn": "9780321125217",
        "anoPublicacao": 2003,
        "quantidadeTotal": 5,
        "quantidadeDsiponivel": 5
      }
    ]
    ```

#### `GET /livros/buscar`

Busca livros por palavra-chave (título ou autor).

* **Acesso**: Público.
* **Body (Raw String)**: `"Evans"`
* **Response 200 OK**: Lista de livros filtrada.

#### `POST /livros`

Cadastra um novo livro manualmente.

* **Acesso**: Autenticado. Requer Role: `BIBLIOTECARIO`.
* **Request Body** (`CriarLivroRequest`):

    ```json
    {
      "titulo": "Clean Code",
      "autor": "Robert C. Martin",
      "isbn": "9780132350884",
      "anoPublicacao": 2008,
      "quantidadeTotal": 10,
      "quantidadeDisponivel": 10
    }
    ```

#### `POST /livros/importar/{isbn}`

Importa automaticamente os dados de um livro da API do Google Books e o cadastra no sistema.

* **Acesso**: Autenticado. Requer Role: `BIBLIOTECARIO`.
* **Parâmetro de URL**: `isbn` (ex: `9780132350884`).
* **Response 200 OK** (`LivroResponse`): Retorna o livro criado com dados importados.

#### `PUT /livros/{id}/estoque/decrementar`

Decrementa a quantidade disponível de um livro (usado quando um empréstimo é realizado).

* **Acesso**: Autenticado (Qualquer usuário logado ou serviço interno).
* **Response 200 OK**: Dados atualizados do livro.

#### `PUT /livros/{id}/estoque/incrementar`

Incrementa a quantidade disponível de um livro (usado na devolução).

* **Acesso**: Autenticado (Qualquer usuário logado ou serviço interno).
* **Response 200 OK**: Dados atualizados do livro.

## Detalhes de Segurança

* **Resource Server**: Este serviço não gera tokens. Ele confia nos tokens assinados pelo `usuario-service`.
* **Validação**: Utiliza a chave definida em `JWT_SECRECT_KEY` para validar a assinatura HMAC SHA-256 do token recebido.
* **Autorização**:
    * Rotas `GET` são públicas.
    * Rotas de gestão (`POST`, `importar`) exigem a role `BIBLIOTECARIO` (extraída da claim `roles` do JWT).
    * Rotas de estoque exigem apenas que o token seja válido.

### Links Úteis para Aprofundamento

* [Spring Boot](https://spring.io/projects/spring-boot)
* [Spring Security OAuth2 Resource Server](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/index.html)
* [Spring WebFlux (WebClient)](https://docs.spring.io/spring-framework/reference/web/webflux-webclient.html)
* [Google Books API](https://developers.google.com/books)
* [Lombok](https://projectlombok.org/)
