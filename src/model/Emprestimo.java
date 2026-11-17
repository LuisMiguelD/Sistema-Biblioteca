package src.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Emprestimo {
    private Usuario usuario;
    private ItemBiblioteca item;
    private LocalDate dataEmprestimo;
    private LocalDate dataPrevisaoDevolucao;
    private LocalDate dataDevolucao;

    public Emprestimo(Usuario usuario, ItemBiblioteca item) {
        this.usuario = usuario;
        this.item = item;
        this.dataEmprestimo = LocalDate.now();
        this.dataPrevisaoDevolucao = dataEmprestimo.plusDays(usuario.calcularPrazoEmprestimo());
        this.dataDevolucao = null;
    }

    public Usuario getUsuario(){
        return usuario;
    }

    public ItemBiblioteca getItem(){
        return item;
    }

    public LocalDate getDataEmprestimo(){
        return dataEmprestimo;
    }

    public LocalDate getDataPrevisaoDevolucao(){
        return dataPrevisaoDevolucao;
    }

    public LocalDate getDataDevolucao(){
        return dataDevolucao;
    }

    public void registrarDevolucao(){
        dataDevolucao = LocalDate.now();
    }

    public long calcularDiasAtraso(){
        if(dataDevolucao == null){
            return 0;
        }else if(ChronoUnit.DAYS.between(dataPrevisaoDevolucao, dataDevolucao) <= 0){
            return 0;
        }else{
            return ChronoUnit.DAYS.between(dataPrevisaoDevolucao, dataDevolucao);
        }
    }

    public double calcularMulta(){
        return calcularDiasAtraso() * 2;
    }

    public Emprestimo(Usuario usuario, ItemBiblioteca item, LocalDate dataEmprestimo){
        this.usuario = usuario;
        this.item = item;
        this.dataEmprestimo = dataEmprestimo;
        this.dataPrevisaoDevolucao = dataEmprestimo.plusDays(usuario.calcularPrazoEmprestimo());
        this.dataDevolucao = null;
    }

    public Emprestimo(
        Usuario usuario,
        ItemBiblioteca item,
        LocalDate dataEmprestimo,
        LocalDate dataPrevisaoDevolucao,
        LocalDate dataDevolucao) {

        this.usuario = usuario;
        this.item = item;
        this.dataEmprestimo = dataEmprestimo;
        this.dataPrevisaoDevolucao = dataPrevisaoDevolucao;
        this.dataDevolucao = dataDevolucao;
    }

    @Override
    public String toString(){
        return "Usuário:\n" + usuario
        + "\nItem:\n" + item
        + "\nData do emprestimo: " + dataEmprestimo
        + "\nData da previsão da devolução: " + dataPrevisaoDevolucao
        + "\nData da devolução: " + dataDevolucao;
    }

}
