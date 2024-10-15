package br.dcx.ufpb.gustavo.controledegastos;

import java.io.Serializable;
import java.util.Objects;

public class GastoPessoal implements Serializable{
    private String descricao;
    private double valor;
    private Data data;
    private Pagamentos pagamentos;


    public GastoPessoal(String descricao, double valor, Data data, Pagamentos pagamentos){
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
        this.pagamentos = pagamentos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GastoPessoal that = (GastoPessoal) o;
        return Double.compare(valor, that.valor) == 0 && Objects.equals(descricao, that.descricao) && Objects.equals(data, that.data) && Objects.equals(pagamentos, that.pagamentos);
    }


    @Override
    public int hashCode() {
        return Objects.hash(descricao, valor, data, pagamentos);
    }

    public String getDescricao() {
        return descricao;
    }

    public double getValor() {
        return valor;
    }

    public Data getData() {
        return data;
    }

    public Pagamentos getFormaPagamento() {
        return pagamentos;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.pagamentos = pagamentos;
    }

    public String toString(){
        return String.format("Descrição: %s | Valor: R$%.2f | Data: %s | Forma de pagamento: %s", this.descricao, this.valor, this.data.toString(), pagamentos);
    }
}
