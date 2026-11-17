package src.model;

public class Professor extends Usuario{

    public Professor(int id,String nome){
        super(id, nome, "Professor", 5);
    }
    
    @Override
    public int calcularPrazoEmprestimo(){
        return 14;
    }
}
