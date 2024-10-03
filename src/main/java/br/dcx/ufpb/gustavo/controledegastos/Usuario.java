package br.dcx.ufpb.gustavo.controledegastos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Usuario implements Serializable {
    private String nome;
    private String email;
    private ArrayList<GastoPessoal> gastos;

    public Usuario(String nome, String email){
        this.nome = nome;
        this.email = email;
        this.gastos = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(nome, usuario.nome) && Objects.equals(email, usuario.email) && Objects.equals(gastos, usuario.gastos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, email, gastos);
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public List<GastoPessoal> getGastos() {
        return gastos;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double calcularTotalGastos(){
        double total = 0.0;
        for (GastoPessoal g: this.gastos){
            total += g.getValor();
        }
        return total;
    }

    public String toString(){
        return String.format("Usuário: %s | Email: %s | Total de gastos: R$%.2f", this.nome, this.email, calcularTotalGastos());
    }
}
