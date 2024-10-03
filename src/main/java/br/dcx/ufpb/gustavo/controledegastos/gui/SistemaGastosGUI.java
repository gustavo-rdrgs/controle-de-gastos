package br.dcx.ufpb.gustavo.controledegastos.gui;

import br.dcx.ufpb.gustavo.controledegastos.SistemaGastosMap;
import br.dcx.ufpb.gustavo.controledegastos.controller.*;

import javax.swing.*;
import java.awt.*;

public class SistemaGastosGUI extends JFrame {
    JLabel linha1, linha2;
    ImageIcon carteiraImg = new ImageIcon("./imgs/wallet.png");
    SistemaGastosMap sistema = new SistemaGastosMap();
    JMenuBar barraDeMenu = new JMenuBar();


    public ImageIcon imagemCarteiraRedimensionada(){
        Image img = carteiraImg.getImage();
        Image novaImg = (img.getScaledInstance(200, 170, Image.SCALE_SMOOTH));
        return new ImageIcon(novaImg);
    }

    public void definirJanela(){
        setTitle("Controle de Gastos");
        setSize(800, 600);
        setLocation(300, 50);
        setResizable(false);
        setBackground(Color.white);

        linha1 = new JLabel("Meu Sistema de Controle de Gastos", JLabel.CENTER);
        linha1.setForeground(Color.BLACK);
        linha1.setFont(new Font("Serif", Font.BOLD, 24));

        linha2 = new JLabel(imagemCarteiraRedimensionada(), JLabel.CENTER);
        setLayout(new GridLayout(3, 1));
        add(linha1);
        add(linha2);
        add(new JLabel());
    }

    public void definirMenuUsuario(){
        JMenu menuUsuario = new JMenu("Operações de Usuario");
        JMenuItem menuAddUsuario = new JMenuItem("Cadastrar novo Usuario");
        JMenuItem menuRemoveUsuario = new JMenuItem("Remover Usuario");
        JMenuItem menuSearchUsuario = new JMenuItem("Procurar Usuario");
        JMenuItem menuListUsuario = new JMenuItem("Listar Usuarios");
        menuUsuario.add(menuAddUsuario);
        menuUsuario.add(menuRemoveUsuario);
        menuUsuario.add(menuSearchUsuario);
        menuUsuario.add(menuListUsuario);

        menuAddUsuario.addActionListener(new UsuarioAddController(sistema, this));
        menuRemoveUsuario.addActionListener(new UsuarioRemoveController(sistema, this));
        menuSearchUsuario.addActionListener(new UsuarioSearchController(sistema, this));
        menuListUsuario.addActionListener(new UsuarioListController(sistema, this));

        barraDeMenu.add(menuUsuario);
    }

    public void definirMenuGasto(){
        JMenu menuGasto = new JMenu("Operações de Gastos");
        JMenuItem menuAddGasto = new JMenuItem("Adicionar novo Gasto");
        JMenuItem menuRemoveGasto = new JMenuItem("Remover Gasto");
        JMenuItem menuSearchGasto = new JMenuItem("Pesquisar Gasto");
        JMenuItem menuListGastos = new JMenuItem("Listar Gastos de um Usuario");
        menuGasto.add(menuAddGasto);
        menuGasto.add(menuRemoveGasto);
        menuGasto.add(menuSearchGasto);
        menuGasto.add(menuListGastos);

        menuAddGasto.addActionListener(new GastoAddController(sistema, this));
        menuRemoveGasto.addActionListener(new GastoRemoveController(sistema, this));
        menuSearchGasto.addActionListener(new GastoSearchController(sistema, this));
        menuListGastos.addActionListener(new GastosListController(sistema, this));

        barraDeMenu.add(menuGasto);
    }

    public SistemaGastosGUI(){
        definirJanela();
        definirMenuUsuario();
        definirMenuGasto();
        setJMenuBar(barraDeMenu);
    }

    public static void main(String [] args){
        JFrame janela = new SistemaGastosGUI();
        janela.setVisible(true);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
