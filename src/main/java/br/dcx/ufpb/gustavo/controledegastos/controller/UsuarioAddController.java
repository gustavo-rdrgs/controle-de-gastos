package br.dcx.ufpb.gustavo.controledegastos.controller;

import br.dcx.ufpb.gustavo.controledegastos.SistemaGastosMap;
import br.dcx.ufpb.gustavo.controledegastos.Usuario;
import br.dcx.ufpb.gustavo.controledegastos.exceptions.UsuarioJaCadastradoException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UsuarioAddController implements ActionListener {
    private SistemaGastosMap sistema;
    private JFrame janelaPrincipal;

    public UsuarioAddController(SistemaGastosMap sistema, JFrame janela){
        this.sistema = sistema;
        this.janelaPrincipal = janela;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String nome = JOptionPane.showInputDialog(janelaPrincipal, "Digite o nome do usuario");
        String email = JOptionPane.showInputDialog(janelaPrincipal, "Digite o email do usuario");
        Usuario novoUsuario = new Usuario(nome, email);
        try {
            if (nome.trim().isEmpty() || email.trim().isEmpty()){
                JOptionPane.showMessageDialog(janelaPrincipal, "Não pode haver campos vazios. Tente novamente!");
            } else {
                sistema.cadastrarUsuario(novoUsuario);
                JOptionPane.showMessageDialog(janelaPrincipal, "Novo usuario cadastrado com sucesso");
            }
        } catch (UsuarioJaCadastradoException ex) {
            JOptionPane.showMessageDialog(janelaPrincipal, ex.getMessage());
        }
    }
}
