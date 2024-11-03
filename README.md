# Aplicação Espaço Alcançar

## Resumo

- A aplicação tem o objetivo de cadastro de usuário, assim como cadastro dos filhos para posterior preenchimento online do "Perfil Sensorial" da criança. O Perfil preenchido será disponibilizado para o profissional analisar. Existem 3 modelos de perfil sensorial, que serão disponibilizados pelo profissional para o pai, via sistema.

1. Perfil Sensorial par menores de 3 anos
2. Perfil Sensorial para maiores de 3 anos
3. Perfil Escolar para maiores de 3 anos

- Além disso, no sistema, o Usuário poderá deixar uma avaliação/sugestão e nota (de 1 a 5) para a clínica, para que possammos medir a satisfação dos clientes.

- No futuro novas funções serão implementadas conforme necessidade da clínica.

## Principais dependências

- SpringBoot
- JWT para geração de token de autenticação
- Jpa e Hibernate para DB
- Docker compose para criar container e facilitar criação do projeto em outros sistemas
- Sonarqube para avaliação de qualidade de código, falhas de segurança, erros, etc.

## Como inicializar o projeto

1 - Preencha as informações de usuário e senha no application.properties
1 - Com o docker instalado, use o comando "docker-compose up -d" no terminal da IDE para criar o container
2 - Confirme se foi criado o container com o comando "docker ps"
