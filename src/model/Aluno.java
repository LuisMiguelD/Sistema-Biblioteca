package src.model;

public class Aluno extends Usuario{

    public Aluno(int id,String nome){
        super(id, nome, "Aluno", 3);
    }

    @Override
    public int calcularPrazoEmprestimo(){
        return 7;
    }
}
