package src.repository;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.AtomicMoveNotSupportedException;
import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.FileReader;

import src.model.ItemBiblioteca;
import src.model.Livro;
import src.model.Revista;
import src.model.DVD;

import java.time.LocalDate;

import src.model.Usuario;
import src.model.Aluno;
import src.model.Professor;
import src.model.Funcionario;
import src.model.Emprestimo;


public class ArquivoRepository {
    public void salvarItens(ArrayList<? extends ItemBiblioteca> itens) throws IOException {
        salvarItens(itens, Path.of("data/itens.txt"));
    }

    private void salvarItens(ArrayList<? extends ItemBiblioteca> itens, Path destino) throws IOException {
        if (itens == null) {
            throw new IOException("Lista de itens não pode ser nula.");
        }
        ArrayList<Integer> idsVerificados = new ArrayList<>();
        for (ItemBiblioteca item : itens) {
            if (item == null || item.getId() <= 0 || item.getAno() <= 0) {
                throw new IOException("Item nulo ou com ID/ano inválido.");
            }
            validarTexto(item.getTitulo(), "título do item");
            if (idsVerificados.contains(item.getId())) {
                throw new IOException("ID de item duplicado: " + item.getId());
            }
            idsVerificados.add(item.getId());
            if (item instanceof Livro) {
                validarTexto(((Livro) item).getAutor(), "autor do livro");
            } else if (item instanceof Revista) {
                if (((Revista) item).getEdicao() <= 0) {
                    throw new IOException("Edição inválida para o item " + item.getId());
                }
            } else if (item instanceof DVD) {
                if (((DVD) item).getDuracao() <= 0) {
                    throw new IOException("Duração inválida para o item " + item.getId());
                }
            } else {
                throw new IOException("Tipo de item desconhecido: " + item.getId());
            }
        }

        try (FileWriter writer = new FileWriter(destino.toFile())){
            for (ItemBiblioteca item : itens) {
                if(item instanceof Livro){
                    Livro livro = (Livro) item;
                    writer.write("Livro;" + livro.getId() + ";" +
                    livro.getTitulo() + ";" +
                    livro.getAno() + ";" +
                    livro.isStatus() + ";" +
                    livro.getAutor() + System.lineSeparator());
                }else if(item instanceof Revista){
                    Revista revista = (Revista) item;
                    writer.write("Revista;" + revista.getId() + ";" +
                    revista.getTitulo() + ";" +
                    revista.getAno() + ";" +
                    revista.isStatus() + ";" +
                    revista.getEdicao() + System.lineSeparator());
                }else if(item instanceof DVD){
                    DVD dvd = (DVD) item;
                    writer.write("DVD;" + dvd.getId() + ";" +
                    dvd.getTitulo() + ";" +
                    dvd.getAno() + ";" +
                    dvd.isStatus() + ";" +
                    dvd.getDuracao() + System.lineSeparator());
                }
                
            }
        }
    }

    public ArrayList<ItemBiblioteca> carregarItens() throws IOException {
        ArrayList<ItemBiblioteca> itens = new ArrayList<>();
        ArrayList<Integer> idsLidos = new ArrayList<>();
        int numeroLinha = 0;
        if (!java.nio.file.Files.isRegularFile(java.nio.file.Path.of("data/itens.txt"))) {
            throw new IOException("Arquivo data/itens.txt não encontrado ou inválido.");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader("data/itens.txt"))) {
            String linha;

            while ((linha = reader.readLine()) != null) {
                numeroLinha++;
                if (linha.isBlank()) {
                    continue;
                }
                try {
                String[] dados = linha.split(";");
                dados = linha.split(";", -1);
                if (dados.length != 6) {
                    throw new IOException("Registro de item incompleto na linha " + numeroLinha + ": " + linha);
                }
                validarTexto(dados[2], "título na linha " + numeroLinha);
                if (!dados[4].equals("true") && !dados[4].equals("false")) {
                    throw new IOException("Status inválido na linha " + numeroLinha + ": " + dados[4]);
                }
                int idLido = Integer.parseInt(dados[1]);
                int anoLido = Integer.parseInt(dados[3]);
                if (idLido <= 0 || anoLido <= 0) {
                    throw new IOException("ID ou ano inválido na linha " + numeroLinha);
                }
                if (idsLidos.contains(idLido)) {
                    throw new IOException("ID de item duplicado na linha " + numeroLinha + ": " + idLido);
                }
                idsLidos.add(idLido);
                if (dados[0].equals("Livro")) {
                    int id = Integer.parseInt(dados[1]);
                    String titulo = dados[2];
                    int ano = Integer.parseInt(dados[3]);
                    boolean status = Boolean.parseBoolean(dados[4]);
                    String autor = dados[5];
                    validarTexto(autor, "autor na linha " + numeroLinha);

                    Livro livro = new Livro(id, titulo, ano, status, autor);
                    itens.add(livro);
                }else if(dados[0].equals("Revista")){
                    int id = Integer.parseInt(dados[1]);
                    String titulo = dados[2];
                    int ano = Integer.parseInt(dados[3]);
                    boolean status = Boolean.parseBoolean(dados[4]);
                    int edicao = Integer.parseInt(dados[5]);
                    if (edicao <= 0) {
                        throw new IOException("Edição inválida na linha " + numeroLinha);
                    }

                    Revista revista = new Revista(id, titulo, ano, status, edicao);
                    itens.add(revista);
                }else if (dados[0].equalsIgnoreCase("DVD")) {
                    int id = Integer.parseInt(dados[1]);
                    String titulo = dados[2];
                    int ano = Integer.parseInt(dados[3]);
                    boolean status = Boolean.parseBoolean(dados[4]);
                    int duracao = Integer.parseInt(dados[5]);
                    if (duracao <= 0) {
                        throw new IOException("Duração inválida na linha " + numeroLinha);
                    }

                    DVD dvd = new DVD(id, titulo, ano, status, duracao);
                    itens.add(dvd);
                }
                else {
                    throw new IOException("Tipo de item desconhecido na linha " + numeroLinha + ": " + dados[0]);
                }
                } catch (NumberFormatException e) {
                    throw new IOException("Número inválido em data/itens.txt, linha " + numeroLinha + ": " + linha, e);
                }
            }
            
        }

        return itens;
    }

    public void salvarUsuarios(ArrayList<Usuario> usuarios) throws IOException {
        salvarUsuarios(usuarios, Path.of("data/usuarios.txt"));
    }

    private void salvarUsuarios(ArrayList<Usuario> usuarios, Path destino) throws IOException {
        if (usuarios == null) {
            throw new IOException("Lista de usuários não pode ser nula.");
        }
        ArrayList<Integer> idsVerificados = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            if (usuario == null || usuario.getId() <= 0) {
                throw new IOException("Usuário nulo ou com ID inválido.");
            }
            validarTexto(usuario.getNome(), "nome do usuário");
            if (idsVerificados.contains(usuario.getId())) {
                throw new IOException("ID de usuário duplicado: " + usuario.getId());
            }
            idsVerificados.add(usuario.getId());
            if (!(usuario instanceof Aluno || usuario instanceof Professor || usuario instanceof Funcionario)) {
                throw new IOException("Tipo de usuário desconhecido: " + usuario.getId());
            }
        }

        try (FileWriter writer = new FileWriter(destino.toFile())) {

            for (Usuario usuario : usuarios) {
                String tipo;

                if (usuario instanceof Aluno) {
                    tipo = "Aluno";
                } else if (usuario instanceof Professor) {
                    tipo = "Professor";
                } else if (usuario instanceof Funcionario) {
                    tipo = "Funcionario";
                } else {
                    throw new IOException("Tipo de usuário desconhecido");
                }

                writer.write(tipo + ";" +
                        usuario.getId() + ";" +
                        usuario.getNome() + System.lineSeparator());
            }
        }
    }

    public ArrayList<Usuario> carregarUsuarios() throws IOException {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        ArrayList<Integer> idsLidos = new ArrayList<>();
        int numeroLinha = 0;
        if (!java.nio.file.Files.isRegularFile(java.nio.file.Path.of("data/usuarios.txt"))) {
            throw new IOException("Arquivo data/usuarios.txt não encontrado ou inválido.");
        }

        try (BufferedReader reader = new BufferedReader(
                new FileReader("data/usuarios.txt"))) {

            String linha;

            while ((linha = reader.readLine()) != null) {
                numeroLinha++;
                if (linha.isBlank()) {
                    continue;
                }
                try {
                if (linha.isEmpty()) {
                    continue;
                }

                String[] dados = linha.split(";");
                dados = linha.split(";", -1);

                if (dados.length != 3) {
                    throw new IOException("Registro de usuário inválido: " + linha);
                }
                validarTexto(dados[2], "nome na linha " + numeroLinha);

                int id = Integer.parseInt(dados[1]);
                String nome = dados[2];
                if (id <= 0) {
                    throw new IOException("ID de usuário inválido na linha " + numeroLinha);
                }
                if (idsLidos.contains(id)) {
                    throw new IOException("ID de usuário duplicado na linha " + numeroLinha + ": " + id);
                }
                idsLidos.add(id);

                if (dados[0].equals("Aluno")) {
                    usuarios.add(new Aluno(id, nome));

                } else if (dados[0].equals("Professor")) {
                    usuarios.add(new Professor(id, nome));

                } else if (dados[0].equals("Funcionario")) {
                    usuarios.add(new Funcionario(id, nome));

                } else {
                    throw new IOException("Tipo de usuário desconhecido: " + dados[0]);
                }
                } catch (NumberFormatException e) {
                    throw new IOException("Número inválido em data/usuarios.txt, linha " + numeroLinha + ": " + linha, e);
                }
            }
        }

        return usuarios;
    }

    public void salvarEmprestimos(ArrayList<Emprestimo> emprestimos) throws IOException {
        salvarEmprestimos(emprestimos, Path.of("data/emprestimos.txt"));
    }

    private void salvarEmprestimos(ArrayList<Emprestimo> emprestimos, Path destino) throws IOException {
        if (emprestimos == null) {
            throw new IOException("Lista de empréstimos não pode ser nula.");
        }
        ArrayList<Integer> itensAtivos = new ArrayList<>();
        for (Emprestimo emprestimo : emprestimos) {
            if (emprestimo == null || emprestimo.getUsuario() == null || emprestimo.getItem() == null
                    || emprestimo.getDataEmprestimo() == null || emprestimo.getDataPrevisaoDevolucao() == null) {
                throw new IOException("Empréstimo incompleto ou com referência nula.");
            }
            if (emprestimo.getUsuario().getId() <= 0 || emprestimo.getItem().getId() <= 0) {
                throw new IOException("ID inválido em empréstimo.");
            }
            if (!emprestimo.getDataPrevisaoDevolucao().isAfter(emprestimo.getDataEmprestimo())) {
                throw new IOException("Previsão de devolução deve ser posterior ao empréstimo.");
            }
            if (emprestimo.getDataDevolucao() != null
                    && emprestimo.getDataDevolucao().isBefore(emprestimo.getDataEmprestimo())) {
                throw new IOException("Devolução anterior à data do empréstimo.");
            }
            if (emprestimo.getDataDevolucao() == null) {
                int idItem = emprestimo.getItem().getId();
                if (itensAtivos.contains(idItem)) {
                    throw new IOException("Mais de um empréstimo ativo para o item " + idItem);
                }
                itensAtivos.add(idItem);
                if (emprestimo.getItem().isStatus()) {
                    throw new IOException("Item disponível com empréstimo ativo: " + idItem);
                }
            }
        }

        try (FileWriter writer = new FileWriter(destino.toFile())) {

            for (Emprestimo emprestimo : emprestimos) {
                String dataDevolucao = "";

                if (emprestimo.getDataDevolucao() != null) {
                    dataDevolucao = emprestimo.getDataDevolucao().toString();
                }

                writer.write(
                    emprestimo.getUsuario().getId() + ";" +
                    emprestimo.getItem().getId() + ";" +
                    emprestimo.getDataEmprestimo() + ";" +
                    emprestimo.getDataPrevisaoDevolucao() + ";" +
                    dataDevolucao + System.lineSeparator()
                );
            }
        }
    }

    public ArrayList<Emprestimo> carregarEmprestimos(ArrayList<Usuario> usuarios,ArrayList<ItemBiblioteca> itens) throws IOException {
        ArrayList<Emprestimo> emprestimos = new ArrayList<>();
        if (usuarios == null || itens == null) {
            throw new IOException("Listas de usuários e itens são obrigatórias para carregar empréstimos.");
        }
        ArrayList<Integer> itensAtivos = new ArrayList<>();
        int numeroLinha = 0;
        if (!java.nio.file.Files.isRegularFile(java.nio.file.Path.of("data/emprestimos.txt"))) {
            throw new IOException("Arquivo data/emprestimos.txt não encontrado ou inválido.");
        }

        try (BufferedReader reader = new BufferedReader(
                new FileReader("data/emprestimos.txt"))) {

            String linha;

            while ((linha = reader.readLine()) != null) {
                numeroLinha++;
                if (linha.isBlank()) {
                    continue;
                }
                try {
                if (linha.isEmpty()) {
                    continue;
                }

                String[] dados = linha.split(";", -1);

                if (dados.length != 5) {
                    throw new IOException("Registro de empréstimo inválido: " + linha);
                }

                int idUsuario = Integer.parseInt(dados[0]);
                int idItem = Integer.parseInt(dados[1]);
                if (idUsuario <= 0 || idItem <= 0) {
                    throw new IOException("ID inválido no empréstimo da linha " + numeroLinha);
                }

                LocalDate dataEmprestimo = LocalDate.parse(dados[2]);
                LocalDate dataPrevisao = LocalDate.parse(dados[3]);

                LocalDate dataDevolucao = null;

                if (!dados[4].isEmpty()) {
                    dataDevolucao = LocalDate.parse(dados[4]);
                }
                if (!dataPrevisao.isAfter(dataEmprestimo)) {
                    throw new IOException("Previsão inválida na linha " + numeroLinha);
                }
                if (dataDevolucao != null && dataDevolucao.isBefore(dataEmprestimo)) {
                    throw new IOException("Data de devolução anterior ao empréstimo na linha " + numeroLinha);
                }

                Usuario usuarioEncontrado = null;
                ItemBiblioteca itemEncontrado = null;

                for (Usuario usuario : usuarios) {
                    if (usuario.getId() == idUsuario) {
                        usuarioEncontrado = usuario;
                        break;
                    }
                }

                for (ItemBiblioteca item : itens) {
                    if (item.getId() == idItem) {
                        itemEncontrado = item;
                        break;
                    }
                }

                if (usuarioEncontrado == null || itemEncontrado == null) {
                    throw new IOException(
                        "Usuário ou item não encontrado para o empréstimo"
                    );
                }

                if (dataDevolucao == null) {
                    if (itensAtivos.contains(idItem)) {
                        throw new IOException("Empréstimos ativos duplicados para o item " + idItem);
                    }
                    itensAtivos.add(idItem);
                    if (itemEncontrado.isStatus()) {
                        throw new IOException("Item " + idItem + " disponível, mas com empréstimo ativo.");
                    }
                }

                Emprestimo emprestimo = new Emprestimo(
                    usuarioEncontrado,
                    itemEncontrado,
                    dataEmprestimo,
                    dataPrevisao,
                    dataDevolucao
                );

                emprestimos.add(emprestimo);
                } catch (NumberFormatException | java.time.format.DateTimeParseException e) {
                    throw new IOException("Dado inválido em data/emprestimos.txt, linha " + numeroLinha + ": " + linha, e);
                }
            }
        }

        for (ItemBiblioteca item : itens) {
            boolean possuiEmprestimoAtivo = itensAtivos.contains(item.getId());
            if (item.isStatus() == possuiEmprestimoAtivo) {
                throw new IOException("Status do item " + item.getId() + " inconsistente com os empréstimos.");
            }
        }

        return emprestimos;
    }

    private static final Path PASTA_DADOS = Path.of("data");
    private static final Path PASTA_TRANSACAO = PASTA_DADOS.resolve(".salvamento-biblioteca");
    private static final String[] ARQUIVOS = {"usuarios.txt", "itens.txt", "emprestimos.txt"};

    // Protege a persistência utilizando arquivos temporários e cópias de segurança.
    public void salvarTodos(ArrayList<Usuario> usuarios,
                            ArrayList<? extends ItemBiblioteca> itens,
                            ArrayList<Emprestimo> emprestimos) throws IOException {
        recuperarSalvamento();
        validarRelacoes(usuarios, itens, emprestimos);
        Files.createDirectories(PASTA_DADOS);
        Files.createDirectory(PASTA_TRANSACAO);

        try {
            salvarUsuarios(usuarios, PASTA_TRANSACAO.resolve("usuarios.txt.novo"));
            salvarItens(itens, PASTA_TRANSACAO.resolve("itens.txt.novo"));
            salvarEmprestimos(emprestimos, PASTA_TRANSACAO.resolve("emprestimos.txt.novo"));

            for (String nome : ARQUIVOS) {
                Path original = PASTA_DADOS.resolve(nome);
                if (Files.exists(original)) {
                    if (!Files.isRegularFile(original)) {
                        throw new IOException("Arquivo de dados inválido: " + original);
                    }
                    Files.copy(original, PASTA_TRANSACAO.resolve(nome + ".bak"));
                } else {
                    Files.createFile(PASTA_TRANSACAO.resolve(nome + ".ausente"));
                }
            }

            Files.createFile(PASTA_TRANSACAO.resolve("pendente"));

            for (String nome : ARQUIVOS) {
                moverArquivo(PASTA_TRANSACAO.resolve(nome + ".novo"), PASTA_DADOS.resolve(nome));
            }

            Files.delete(PASTA_TRANSACAO.resolve("pendente"));
        } catch (IOException erro) {
            if (Files.exists(PASTA_TRANSACAO.resolve("pendente"))) {
                try {
                    restaurarBackups();
                    Files.delete(PASTA_TRANSACAO.resolve("pendente"));
                } catch (IOException erroRestauracao) {
                    erro.addSuppressed(erroRestauracao);
                }
            }
            throw erro;
        } finally {
            if (!Files.exists(PASTA_TRANSACAO.resolve("pendente"))) {
                try {
                    limparTransacao();
                } catch (IOException erroLimpeza) {
                    System.err.println("Aviso: não foi possível limpar os arquivos temporários: "
                            + erroLimpeza.getMessage());
                }
            }
        }
    }

    public void recuperarSalvamento() throws IOException {
        if (!Files.exists(PASTA_TRANSACAO)) {
            return;
        }
        if (!Files.isDirectory(PASTA_TRANSACAO)) {
            throw new IOException("Pasta de salvamento temporário inválida.");
        }

        if (Files.exists(PASTA_TRANSACAO.resolve("pendente"))) {
            restaurarBackups();
            Files.delete(PASTA_TRANSACAO.resolve("pendente"));
        }
        limparTransacao();
    }

    private void validarRelacoes(ArrayList<Usuario> usuarios,
                                ArrayList<? extends ItemBiblioteca> itens,
                                ArrayList<Emprestimo> emprestimos) throws IOException {
        if (usuarios == null || itens == null || emprestimos == null) {
            throw new IOException("As listas de usuários, itens e empréstimos são obrigatórias.");
        }

        for (Emprestimo emprestimo : emprestimos) {
            if (emprestimo == null || emprestimo.getUsuario() == null || emprestimo.getItem() == null) {
                throw new IOException("Empréstimo com referências inválidas.");
            }

            boolean usuarioExiste = false;
            boolean itemExiste = false;

            for (Usuario usuario : usuarios) {
                if (usuario != null && usuario.getId() == emprestimo.getUsuario().getId()) {
                    usuarioExiste = true;
                    break;
                }
            }
            for (ItemBiblioteca item : itens) {
                if (item != null && item.getId() == emprestimo.getItem().getId()) {
                    itemExiste = true;
                    break;
                }
            }

            if (!usuarioExiste || !itemExiste) {
                throw new IOException("Empréstimo referencia usuário ou item inexistente.");
            }
        }

        for (ItemBiblioteca item : itens) {
            if (item == null) {
                throw new IOException("Lista contém item nulo.");
            }

            boolean emprestado = false;
            for (Emprestimo emprestimo : emprestimos) {
                if (emprestimo.getItem().getId() == item.getId()
                        && emprestimo.getDataDevolucao() == null) {
                    emprestado = true;
                    break;
                }
            }

            if (item.isStatus() == emprestado) {
                throw new IOException("Status do item " + item.getId()
                        + " inconsistente com seus empréstimos.");
            }
        }
    }

    private void restaurarBackups() throws IOException {
        for (String nome : ARQUIVOS) {
            Path original = PASTA_DADOS.resolve(nome);
            Path backup = PASTA_TRANSACAO.resolve(nome + ".bak");
            Path ausente = PASTA_TRANSACAO.resolve(nome + ".ausente");

            if (Files.isRegularFile(ausente)) {
                Files.deleteIfExists(original);
            } else if (Files.isRegularFile(backup)) {
                Path restaurando = PASTA_TRANSACAO.resolve(nome + ".restaurando");
                Files.copy(backup, restaurando, StandardCopyOption.REPLACE_EXISTING);
                moverArquivo(restaurando, original);
            } else {
                throw new IOException("Cópia de segurança ausente: " + nome);
            }
        }
    }

    private static void moverArquivo(Path origem, Path destino) throws IOException {
        try {
            Files.move(origem, destino,
                    StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(origem, destino, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private void limparTransacao() throws IOException {
        for (String nome : ARQUIVOS) {
            Files.deleteIfExists(PASTA_TRANSACAO.resolve(nome + ".novo"));
            Files.deleteIfExists(PASTA_TRANSACAO.resolve(nome + ".bak"));
            Files.deleteIfExists(PASTA_TRANSACAO.resolve(nome + ".ausente"));
            Files.deleteIfExists(PASTA_TRANSACAO.resolve(nome + ".restaurando"));
        }
        Files.deleteIfExists(PASTA_TRANSACAO.resolve("pendente"));
        Files.deleteIfExists(PASTA_TRANSACAO);
    }

    private void validarTexto(String valor, String campo) throws IOException {
        if (valor == null || valor.isBlank() || valor.contains(";")
                || valor.contains("\n") || valor.contains("\r")) {
            throw new IOException("Campo inválido: " + campo + ". Não use texto vazio, ';' ou quebra de linha.");
        }
    }

}
