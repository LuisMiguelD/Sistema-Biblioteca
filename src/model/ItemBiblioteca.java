package src.model;

// Abstração: define os atributos e comportamentos comuns aos itens da biblioteca.
public abstract class ItemBiblioteca {
    private int id;
    private String titulo;
    private int ano;
    private boolean status;

    public ItemBiblioteca(int id,String titulo,int ano,boolean status){
        this.id = id;
        this.titulo = titulo;
        this.ano = ano;
        this.status = status;
    }

    public int getId(){
        return id;
    }

    public String getTitulo(){
        return titulo;
    }

    public int getAno(){
        return ano;
    }

    public boolean isStatus(){
        return status;
    }

    public void setTitulo(String titulo){
        this.titulo = titulo;
    }

    public void setAno(int ano){
        this.ano = ano;
    }

    public void setStatus(boolean status){
        this.status = status;
    }

    @Override
    public String toString(){
        String textoStatus;

        if(status){
            textoStatus = "Disponivel";
        }else{
            textoStatus = "Emprestado";
        }

        return "Id: " + id
        + "\nTitulo: " + titulo
        + "\nAno: " + ano
        + "\nStatus: " + textoStatus;
    } 
}
