package src.service;

import java.util.ArrayList;
import src.interfaces.IPesquisavel;
import src.model.DVD;
import src.model.Emprestimo;
import src.model.ItemBiblioteca;
import src.model.Livro;
import src.model.Revista;
import src.model.Usuario;
import src.exception.BibliotecaException;
import java.io.IOException;
import src.repository.ArquivoRepository;

public class BibliotecaService implements IPesquisavel{
    private ArrayList<ItemBiblioteca> itens;
    private ArrayList<Usuario> usuarios;
    private ArrayList<Emprestimo> emprestimos;

    public BibliotecaService(){
        this.itens = new ArrayList<>();
        this.usuarios = new ArrayList<>();
        this.emprestimos = new ArrayList<>();
    }

    public void cadastrarItem(ItemBiblioteca item){
        if (item == null) {
            System.out.println("Não é possível cadastrar um item nulo.");
            return;
        }
        if (item.getId() <= 0 || item.getAno() <= 0 || !textoValido(item.getTitulo())) {
            System.out.println("Dados do item inválidos: verifique ID, título e ano.");
            return;
        }
        if (item instanceof Livro) {
            if (!textoValido(((Livro) item).getAutor())) {
                System.out.println("O autor do livro é inválido.");
                return;
            }
        } else if (item instanceof Revista) {
            if (((Revista) item).getEdicao() <= 0) {
                System.out.println("A edição da revista deve ser maior que zero.");
                return;
            }
        } else if (item instanceof DVD) {
            if (((DVD) item).getDuracao() <= 0) {
                System.out.println("A duração do DVD deve ser maior que zero.");
                return;
            }
        } else {
            System.out.println("Tipo de item não reconhecido.");
            return;
        }

        if(buscarItemId(item.getId()) != null){
            System.out.println("Ja existe um item com esse id");
            return;
        }else{
            itens.add(item);
        }
    }

    public void listarItens(){
        for (ItemBiblioteca item : itens) {
            System.out.println(item);
        }
    }

    public void cadastrarUsuario(Usuario usuario){
        if (usuario == null) {
            System.out.println("Não é possível cadastrar um usuário nulo.");
            return;
        }
        if (usuario.getId() <= 0 || !textoValido(usuario.getNome())) {
            System.out.println("Dados do usuário inválidos: verifique ID e nome.");
            return;
        }
        if (!(usuario instanceof src.model.Aluno)
                && !(usuario instanceof src.model.Professor)
                && !(usuario instanceof src.model.Funcionario)) {
            System.out.println("Tipo de usuário não reconhecido.");
            return;
        }

        if(buscarUsuarioId(usuario.getId()) != null){
            System.out.println("Ja existe um usuario com esse id");
            return;
        }else{
            usuarios.add(usuario);
        }
       
    }

    public void listarUsuarios(){
        for (Usuario usuario : usuarios) {
            System.out.println(usuario);
        }
    }

    public ItemBiblioteca buscarItemId(int id){
        for (ItemBiblioteca item : itens) {
            if(id == item.getId()){
                return item;
            }
        }
        return null;
    }

    public Usuario buscarUsuarioId(int id){
        for(Usuario usuario : usuarios){
            if(id == usuario.getId()){
                return usuario;
            }
        }
        return null;
    }

    // Verifica a disponibilidade do item e o limite de empréstimos do usuário.
    public void realizarEmprestimo(int idUsuario,int idItem) throws BibliotecaException{
        if (idUsuario <= 0 || idItem <= 0) {
            throw new BibliotecaException("Os IDs do usuário e do item devem ser maiores que zero.");
        }

        Usuario usuario = buscarUsuarioId(idUsuario);
        ItemBiblioteca item = buscarItemId(idItem);
        

        if(usuario == null){
            throw new BibliotecaException("Usuario não encontrado");
        }
        if(item == null){
            throw new BibliotecaException("Item não encontrado");
        }

        if (item.isStatus() == false) {
            throw new BibliotecaException("Item já está emprestado");
        }

        int quantidadeEmprestimos = 0;
        
        for (Emprestimo registrado : emprestimos) {
            if (registrado.getItem().getId() == idItem && registrado.getDataDevolucao() == null) {
                throw new BibliotecaException("Este item já possui um empréstimo ativo.");
            }
        }

        for (Emprestimo emprestimo : emprestimos) {
            if(emprestimo.getUsuario().getId() == usuario.getId() && emprestimo.getDataDevolucao() == null){
                quantidadeEmprestimos++;
            }
        }

        if(quantidadeEmprestimos >= usuario.getLimiteEmprestimos()){
            throw new BibliotecaException("Limite de emprestimos atingido");
        }

        adicionarEmprestimo(usuario, item);
        item.setStatus(false);
    }

    public void listarEmprestimos(){
        for (Emprestimo emprestimo : emprestimos){
            System.out.println(emprestimo);
        }
    }

    public void devolverItem(int id) throws BibliotecaException{
        if (id <= 0) {
            throw new BibliotecaException("O ID do item deve ser maior que zero.");
        }

        for (Emprestimo emprestimo : emprestimos) {
            if(emprestimo.getItem().getId() == id && emprestimo.getDataDevolucao() == null){
                emprestimo.registrarDevolucao();
                double multa = emprestimo.calcularMulta();
                System.out.println("Valor da multa: " + multa);
                emprestimo.getItem().setStatus(true);
                return;
            }
        }
        throw new BibliotecaException("Nenhum emprestimo ativo encontrado para esse item.");
    }

    public void editarItem(int id,String novoTitulo,int novoAno){
        if (id <= 0 || novoAno <= 0 || !textoValido(novoTitulo)) {
            System.out.println("Dados inválidos: verifique ID, título e ano.");
            return;
        }

        ItemBiblioteca item = buscarItemId(id);
        if(buscarItemId(id) == null){
            System.out.println("Item não encontrado");
            return;
        }
        item.setTitulo(novoTitulo);
        item.setAno(novoAno);
        System.out.println("Item atualizado com sucesso");
    }


    public void editarAutorLivro(int id, String novoAutor) {
        if (id <= 0 || !textoValido(novoAutor)) {
            System.out.println("ID ou autor inválido.");
            return;
        }

        ItemBiblioteca item = buscarItemId(id);

        if (item instanceof Livro) {
            Livro livro = (Livro) item; 
            livro.setAutor(novoAutor);
            System.out.println("Autor atualizado com sucesso!");
        } else {
            System.out.println("Livro não encontrado!");
        }
    }

    public void editarEdicaoRevista(int id, int novaEdicao) {
        if (id <= 0 || novaEdicao <= 0) {
            System.out.println("ID ou edição inválida.");
            return;
        }

        ItemBiblioteca item = buscarItemId(id);

        if (item instanceof Revista) {
            Revista revista = (Revista) item;
            revista.setEdicao(novaEdicao);
            System.out.println("Edição atualizada com sucesso!");
        } else {
            System.out.println("Revista não encontrada!");
        }
    }

    public void editarDuracaoDVD(int id, int novaDuracao) {
        if (id <= 0 || novaDuracao <= 0) {
            System.out.println("ID ou duração inválida.");
            return;
        }

        ItemBiblioteca item = buscarItemId(id);

        if (item instanceof DVD) {
            DVD dvd = (DVD) item;
            dvd.setDuracao(novaDuracao);
            System.out.println("Duração atualizada com sucesso!");
        } else {
            System.out.println("DVD não encontrado!");
        }
    }


    public void editarUsuario(int id,String novoNome){
        if (id <= 0 || !textoValido(novoNome)) {
            System.out.println("ID ou nome inválido.");
            return;
        }

        Usuario usuario = buscarUsuarioId(id);
        if(usuario == null){
            System.out.println("Usuario não encontrado");
            return;
        }

        usuario.setNome(novoNome);
        System.out.println("Usuário atualizado com sucesso");
    }

    public void removerItem(int id){
        if (id <= 0) {
            System.out.println("O ID do item deve ser maior que zero.");
            return;
        }

        ItemBiblioteca item = buscarItemId(id);
        if(item == null){
            System.out.println("Item não encontrado");
            return;
        }else if(item.isStatus() == false){
            System.out.println("Não é possível remover um item emprestado");
            return;
        }

        for (Emprestimo emprestimo : emprestimos) {
            if (emprestimo.getItem().getId() == id) {
                System.out.println("Não é possível remover: item possui histórico de empréstimos.");
                return;
            }
        }
        itens.remove(item);
        System.out.println("Removido com sucesso");
    }

    public void removerUsuario(int id){
        if (id <= 0) {
            System.out.println("O ID do usuário deve ser maior que zero.");
            return;
        }

        Usuario usuario = buscarUsuarioId(id);
        if(usuario == null){
            System.out.println("Usuario não encontrado");
            return;
        }

        for (Emprestimo emprestimo : emprestimos) {
            if(emprestimo.getUsuario().getId() == usuario.getId() && emprestimo.getDataDevolucao() == null){
                System.out.println("Não é possivel remover usuários com emprestimos ativos");
                return;
            }
        }
        for (Emprestimo emprestimo : emprestimos) {
            if (emprestimo.getUsuario().getId() == id) {
                System.out.println("Não é possível remover: usuário possui histórico de empréstimos.");
                return;
            }
        }
        usuarios.remove(usuario);
        System.out.println("Usuário removido com sucesso");
    }

    public ArrayList<ItemBiblioteca> getItens() {
        return itens;
    }

    public void carregarDados(ArquivoRepository repository) throws IOException{
        if (repository == null) {
            throw new IOException("Repositório não informado para carregamento.");
        }

        repository.recuperarSalvamento();
        ArrayList<Usuario> usuariosCarregados = repository.carregarUsuarios();
        ArrayList<ItemBiblioteca> itensCarregados = repository.carregarItens();
        ArrayList<Emprestimo> emprestimosCarregados = repository.carregarEmprestimos(usuariosCarregados, itensCarregados);

        this.usuarios = usuariosCarregados;
        this.itens = itensCarregados;
        this.emprestimos = emprestimosCarregados;
    }

    public void salvarDados(ArquivoRepository repository) throws IOException {
        if (repository == null) {
            throw new IOException("Repositório não informado para salvamento.");
        }

        repository.salvarTodos(this.usuarios, this.itens, this.emprestimos);
    }

    // Centraliza a criação e o armazenamento dos empréstimos da biblioteca.
    private void adicionarEmprestimo(Usuario usuario, ItemBiblioteca item) {
        emprestimos.add(new Emprestimo(usuario, item));
    }

    @Override
    public ArrayList<ItemBiblioteca> pesquisarPorTitulo(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            System.out.println("Informe um título válido para a pesquisa.");
            return new ArrayList<>();
        }

        ArrayList<ItemBiblioteca> resultados = new ArrayList<>();
        for (ItemBiblioteca item : itens) {
            if(item.getTitulo().toLowerCase().contains(titulo.toLowerCase())){
                resultados.add(item);
            }
        }
        return resultados;
    }

    @Override
    public ArrayList<ItemBiblioteca> pesquisarPorAutor(String autor){
        if (autor == null || autor.isBlank()) {
            System.out.println("Informe um autor válido para a pesquisa.");
            return new ArrayList<>();
        }

        ArrayList<ItemBiblioteca> resultados = new ArrayList<>();
        for (ItemBiblioteca item : itens) {
            if(item instanceof Livro){
                Livro livro = (Livro) item;
                if(livro.getAutor().toLowerCase().contains(autor.toLowerCase())){
                    resultados.add(livro);
                }
            }
        }
        return resultados;
    }

    @Override
    public ArrayList<ItemBiblioteca> pesquisarPorTipo(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            System.out.println("Informe um tipo válido para a pesquisa.");
            return new ArrayList<>();
        }

        ArrayList<ItemBiblioteca> resultados = new ArrayList<>();
        for (ItemBiblioteca item : itens) {
            if(item instanceof Livro && tipo.equalsIgnoreCase("Livro")){
                resultados.add(item);
            }
            if(item instanceof DVD && tipo.equalsIgnoreCase("DVD")){
                resultados.add(item);
            }
            if(item instanceof Revista && tipo.equalsIgnoreCase("Revista")){
                resultados.add(item);
            }
        }
        return resultados;
    }

    @Override
    public ArrayList<ItemBiblioteca> pesquisarPorDisponibilidade(boolean disponivel){
        ArrayList<ItemBiblioteca> resultados = new ArrayList<>();
        for (ItemBiblioteca item : itens) {
            if(item.isStatus() == disponivel){
                resultados.add(item);
            }
        }
        return resultados;
    }

    
    private static boolean textoValido(String valor) {
        return valor != null && !valor.isBlank()
                && !valor.contains(";") && !valor.contains("\n") && !valor.contains("\r");
    }

}