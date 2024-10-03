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
        try {
            String nome = JOptionPane.showInputDialog(janelaPrincipal, "Digite o nome do usuario");
            sistema.buscarUsuario(nome);

            String descricao = JOptionPane.showInputDialog(janelaPrincipal, "Digite a descrição");
            double valor = Double.parseDouble(JOptionPane.showInputDialog(janelaPrincipal, "Digite o valor do gasto"));

            JOptionPane.showMessageDialog(janelaPrincipal, "Prossiga com a data do gasto");
            int dia = Integer.parseInt(JOptionPane.showInputDialog(janelaPrincipal, "Digite o dia (dd)"));
            int mes = Integer.parseInt(JOptionPane.showInputDialog(janelaPrincipal, "Digite o mes (mm)"));
            int ano = Integer.parseInt(JOptionPane.showInputDialog(janelaPrincipal, "Digite o ano (aaaa)"));

            if (dia > 31 || mes > 12){
                JOptionPane.showMessageDialog(janelaPrincipal, "Insira uma data válida.");
            } else{
                boolean formaValida = false;
                String formaDePagamento = JOptionPane.showInputDialog(janelaPrincipal, "Digite a forma de pagamento (Pix, Boleto, Débito, Crédito, Dinheiro)");
                for (Pagamentos p : Pagamentos.values()){
                    if (p.name().equals(formaDePagamento)) {
                        formaValida = true;
                        break;
                    }
                }
                if (!formaValida){
                    JOptionPane.showMessageDialog(janelaPrincipal, "Forma de pagamento inválida.");
                    return;
                }
                GastoPessoal gasto = new GastoPessoal(descricao, valor, new Data(dia, mes, ano), formaDePagamento);

                sistema.adicionarGasto(nome, gasto);
                JOptionPane.showMessageDialog(janelaPrincipal, "Novo gasto cadastrado para o usuario "+nome);
            }
        } catch (GastoJaAdicionadoException | UsuarioNaoEncontradoException ex){
            JOptionPane.showMessageDialog(janelaPrincipal, ex.getMessage());
        }
    }
}
