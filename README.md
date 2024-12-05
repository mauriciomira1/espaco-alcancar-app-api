# Aplicação Espaço Alcançar

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.5.4-brightgreen)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED)
![License](https://img.shields.io/badge/License-MIT-yellow)

## Tabela de Conteúdos

- [Resumo](#resumo)
- [Principais Funcionalidades](#principais-funcionalidades)
- [Principais Dependências](#principais-dependências)
- [Requisitos](#requisitos)
- [Como Inicializar o Projeto](#como-inicializar-o-projeto)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Testes](#testes)
- [Deploy](#deploy)
- [Licença](#licença)
- [Contribuição](#contribuição)
- [Contato](#contato)

## Resumo

A aplicação Espaço Alcançar tem como objetivo principal o cadastro de usuários e seus filhos para posterior preenchimento online do "Perfil Sensorial" da criança. O perfil preenchido ser disponibilizado para análise de um profissional. Existem três modelos de perfil sensorial que podem ser disponibilizados pelo profissional para os pais, via sistema:

1. Perfil Sensorial para menores de 3 anos
2. Perfil Sensorial para maiores de 3 anos
3. Perfil Escolar para maiores de 3 anos

Além disso, o sistema permite que os usuários deixem avaliações, sugestões e notas (de 1 a 5) para a clínica, ajudando a medir a satisfação dos clientes.

No futuro, novas funcionalidades serão implementadas conforme a necessidade da clínica.

## Principais Funcionalidades

- Cadastro de usuários e filhos
- Preenchimento de perfis sensoriais
- Análise de perfis sensoriais por profissionais
- Avaliação e sugestão de usuários para a clínica
- Autenticação e autorização de usuários e profissionais
- Atualização de tokens de autenticação

## Principais Dependências

- Spring Boot
- JWT para geração de token de autenticação
- JPA e Hibernate para persistência de dados
- Docker Compose para criação de containers e facilitação da configuração do projeto em outros sistemas
- SonarQube para avaliação de qualidade de código, falhas de segurança, erros, etc. (ainda será implantado)
- JUnit e Mockito para testes (ainda será implantado)

## Requisitos

- Java 17
- Docker e Docker Compose
- Maven

## Como Inicializar o Projeto

1. Preencha as informações de usuário e senha no arquivo `application.properties` localizado em `src/main/resources/`.
2. Com o Docker instalado, use o comando `docker-compose up -d` no terminal da IDE para criar o container.
3. Confirme se o container foi criado com sucesso usando o comando `docker ps`.

## Estrutura do Projeto

A estrutura do projeto é organizada da seguinte forma:

## Licença

Este projeto está licenciado sob a Licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## Contato

Para mais informações, entre em contato com o desenvolvedor Maurício Miranda (https://www.linkedin.com/in/mmirandag)
