# To-do List e monitoramento de estudo API

API Restful para monitoramento de estudo e criação de tarefas. Nela você pode inserir provas que você irá fazer e as respectivas matérias.
Além disso, ainda pode inserir a quantidade de questões que fez/acertou em determinado dia para que possa acompanhar seu desempenho no decorrer do tempo.

**Contato:** Dev Filipe  
**Email:** fbianchi.andrade@gmail.com  
**Licença:** [Apache 2.0](http://to-do.list/api/licenca)

**Antes de tudo, faça o login, pegue o token JWT retornado e insira no cabeçalho das requisições com a chave "Authorization"!**

## Endpoints

### `/usuario/addUser`

#### `POST`

Cria um novo usuário.

- **Operation ID:** `criarUsuario`
- **Corpo da requisição:**
  - Conteúdo:
    ```json
        {
            "login":"fulano@xpto.com",
            "nome":"Fulano de Tal",
            "senha":"123456"
        }
    ```
- **Respostas:**
  - `200 OK`
    - Schema:
      ```json
        {
            "id": 1,
            "login": "fulano@xpto.com",
            "nome": "Fulano de Tal"
        }
      ```

### `/login`

#### `POST`

Realiza a autenticação do usuário.

- **Operation ID:** `efetuarLogin`
- **Corpo da requisição:**
  - Conteúdo:
    ```json
    {
        "login":"fulano@xpto.com",
        "senha":"123456"
    }
    ```
- **Respostas:**
  - `200 OK`
    - Schema:
      ```json
        {
            "tokenJWT": "token JWT"
        }
      ```

### `/tarefas/{idTarefa}`

#### `GET`

Retorna os dados de uma tarefa específica.

- **Operation ID:** `obterDadosTarefaEspecifica`
- **Parâmetros:**
  - `idTarefa` (path) - ID da tarefa a ser obtida (integer, int64)
- **Respostas:**
  - `200 OK`
    - Schema:
      ```json
      {
        "$ref": "#/components/schemas/DadosDetalhamentoTarefaDTO"
      }
      ```
- **Segurança:**
  - Necessita token JWT (`bearer-key`).

#### `PUT`

Altera os dados de uma tarefa específica.

- **Operation ID:** `alterarTarefa`
- **Parâmetros:**
  - `idTarefa` (path) - ID da tarefa a ser alterada (integer, int64)
- **Corpo da requisição:**
  - Conteúdo:
    ```json
    {
      "$ref": "#/components/schemas/DadosCriacaoTarefasDTO"
    }
    ```
- **Respostas:**
  - `200 OK`
    - Schema:
      ```json
      {
        "$ref": "#/components/schemas/DadosDetalhamentoTarefaDTO"
      }
      ```
- **Segurança:**
  - Necessita token JWT (`bearer-key`).

### `/tarefas/concluir/{idTarefa}`

#### `PUT`

Marca uma tarefa como concluída.

- **Operation ID:** `concluirTarefa`
- **Parâmetros:**
  - `idTarefa` (path) - ID da tarefa a ser concluída (integer, int64)
- **Respostas:**
  - `200 OK`
    - Schema:
      ```json
      {
        "$ref": "#/components/schemas/DadosDetalhamentoTarefaDTO"
      }
      ```
- **Segurança:**
  - Necessita token JWT (`bearer-key`).


### `/tarefas`

#### `GET`

Lista todas as tarefas paginadas.

- **Operation ID:** `listarTarefas`
- **Parâmetros:**
  - `pageable` (query) - Configuração de paginação
- **Respostas:**
  - `200 OK`
    - Schema:
      ```json
      {
        "$ref": "#/components/schemas/PageDadosListagemTarefaDTO"
      }
      ```
- **Segurança:**
  - Necessita token JWT (`bearer-key`).

#### `POST`

Cria uma nova tarefa.

- **Operation ID:** `inserirTarefa`
- **Corpo da requisição:**
  - Conteúdo:
    ```json
    {
      "$ref": "#/components/schemas/DadosCriacaoTarefasDTO"
    }
    ```
- **Respostas:**
  - `200 OK`
    - Schema:
      ```json
      {
        "$ref": "#/components/schemas/DadosDetalhamentoTarefaDTO"
      }
      ```
- **Segurança:**
  - Necessita token JWT (`bearer-key`).

### `/provas`

#### `GET`

Lista todas as provas paginadas.

- **Operation ID:** `getProvas`
- **Parâmetros:**
  - `pageable` (query) - Configuração de paginação
- **Respostas:**
  - `200 OK`
    - Schema:
      ```json
      {
        "$ref": "#/components/schemas/PageDadosDetalhamentoProvaDTO"
      }
      ```
- **Segurança:**
  - Necessita token JWT (`bearer-key`).

#### `POST`

Cria uma nova prova.

- **Operation ID:** `inserirProva`
- **Corpo da requisição:**
  - Conteúdo:
    ```json
    {
      "$ref": "#/components/schemas/DadosCriacaoProvaDTO"
    }
    ```
- **Respostas:**
  - `200 OK`
    - Schema:
      ```json
      {
        "$ref": "#/components/schemas/DadosListagemProvaDTO"
      }
      ```
- **Segurança:**
  - Necessita token JWT (`bearer-key`).

### `/provas/materias/{idProva}`

#### `GET`

Lista todas as matérias de uma prova paginadas.

- **Operation ID:** `getMaterias`
- **Parâmetros:**
  - `pageable` (query) - Configuração de paginação
  - `idProva` (path) - ID da prova (integer, int64)
- **Respostas:**
  - `200 OK`
    - Schema:
      ```json
      {
        "$ref": "#/components/schemas/PageDadosListagemMateriaDTO"
      }
      ```
- **Segurança:**
  - Necessita token JWT (`bearer-key`).

#### `POST`

Adiciona uma nova matéria a uma prova.

- **Operation ID:** `inserirMaterias`
- **Parâmetros:**
  - `idProva` (path) - ID da prova (integer, int64)
- **Corpo da requisição:**
  - Conteúdo:
    ```json
    {
      "$ref": "#/components/schemas/DadosCriacaoMateriaDTO"
    }
    ```
- **Respostas:**
  - `200 OK`
    - Schema:
      ```json
      {
        "$ref": "#/components/schemas/DadosListagemMateriaDTO"
      }
      ```
- **Segurança:**
  - Necessita token JWT (`bearer-key`).

### `/provas/materias/{idProva}/questoes/{idMateria}`

#### `POST`

Adiciona questões a uma matéria de uma prova.

- **Operation ID:** `adicionarQuestoes`
- **Parâmetros:**
  - `idProva` (path) - ID da prova (integer, int64)
  - `idMateria` (path) - ID da matéria (integer, int64)
- **Corpo da requisição:**
  - Conteúdo:
    ```json
    {
      "$ref": "#/components/schemas/DadosCriacaoQuestaoDTO"
    }
    ```
- **Respostas:**
  - `200 OK`
    - Schema:
      ```json
      {
        "$ref": "#/components/schemas/DadosListagemQuestoesDTO"
      }
      ```
- **Segurança:**
  - Necessita token JWT (`bearer-key`).

### `/usuario`

#### `GET`

Retorna os dados do usuário autenticado.

- **Operation ID:** `retornaDadosUsuario`
- **Respostas:**
  - `200 OK`
    - Schema:
      ```json
      {
        "$ref": "#/components/schemas/DadosUsuarioDTO"
      }
      ```
- **Segurança:**
  - Necessita token JWT (`bearer-key`).

### `/provas/{idProva}`

#### `GET`

Retorna os detalhes de uma prova específica.

- **Operation ID:** `getProva`
- **Parâmetros:**
  - `idProva` (path) - ID da prova a ser obtida (integer, int64)
- **Respostas:**
  - `200 OK`
    - Schema:
      ```json
      {
        "$ref": "#/components/schemas/DadosListagemProvaDTO"
      }
      ```
- **Segurança:**
  - Necessita token JWT (`bearer-key`).

#### `DELETE`

Deleta uma prova específica.

- **Operation ID:** `deletarProva`
- **Parâmetros:**
  - `idProva` (path) - ID da prova a ser deletada (integer, int64)
- **Respostas:**
  - `200 OK`
- **Segurança:**
  - Necessita token JWT (`bearer-key`).
