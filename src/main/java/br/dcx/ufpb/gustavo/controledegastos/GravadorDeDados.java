package br.dcx.ufpb.gustavo.controledegastos;

import java.io.*;
import java.util.HashMap;


public class GravadorDeDados {

    public static final String ARQUIVO = "usuarios.dat";

    public HashMap<String, Usuario> recuperarUsuarios() throws IOException {
        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(ARQUIVO))) {
            return (HashMap<String, Usuario>) in.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        }
    }

    public void salvarUsuarios(HashMap<String, Usuario> usuarios) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ARQUIVO))) {
            out.writeObject(usuarios);
        }
    }

}
