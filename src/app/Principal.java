package src.app;
import java.util.Scanner;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import src.exception.BibliotecaException;
import src.model.Aluno;
import src.model.DVD;
import src.model.Emprestimo;
import src.model.Funcionario;
import src.model.ItemBiblioteca;
import src.model.Livro;
import src.model.Professor;
import src.model.Revista;
import src.model.Usuario;
import src.repository.ArquivoRepository;
import src.service.BibliotecaService;


public class Principal {
    public static int lerInteiro(Scanner teclado) {
        while (!teclado.hasNextInt()) {
            System.out.println("Entrada inválida! Digite um número inteiro.");
            teclado.nextLine();
        }

        int numero = teclado.nextInt();
        teclado.nextLine();

        return numero;
    }
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        BibliotecaService biblioteca = new BibliotecaService();
        ArquivoRepository repository = new ArquivoRepository();

        try {
            Path pastaDados = Path.of("data");

            Path arquivoItens = pastaDados.resolve("itens.txt");
            Path arquivoUsuarios = pastaDados.resolve("usuarios.txt");
            Path arquivoEmprestimos = pastaDados.resolve("emprestimos.txt");

            if (Files.notExists(arquivoItens) &&
                Files.notExists(arquivoUsuarios) &&
                Files.notExists(arquivoEmprestimos)) {

                Files.createDirectories(pastaDados);

                Files.createFile(arquivoItens);
                Files.createFile(arquivoUsuarios);
                Files.createFile(arquivoEmprestimos);
            }

            if (Files.notExists(arquivoItens) ||
                Files.notExists(arquivoUsuarios) ||
                Files.notExists(arquivoEmprestimos)) {

                throw new IOException("Um ou mais arquivos de dados estão ausentes.");
            }

            biblioteca.carregarDados(repository);

        } catch (IOException e) {
            System.out.println("Erro ao carregar dados: " + e.getMessage());
            teclado.close();
            return;
        }

        int resposta = -1;

        do{
            System.out.printf("===== SISTEMA DE BIBLIOTECA =====\n" + 
                                "\n" + 
                                "1 - Cadastrar item \n" + 
                                "2 - Cadastrar usuário\n" + 
                                "3 - Listar itens\n" + 
                                "4 - Listar usuários\n" + 
                                "5 - Editar item\n" + 
                                "6 - Editar usuário\n" + 
                                "7 - Remover item\n" + 
                                "8 - Remover usuário\n" + 
                                "9 - Realizar empréstimo\n" + 
                                "10 - Devolver item\n" + 
                                "11 - Pesquisar itens\n" + 
                                "12 - Listar empréstimos\n" + 
                                "\n" + 
                                "0 - Salvar e sair\n" + 
                                "\n" + 
                                "Escolha uma opção:");
                                if (!teclado.hasNextInt()) {
                                    System.out.println("Entrada inválida! Digite um número.");
                                    teclado.nextLine();
                                    continue;
                                }

                                resposta = lerInteiro(teclado);
            
            switch (resposta) {
                case 1:{
                    System.out.println("=== CADASTRAR ITEM ===");
                    System.out.println("1 - Livro");
                    System.out.println("2 - Revista");
                    System.out.println("3 - DVD");
                    System.out.print("Escolha o tipo: ");

                    int tipoItem = lerInteiro(teclado);

                    if (tipoItem < 1 || tipoItem > 3) {
                        System.out.println("Tipo inválido!");
                        break;
                    }

                    System.out.print("ID: ");
                    int id = lerInteiro(teclado);
                    if (id <= 0) {
                        System.out.println("O ID deve ser maior que zero!");
                        break;
                    }

                    System.out.print("Título: ");
                    String titulo = teclado.nextLine();
                    if (titulo.isBlank()) {
                        System.out.println("O título não pode estar vazio!");
                        break;
                    }

                    System.out.print("Ano: ");
                    int ano = lerInteiro(teclado);
                    if (ano <= 0) {
                        System.out.println("O ano deve ser maior que zero!");
                        break;
                    }
                    
                    switch (tipoItem) {
                        case 1:
                            System.out.print("Autor: ");
                            String autor = teclado.nextLine();
                            if (autor.isBlank()) {
                                System.out.println("O autor não pode estar vazio!");
                                break;
                            }

                            biblioteca.cadastrarItem(new Livro(id, titulo, ano, true, autor));
                            break;

                        case 2:
                            System.out.print("Edição: ");
                            int edicao = lerInteiro(teclado);
                            if (edicao <= 0) {
                                System.out.println("A edição deve ser maior que zero!");
                                break;
                            }

                            biblioteca.cadastrarItem(new Revista(id, titulo, ano, true, edicao));
                            break;

                        case 3:
                            System.out.print("Duração em minutos: ");
                            int duracao = lerInteiro(teclado);
                            if (duracao <= 0) {
                                System.out.println("A duração deve ser maior que zero!");
                                break;
                            }

                            biblioteca.cadastrarItem(new DVD(id, titulo, ano, true, duracao));
                            break;
                    }

                    break;
                }

                case 2: {
                    System.out.println("=== CADASTRAR USUÁRIO ===");
                    System.out.println("1 - Aluno");
                    System.out.println("2 - Professor");
                    System.out.println("3 - Funcionário");
                    System.out.print("Escolha o tipo: ");

                    int tipoUsuario = lerInteiro(teclado);

                    if (tipoUsuario < 1 || tipoUsuario > 3) {
                        System.out.println("Tipo inválido!");
                        break;
                    }

                    System.out.print("ID: ");
                    int id = lerInteiro(teclado);
                    if (id <= 0) {
                        System.out.println("O ID deve ser maior que zero!");
                        break;
                    }

                    System.out.print("Nome: ");
                    String nome = teclado.nextLine();
                    if (nome.isBlank()) {
                        System.out.println("O nome não pode estar vazio!");
                        break;
                    }

                    switch (tipoUsuario) {
                        case 1:
                            biblioteca.cadastrarUsuario(new Aluno(id, nome));
                            break;

                        case 2:
                            biblioteca.cadastrarUsuario(new Professor(id, nome));
                            break;

                        case 3:
                            biblioteca.cadastrarUsuario(new Funcionario(id, nome));
                            break;
                    }

                    break;
                }

                case 3:
                    biblioteca.listarItens();
                    break;

                case 4:
                    biblioteca.listarUsuarios();
                    break;

                
                case 5: {
                    System.out.println("=== EDITAR ITEM ===");
                    System.out.print("Digite o ID do item: ");
                    int idItem = lerInteiro(teclado);

                    if (idItem <= 0) {
                        System.out.println("O ID deve ser maior que zero!");
                        break;
                    }

                    ItemBiblioteca item = biblioteca.buscarItemId(idItem);

                    if (item == null) {
                        System.out.println("Item não encontrado!");
                        break;
                    }

                    System.out.println("1 - Editar título");
                    System.out.println("2 - Editar ano");
                    System.out.println("3 - Editar atributo específico");
                    System.out.print("Escolha uma opção: ");

                    int opcaoEditar = lerInteiro(teclado);

                    switch (opcaoEditar) {
                        case 1:
                            System.out.print("Novo título: ");
                            String novoTitulo = teclado.nextLine();
                            if (novoTitulo.isBlank()) {
                                System.out.println("O título não pode estar vazio!");
                                break;
                            }

                            biblioteca.editarItem(idItem, novoTitulo, item.getAno());
                            break;

                        case 2:
                            System.out.print("Novo ano: ");
                            int novoAno = lerInteiro(teclado);
                            if (novoAno <= 0) {
                                System.out.println("O ano deve ser maior que zero!");
                                break;
                            }

                            biblioteca.editarItem(idItem, item.getTitulo(), novoAno);
                            break;

                        case 3:
                            if (item instanceof Livro) {
                                System.out.print("Novo autor: ");
                                String novoAutor = teclado.nextLine();
                                if (novoAutor.isBlank()) {
                                    System.out.println("O autor não pode estar vazio!");
                                    break;
                                }
                                biblioteca.editarAutorLivro(idItem, novoAutor);

                            } else if (item instanceof Revista) {
                                System.out.print("Nova edição: ");
                                int novaEdicao = lerInteiro(teclado);
                                if (novaEdicao <= 0) {
                                    System.out.println("A edição deve ser maior que zero!");
                                    break;
                                }
                                biblioteca.editarEdicaoRevista(idItem, novaEdicao);

                            } else if (item instanceof DVD) {
                                System.out.print("Nova duração: ");
                                int novaDuracao = lerInteiro(teclado);
                                if (novaDuracao <= 0) {
                                    System.out.println("A duração deve ser maior que zero!");
                                    break;
                                }
                                biblioteca.editarDuracaoDVD(idItem, novaDuracao);
                            }
                            break;

                        default:
                            System.out.println("Opção inválida!");
                            break;
                    }

                    break;
                }

                case 6: {
                    System.out.println("=== EDITAR USUÁRIO ===");

                    System.out.print("Digite o ID do usuário: ");
                    int idUsuario = lerInteiro(teclado);
                    if (idUsuario <= 0) {
                        System.out.println("O ID deve ser maior que zero!");
                        break;
                    }

                    System.out.print("Digite o novo nome: ");
                    String novoNome = teclado.nextLine();
                    if (novoNome.isBlank()) {
                        System.out.println("O nome não pode estar vazio!");
                        break;
                    }

                    biblioteca.editarUsuario(idUsuario, novoNome);

                    break;
                }
                case 7: {
                    System.out.println("=== REMOVER ITEM ===");

                    System.out.print("Digite o ID do item: ");
                    int idItem = lerInteiro(teclado);
                    if (idItem <= 0) {
                        System.out.println("O ID deve ser maior que zero!");
                        break;
                    }

                    biblioteca.removerItem(idItem);

                    break;
                }
                case 8: {
                    System.out.println("=== REMOVER USUÁRIO ===");

                    System.out.print("Digite o ID do usuário: ");
                    int idUsuario = lerInteiro(teclado);
                    if (idUsuario <= 0) {
                        System.out.println("O ID deve ser maior que zero!");
                        break;
                    }

                    biblioteca.removerUsuario(idUsuario);

                    break;
                } 
                
                case 9: {
                    System.out.println("=== REALIZAR EMPRÉSTIMO ===");

                    System.out.print("Digite o ID do usuário: ");
                    int idUsuario = lerInteiro(teclado);
                    if (idUsuario <= 0) {
                        System.out.println("O ID do usuário deve ser maior que zero!");
                        break;
                    }

                    System.out.print("Digite o ID do item: ");
                    int idItem = lerInteiro(teclado);

                    if (idItem <= 0) {
                        System.out.println("O ID do item deve ser maior que zero!");
                        break;
                    }

                    try {
                        biblioteca.realizarEmprestimo(idUsuario, idItem);
                    } catch (BibliotecaException e) {
                        System.out.println("Erro: " + e.getMessage());
                    }

                    break;
                }
                case 10: {
                    System.out.println("=== DEVOLVER ITEM ===");

                    System.out.print("Digite o ID do item: ");
                    int idItem = lerInteiro(teclado);
                    if (idItem <= 0) {
                        System.out.println("O ID deve ser maior que zero!");
                        break;
                    }

                    try {
                        biblioteca.devolverItem(idItem);
                    } catch (BibliotecaException e) {
                        System.out.println("Erro: " + e.getMessage());
                    }

                    break;
                }
                
                case 11: {
                    System.out.println("=== PESQUISAR ITENS ===");
                    System.out.println("1 - Pesquisar por título");
                    System.out.println("2 - Pesquisar por autor");
                    System.out.println("3 - Pesquisar por tipo");
                    System.out.println("4 - Pesquisar por disponibilidade");
                    System.out.print("Escolha uma opção: ");

                    int opcaoPesquisa = lerInteiro(teclado);
                    if (opcaoPesquisa < 1 || opcaoPesquisa > 4) {
                        System.out.println("Opção inválida!");
                        break;
                    }

                    ArrayList<ItemBiblioteca> resultados = new ArrayList<>();
                    boolean pesquisaValida = true;

                    switch (opcaoPesquisa) {
                        case 1:
                            System.out.print("Digite o título: ");
                            String titulo = teclado.nextLine();
                            if (titulo.isBlank()) {
                                System.out.println("O título não pode estar vazio!");
                                pesquisaValida = false;
                                break;
                            }
                            resultados = biblioteca.pesquisarPorTitulo(titulo);
                            break;

                        case 2:
                            System.out.print("Digite o autor: ");
                            String autor = teclado.nextLine();
                            if (autor.isBlank()) {
                                System.out.println("O autor não pode estar vazio!");
                                pesquisaValida = false;
                                break;
                            }
                            resultados = biblioteca.pesquisarPorAutor(autor);
                            break;

                        case 3:
                            System.out.print("Digite o tipo (Livro, Revista ou DVD): ");
                            String tipo = teclado.nextLine();
                            if (!tipo.equalsIgnoreCase("Livro") &&
                                !tipo.equalsIgnoreCase("Revista") &&
                                !tipo.equalsIgnoreCase("DVD")) {
                                System.out.println("Tipo inválido! Informe Livro, Revista ou DVD.");
                                pesquisaValida = false;
                                break;
                            }
                            resultados = biblioteca.pesquisarPorTipo(tipo);
                            break;

                        case 4:
                            System.out.println("1 - Disponíveis");
                            System.out.println("2 - Emprestados");
                            System.out.print("Escolha: ");

                            int disponibilidade = lerInteiro(teclado);

                            if (disponibilidade == 1) {
                                resultados = biblioteca.pesquisarPorDisponibilidade(true);
                            } else if (disponibilidade == 2) {
                                resultados = biblioteca.pesquisarPorDisponibilidade(false);
                            } else {
                                System.out.println("Opção inválida!");
                                pesquisaValida = false;
                                break;
                            }
                            break;

                        default:
                            System.out.println("Opção inválida!");
                            break;
                    }

                    if (!pesquisaValida) {
                        break;
                    }

                    if (resultados.isEmpty()) {
                        System.out.println("Nenhum item encontrado.");
                    } else {
                        System.out.println("=== RESULTADOS ===");

                        for (ItemBiblioteca item : resultados) {
                            System.out.println(item);
                        }
                    }

                    break;
                }

                case 12:
                    biblioteca.listarEmprestimos();
                    break;

                case 0:
                    try {
                        biblioteca.salvarDados(repository);
                        System.out.println("Dados salvos com sucesso!");
                    }catch(IOException e){
                        System.out.println("Erro ao salvar: " + e.getMessage());
                        resposta = -1;
                    }
                    break;

                default:
                    System.out.println("Opção inválida!");
                    break;
            }
            
        }while(resposta != 0);


        teclado.close();
    }
}