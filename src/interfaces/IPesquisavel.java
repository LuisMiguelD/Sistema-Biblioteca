package src.interfaces;

import java.util.ArrayList;

import src.model.ItemBiblioteca;

// Define o contrato dos métodos de pesquisa disponíveis no sistema.
public interface IPesquisavel {
    public ArrayList<ItemBiblioteca> pesquisarPorTitulo(String titulo);
    
    public ArrayList<ItemBiblioteca> pesquisarPorAutor(String autor);

    public ArrayList<ItemBiblioteca> pesquisarPorTipo(String tipo);

    ArrayList<ItemBiblioteca> pesquisarPorDisponibilidade(boolean disponivel);
}
    

