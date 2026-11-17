package src.model;

public abstract class Usuario {
    private int id;
    private String nome;
    private String tipo;
    private int limiteEmprestimos;

    public Usuario(int id,String nome,String tipo,int limiteEmprestimos){
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.limiteEmprestimos = limiteEmprestimos;
    }

    public int getId(){
        return id;
    }

    public String getNome(){
        return nome;
    }

    public String getTipo(){
        return tipo;
    }

    public int getLimiteEmprestimos(){
        return limiteEmprestimos;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public abstract int calcularPrazoEmprestimo();

    @Override
    public String toString(){
        return "Id: " + id
        + "\nNome: " + nome
        + "\nTipo: " + tipo
        + "\nLimite de emprestimo: " + limiteEmprestimos;
    }
}
