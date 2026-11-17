# Sistema de Biblioteca

Sistema de gerenciamento de biblioteca desenvolvido em **Java** como projeto acadêmico do curso de **Análise e Desenvolvimento de Sistemas (ADS)**, da **Faculdade CDL**. A aplicação funciona pelo terminal e permite administrar itens, usuários e empréstimos, com persistência de dados em arquivos `.txt`.

## Funcionalidades

- Cadastro de **livros, revistas e DVDs**.
- Cadastro de **alunos, professores e funcionários**.
- Listagem, edição e remoção de itens e usuários, com verificações para preservar o histórico de empréstimos.
- Pesquisa de itens por **título, autor, tipo e disponibilidade**.
- Registro de empréstimos e devoluções.
- Controle de disponibilidade e limite de empréstimos por tipo de usuário.
- Cálculo de prazo de devolução e multa por atraso (**R$ 2,00 por dia**).
- Salvamento e carregamento de dados em arquivos `.txt`, com mecanismos de backup e recuperação de gravações interrompidas.
- Validações de entrada e tratamento de exceções.

## Regras de empréstimo

| Tipo de usuário | Limite de empréstimos ativos | Prazo de devolução |
| --- | ---: | ---: |
| Aluno | 3 | 7 dias |
| Professor | 5 | 14 dias |
| Funcionário | 4 | 10 dias |

## Tecnologias e conceitos

- **Java 21**
- **Programação Orientada a Objetos:** encapsulamento, herança, polimorfismo, abstração e interfaces
- **ArrayList** para organização das coleções
- **LocalDate** e **ChronoUnit** para datas e atrasos
- **Java I/O e NIO** para persistência em arquivos
- **Aplicação de console** (sem interface gráfica e sem banco de dados)

## Organização do projeto

| Pasta ou arquivo | Responsabilidade |
| --- | --- |
| `src/app/Principal.java` | Menu e interação com o usuário |
| `src/model/` | Classes de itens, usuários e empréstimos |
| `src/service/BibliotecaService.java` | Regras de negócio, cadastros, pesquisas, empréstimos e devoluções |
| `src/repository/ArquivoRepository.java` | Leitura, gravação e proteção dos arquivos de dados |
| `src/interfaces/IPesquisavel.java` | Contrato para os métodos de pesquisa |
| `src/exception/BibliotecaException.java` | Exceção específica para operações da biblioteca |
| `data/` | Arquivos `itens.txt`, `usuarios.txt` e `emprestimos.txt` |

## Como executar

1. Tenha o **JDK 21** instalado.
2. Abra a **pasta raiz do projeto** no VS Code com suporte a Java.
3. Execute a classe `src/app/Principal.java`, pelo método `main`.
4. Utilize o menu numérico para realizar as operações.
5. Ao terminar, escolha **`0 - Salvar e sair`** para gravar as alterações nos arquivos.

**Importante:** execute a aplicação a partir da pasta raiz do projeto, pois os dados são acessados pelo caminho relativo `data/`.

## Autor

**Luis Miguel De Sousa De Castro**  
Curso: Análise e Desenvolvimento de Sistemas — Faculdade CDL
