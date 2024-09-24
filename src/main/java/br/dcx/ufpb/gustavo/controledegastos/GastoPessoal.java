package br.dcx.ufpb.gustavo.controledegastos;

import java.util.Objects;

public class GastoPessoal {
    private String descricao;
    private double valor;
    private Data data;
    private String formaPagamento;


    public GastoPessoal(String descricao, double valor, Data data, String formaPagamento){
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
        this.formaPagamento = formaPagamento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GastoPessoal that = (GastoPessoal) o;
        return Double.compare(valor, that.valor) == 0 && Objects.equals(descricao, that.descricao) && Objects.equals(data, that.data) && Objects.equals(formaPagamento, that.formaPagamento);
    }


    @Override
    public int hashCode() {
        return Objects.hash(descricao, valor, data, formaPagamento);
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

    public String getFormaPagamento() {
        return formaPagamento;
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
        this.formaPagamento = formaPagamento;
    }

    public String toString(){
        return String.format("Descrição: %s | Valor: R$%.2f | Data: %s | Forma de pagamento: %s", this.descricao, this.valor, this.data.toString(), this.formaPagamento);
    }
}
