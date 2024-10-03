package br.dcx.ufpb.gustavo.controledegastos.controller;

import br.dcx.ufpb.gustavo.controledegastos.SistemaGastosMap;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class SalvarDadosController implements ActionListener {

    private SistemaGastosMap sistema;
    private JFrame janelaPrincipal;

    public SalvarDadosController(SistemaGastosMap sistema, JFrame janela){
        this.sistema = sistema;
        this.janelaPrincipal = janela;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            sistema.salvarDados();
            JOptionPane.showMessageDialog(janelaPrincipal, "Dados salvos com sucesso!", "Salvar Dados", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(janelaPrincipal, "Dados não puderam ser salvos", "Salvar Dados", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
