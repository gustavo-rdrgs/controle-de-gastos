package br.dcx.ufpb.gustavo.controledegastos;

public class Data {
    private int dia;
    private int mes;
    private int ano;

    public Data(int dia, int mes, int ano){
        this.dia = dia;
        this.mes = mes;
        this.ano = ano;
    }

    public Data(){
        this(1, 1, 2001);
    }

    public String toString(){
        return "dia "+ this.dia +" do mês "+ this.mes +" do ano "+ this.ano;
    }
}
