package br.dcx.ufpb.gustavo.controledegastos.controller;

import br.dcx.ufpb.gustavo.controledegastos.GastoNaoEncontradoException;
import br.dcx.ufpb.gustavo.controledegastos.GastoPessoal;
import br.dcx.ufpb.gustavo.controledegastos.SistemaGastosMap;
import br.dcx.ufpb.gustavo.controledegastos.UsuarioNaoEncontradoException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GastoSearchController implements ActionListener {
    private SistemaGastosMap sistema;
    private JFrame janelaPrincipal;

    public GastoSearchController(SistemaGastosMap sistema, JFrame janela){
        this.sistema = sistema;
        this.janelaPrincipal = janela;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String nome = JOptionPane.showInputDialog(janelaPrincipal, "Digite o nome do usuario");
            sistema.buscarUsuario(nome);
            String descricao = JOptionPane.showInputDialog(janelaPrincipal, "Digite o gasto");
            GastoPessoal gasto = sistema.pesquisarGasto(nome, descricao);
            JOptionPane.showMessageDialog(janelaPrincipal, gasto.toString());
        } catch (UsuarioNaoEncontradoException | GastoNaoEncontradoException ex){
            JOptionPane.showMessageDialog(janelaPrincipal, ex.getMessage());
        }
    }
}
