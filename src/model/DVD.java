package src.model;

public class DVD extends ItemBiblioteca{
    private int duracao;

    public DVD(int id,String titulo,int ano,boolean status,int duracao){
        super(id, titulo, ano, status);
        this.duracao = duracao;
    }

    public int getDuracao(){
        return duracao;
    }

    public void setDuracao(int duracao){
        this.duracao = duracao;
    }

    @Override
    public String toString(){
        return super.toString()
        + "\nDuração em minutos: " + duracao;
    }
}
