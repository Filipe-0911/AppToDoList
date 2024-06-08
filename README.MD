# API To do List

Projeto desenvolvido em Java utilizando o Framework Spring.

## Objetivos do projeto:
 - Possibilitar Criação de Tarefas com prazo para término para cada usuário;
 - Treinar autenticação com JWT;
 - Criar um CRUD para Tarefas e Usuários em que cada usuário tem sua lista de tarefas;

## Como utilizar:

O projeto foi desenvolvido com variáveis de ambiente salvas em um arquivo .env.
No arquivo application.properties estão os nomes que as variáveis devem ser salvas neste arquivo:

```commandline
spring.datasource.url=jdbc:mysql://${DB_HOST_PORT}/to_do_list
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

# Rotas e suas utilizações

## Criando um usuário 
## POST '/usuario/addUser'
Primeiro você deve criar um usuário na rota POST '/usuario/addUser' com as seguintes informações: 
```commandline
{
    "nome":"digite o seu nome",
    "login":"digite seu login",
    "senha":"digite sua senha"
}
```

## GET '/usuario'
Retorna os dados do usuário que está logado. Esta verificação é feita através do Token JWT inserido no Header.

## POST '/login'
Após a criação do usuário, vá até a rota '/login' utilize o metodo post para realizar o login:
```commandline
{
    "login": "usuario",
    "senha": "senha"
}
```
Esta operação te retornará um token JWT que deverá ser adicionaro ao Header com a chave 'Authorization'.

## '/tarefas'
### POST '/tarefas' contendo as seguintes informações:

```commandline
{
    "titulo":"Titulo da tarefa",
    "descricao":"Descrição da tarefa",
    "data":"dd/MM/yyyy"
}
```

### GET '/tarefas'
Obtem todas as tarefas do seu usuário;
<br>

## '/tarefas/{idTarefa}'
### GET
Para visualizar uma tarefa específica, inclua o ID da tarefa na rota '/tarefas', ou seja, ficará assim: '/tarefas/id' em que id é o número do id desta tarefa.

### PUT
Altera alguma informação em alguma tarefa específica.

## '/tarefas/concluir/{idTarefa}'
Determina que a tarefa está concluída.


