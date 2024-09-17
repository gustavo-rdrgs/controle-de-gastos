package br.dcx.ufpb.gustavo.controledegastos.gui;

import br.dcx.ufpb.gustavo.controledegastos.SistemaGastosMap;

import javax.swing.*;
import java.awt.*;

public class SistemaGastosGUI extends JFrame {
    JLabel linha1;
    SistemaGastosMap sistema = new SistemaGastosMap();
    JMenuBar barraDeMenu = new JMenuBar();

    public SistemaGastosGUI(){
        setTitle("Controle de Gastos");
        setSize(800, 600);
        setLocation(150, 150);
        setResizable(true);
        setBackground(Color.white);
        linha1 = new JLabel("Sistema de Controle de Gastos", JLabel.CENTER);
        linha1.setForeground(Color.gray);
        linha1.setFont(new Font("Serif", Font.BOLD, 24));
        //TODO...
    }
}
