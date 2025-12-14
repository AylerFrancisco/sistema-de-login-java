Sistema de Login, Cadastro e Notificações
Visão Geral do Projeto

Este projeto consiste no desenvolvimento de um sistema de login e gerenciamento de usuários, com controle de acesso, notificações e funcionalidades administrativas. O sistema foi implementado utilizando Java com Swing, seguindo uma arquitetura organizada em camadas, com foco em separação de responsabilidades, manutenibilidade e clareza do código.

O sistema contempla CRUD completo de usuários, autenticação, autorização, validação de senha e controle de acesso baseado em perfis.

Autores

Projeto desenvolvido por:

Lukian Borges

Thobias

Ayler

Arquitetura e Organização

O sistema foi estruturado utilizando uma arquitetura em camadas, separando claramente as responsabilidades:

View (Swing)
Responsável apenas pela interface gráfica e captura de eventos do usuário.

Presenter
Camada intermediária responsável por:

Orquestrar ações da interface

Aplicar regras de fluxo

Coordenar chamadas aos serviços

Service
Contém as regras de negócio, como:

Autenticação

Validação de senha

Cadastro de usuários

Regras de autorização

Repository
Responsável pelo acesso a dados, utilizando SQLite como banco de dados.

Adapter e State
Utilizados para aplicar padrões de projeto, garantindo extensibilidade e desacoplamento.

 Funcionalidades Implementadas
 Autenticação (Login)

Autenticação de usuários por nome de usuário e senha

Validação de credenciais

Direcionamento automático para telas diferentes conforme o perfil:

ADMIN / ADMIN_MASTER

USER

 Cadastro de Usuário

Cadastro de novos usuários com:

Nome de usuário

Senha

Confirmação de senha

Validação de senha com regras específicas (tamanho, complexidade, etc.)

Verificação de:

Usuário já existente

Senhas iguais (senha × confirmação)

Usuários cadastrados iniciam como não autorizados

Controle de Acesso e Autorização

Tela administrativa para:

Visualizar usuários pendentes

Autorizar acesso

Excluir usuários

Atualização automática da tabela após ações

Feedback visual por meio de mensagens modais

 CRUD de Usuários

Create – Cadastro de novos usuários

Read – Listagem de usuários

Update – Autorização de acesso

Delete – Exclusão de usuários

Notificações e Mensagens

Utilização de JOptionPane para:

Mensagens de erro

Confirmações

Notificações de sucesso

Ajustes de foco e hierarquia para garantir que os diálogos apareçam corretamente em primeiro plano

 Padrões de Projeto Utilizados

MVC / MVP (adaptado)
Separação clara entre View, Presenter e Service.

Adapter Pattern
Utilizado para integração com biblioteca externa de validação de senha, desacoplando a implementação do sistema.

State Pattern
Aplicado no fluxo de cadastro para controle de estados após o sucesso da operação.

Singleton
Utilizado no repositório SQLite para garantir instância única de acesso ao banco.

Interface Gráfica (Swing)

A interface foi desenvolvida utilizando:

JFrame

JPanel

JTable

JButton

JPasswordField

Sobre o uso de MDI

Embora o conceito MDI (Multiple Document Interface) tenha sido considerado, não foi adotado o uso de JInternalFrame.

Justificativa Técnica

Optou-se pelo uso de JFrame e JPanel pelos seguintes motivos:

Maior simplicidade de implementação

Melhor controle de foco e eventos

Menor complexidade para manutenção e evolução do sistema

Evita problemas recorrentes do JInternalFrame, como:

Gerenciamento de camadas

Z-order

Foco de diálogos modais

A abordagem adotada mantém o sistema consistente, funcional e alinhado aos objetivos do projeto, sem prejuízo às funcionalidades propostas.

Persistência de Dados

Banco de dados: SQLite

Operações realizadas via camada Repository

Controle de usuários e estados de autorização persistidos localmente

Validações Implementadas

Senha forte (via biblioteca externa)

Confirmação de senha obrigatória

Usuário duplicado

Campos obrigatórios

Controle de acesso por perfil

Considerações Finais

O projeto atendeu aos requisitos propostos, entregando um sistema funcional, organizado e extensível. As decisões arquiteturais priorizaram clareza, separação de responsabilidades e robustez, garantindo uma base sólida para futuras melhorias.