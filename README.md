Hotel Management API RESTful

Uma API RESTful para gerenciamento de um sistema hoteleiro, responsável por controlar clientes, quartos, reservas e pagamentos.
Este projeto simula o fluxo real de funcionamento de um hotel, aplicando regras de negócio e boas práticas de desenvolvimento backend com Spring Boot.

<img width="1207" height="676" alt="Captura de Tela 2026-01-16 às 11 38 32" src="https://github.com/user-attachments/assets/af30af1e-1010-4868-b759-78d3d20d6270" />


🚀 Tecnologias Utilizadas

Linguagem: Java 21

Framework: Spring Boot 3

Persistência: Spring Data JPA & Hibernate

Banco de Dados: H2 Database (ambiente de teste e desenvolvimento)

Auxiliares: Lombok

Arquitetura: Arquitetura em camadas (Controller, Service, Repository, Model)

API: RESTful com suporte a HATEOAS

📌 Estrutura do Projeto

O projeto segue a arquitetura em camadas padrão do Spring, garantindo separação de responsabilidades, organização e facilidade de manutenção:

model
Contém as entidades JPA que representam os principais domínios do sistema, como Cliente, Quarto, Reserva e Pagamento.

repository
Interfaces que estendem JpaRepository, responsáveis pelo acesso e persistência dos dados no banco.

service
Camada que concentra a lógica de negócio, como:

Validação de disponibilidade de quartos

Controle do ciclo de vida da reserva

Expiração automática de reservas não pagas

Processamento de pagamentos

controller
Camada de exposição REST, responsável por receber as requisições HTTP, delegar o processamento para a camada de serviço e retornar respostas adequadas.

controller.exception
Implementação de tratamento de exceções customizadas utilizando @RestControllerAdvice, garantindo respostas HTTP padronizadas.

🔁 Fluxo de Funcionamento da Reserva

O cliente solicita a criação de uma reserva para um quarto disponível.

A reserva é criada com um status inicial e um prazo limite para pagamento.

Se o pagamento for realizado dentro do prazo, a reserva é confirmada.

Caso o pagamento não seja realizado no tempo definido, a reserva é automaticamente expirada e o quarto é liberado para novas reservas.

💰 Processamento de Pagamentos

Os valores financeiros são tratados utilizando BigDecimal, garantindo precisão nos cálculos monetários e evitando problemas comuns de imprecisão com 
tipos primitivos como double e float.

📊 Status e Regras de Negócio

O sistema utiliza Enums para padronizar os status de reserva e pagamento, garantindo:

Consistência dos dados

Redução de estados inválidos

Clareza na aplicação das regras de negócio

💡 Como Executar o Projeto

Clone o repositório:

git clone <url-do-repositorio>
cd hotel-management-api

Configuração do ambiente (Opcional):
O projeto utiliza o banco de dados em memória H2, ideal para testes e desenvolvimento local.
Dados iniciais podem ser carregados via CommandLineRunner.

Execute a aplicação:

# Usando Maven
./mvnw spring-boot:run

A aplicação estará disponível em:
http://localhost:8080

| Recurso   | Método HTTP | Endpoint         | Descrição                          | Status de Sucesso |
| --------- | ----------- | ---------------- | ---------------------------------- | ----------------- |
| Cliente   | POST        | `/clientes`      | Cria um novo cliente               | 201 Created       |
| Cliente   | GET         | `/clientes`      | Lista todos os clientes            | 200 OK            |
| Quarto    | POST        | `/quartos`       | Cria um novo quarto                | 201 Created       |
| Quarto    | GET         | `/quartos`       | Lista quartos disponíveis          | 200 OK            |
| Reserva   | POST        | `/reservas`      | Cria uma nova reserva              | 201 Created       |
| Reserva   | GET         | `/reservas/{id}` | Busca uma reserva por ID           | 200 OK            |
| Pagamento | POST        | `/pagamentos`    | Realiza o pagamento de uma reserva | 200 OK            |

Exemplo de Requisição (POST Reserva)
{
  "clienteId": 1,
  "quartoId": 2,
  "valor": 350.00
}

Tratamento de Exceções

O projeto possui um ControllerExceptionHandler responsável por capturar exceções de negócio, como recursos não encontrados ou regras inválidas.

Exemplo de resposta ao tentar acessar uma reserva inexistente:

{
  "timestamp": "2025-11-11T20:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Reserva de id: 999 não encontrada",
  "path": "/reservas/999"
}

🎯 Objetivo do Projeto

Este projeto foi desenvolvido com foco em aprendizado prático, aplicando conceitos de API REST, arquitetura em camadas, regras de negócio e 
boas práticas de desenvolvimento backend, simulando um cenário real de sistema corporativo.
