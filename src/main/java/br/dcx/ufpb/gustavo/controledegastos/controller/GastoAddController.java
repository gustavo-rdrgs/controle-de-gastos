package br.dcx.ufpb.gustavo.controledegastos.controller;

import br.dcx.ufpb.gustavo.controledegastos.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GastoAddController implements ActionListener {
    private SistemaGastosMap sistema;
    private JFrame janelaPrincipal;

    public GastoAddController(SistemaGastosMap sistema, JFrame janela){
        this.sistema = sistema;
        this.janelaPrincipal = janela;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String nome = JOptionPane.showInputDialog(janelaPrincipal, "Digite o nome do usuario");
        String descricao = JOptionPane.showInputDialog(janelaPrincipal, "Digite o gasto");
        double valor = Double.parseDouble(JOptionPane.showInputDialog(janelaPrincipal, "Digite o valor do gasto"));

        JOptionPane.showMessageDialog(janelaPrincipal, "Prossiga com a data do gasto");
        int dia = Integer.parseInt(JOptionPane.showInputDialog(janelaPrincipal, "Digite o dia"));
        int mes = Integer.parseInt(JOptionPane.showInputDialog(janelaPrincipal, "Digite o mes"));
        int ano = Integer.parseInt(JOptionPane.showInputDialog(janelaPrincipal, "Digite o ano"));

        String formaDePagamento = JOptionPane.showInputDialog(janelaPrincipal, "Digite a forma de pagamento");
        GastoPessoal gasto = new GastoPessoal(descricao, valor, new Data(dia, mes, ano), formaDePagamento);
        try {
            sistema.adicionarGasto(nome, gasto);
            JOptionPane.showMessageDialog(janelaPrincipal, "Novo gasto cadastrado para o usuario "+nome);
        } catch (GastoJaAdicionadoException | UsuarioNaoEncontradoException ex){
            JOptionPane.showMessageDialog(janelaPrincipal, ex.getMessage());
        }
    }
}
