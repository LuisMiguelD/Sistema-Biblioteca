package src.model;

// Herança: Livro especializa ItemBiblioteca com atributos próprios.
public class Livro extends ItemBiblioteca{
    private String autor;

    public Livro(int id,String titulo,int ano,boolean status,String autor){
        super(id,titulo,ano,status);
        this.autor = autor;
    }

    public String getAutor(){
        return autor;
    }

    public void setAutor(String autor){
        this.autor = autor;
    }

    @Override
    public String toString(){
        return super.toString()
        + "\nAutor: " + autor;
    }
}
