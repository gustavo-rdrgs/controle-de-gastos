package br.dcx.ufpb.gustavo.controledegastos.controller;

import br.dcx.ufpb.gustavo.controledegastos.SistemaGastosMap;
import br.dcx.ufpb.gustavo.controledegastos.gui.SistemaGastosGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UsuarioListController implements ActionListener {
    private SistemaGastosMap sistema;
    private JFrame janelaPrincipal;

    public UsuarioListController(SistemaGastosMap sistema, JFrame janela){
        this.sistema = sistema;
        this.janelaPrincipal = janela;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(janelaPrincipal, sistema.listarUsuarios());
    }
}
