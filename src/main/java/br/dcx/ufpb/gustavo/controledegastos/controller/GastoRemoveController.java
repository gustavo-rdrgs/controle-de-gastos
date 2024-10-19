package br.dcx.ufpb.gustavo.controledegastos.controller;

import br.dcx.ufpb.gustavo.controledegastos.exceptions.GastoNaoEncontradoException;
import br.dcx.ufpb.gustavo.controledegastos.SistemaGastosMap;
import br.dcx.ufpb.gustavo.controledegastos.exceptions.UsuarioNaoEncontradoException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GastoRemoveController implements ActionListener {
    private SistemaGastosMap sistema;
    private JFrame janelaPrincipal;

    public GastoRemoveController(SistemaGastosMap sistema, JFrame janela){
        this.sistema = sistema;
        this.janelaPrincipal = janela;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String nome = JOptionPane.showInputDialog(janelaPrincipal, "Digite o nome do usuario");
            sistema.buscarUsuario(nome);
            String descricao = JOptionPane.showInputDialog(janelaPrincipal, "Digite o gasto");
            sistema.removerGasto(nome, descricao);
        } catch (UsuarioNaoEncontradoException | GastoNaoEncontradoException ex){
            JOptionPane.showMessageDialog(janelaPrincipal, ex.getMessage());
        }
    }
}
