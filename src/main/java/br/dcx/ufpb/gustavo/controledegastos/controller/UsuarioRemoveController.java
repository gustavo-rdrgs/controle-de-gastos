package br.dcx.ufpb.gustavo.controledegastos.controller;

import br.dcx.ufpb.gustavo.controledegastos.SistemaGastosMap;
import br.dcx.ufpb.gustavo.controledegastos.UsuarioNaoEncontradoException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UsuarioRemoveController implements ActionListener {
    private SistemaGastosMap sistema;
    private JFrame janelaPrincipal;

    public UsuarioRemoveController(SistemaGastosMap sistema, JFrame janela) {
        this.sistema = sistema;
        this.janelaPrincipal = janela;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String nome = JOptionPane.showInputDialog(janelaPrincipal, "Digite o nome do usuario que deseja remover");
        try {
            sistema.removerUsuario(nome);
        } catch (UsuarioNaoEncontradoException ex){
            JOptionPane.showMessageDialog(janelaPrincipal, ex.getMessage());
        }
    }
}
