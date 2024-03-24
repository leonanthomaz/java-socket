# Java Socket Chat

Este é um projeto simples de chat implementado em Java usando sockets para comunicação entre cliente e servidor.

## Funcionalidades

- Os clientes se conectam ao servidor e podem enviar mensagens.
- As mensagens enviadas por um cliente são transmitidas para todos os outros clientes conectados.
- O servidor aceita múltiplos clientes simultaneamente.

## Como usar

1. Compile os arquivos Java: `javac Server.java Client01.java Client02.java`
2. Execute o servidor: `java Server`
3. Execute os clientes em diferentes terminais: `java Client01` e `java Client02`
4. Comece a digitar mensagens nos clientes e pressione Enter para enviá-las.

## Arquivos

### `Server.java`

Este arquivo contém a implementação do servidor que aceita conexões de clientes e encaminha mensagens entre eles.

### `Client01.java` e `Client02.java`

Estes arquivos contêm a implementação dos clientes que se conectam ao servidor e enviam mensagens.

## Javadoc

Consulte os arquivos fonte para obter documentação detalhada.

