package src.model;

public class Revista extends ItemBiblioteca{
    private int edicao;

    public Revista(int id,String titulo,int ano,boolean status,int edicao){
        super(id, titulo, ano, status);
        this.edicao = edicao;
    }

    public int getEdicao(){
        return edicao;
    }

    public void setEdicao(int edicao){
        this.edicao = edicao;
    }

    @Override
    public String toString(){
        return super.toString()
        + "\nEdição: " + edicao;
    }

}
