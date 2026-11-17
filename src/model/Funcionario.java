package src.model;

public class Funcionario extends Usuario{

    public Funcionario(int id,String nome){
        super(id, nome, "Funcionário", 4);
    }
    
    @Override
    public int calcularPrazoEmprestimo(){
        return 10;
    }
}