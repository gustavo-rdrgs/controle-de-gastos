package br.dcx.ufpb.gustavo.controledegastos;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SistemaGastosMap implements SistemaGastosInterface {
    private HashMap<String, Usuario> usuarios;

    private GravadorDeDados gravador;


    public SistemaGastosMap(){
        this.usuarios = new HashMap<>();
        this.gravador = new GravadorDeDados();
    }


    @Override
    public boolean cadastrarUsuario(Usuario usuario) throws UsuarioJaCadastradoException {
        if (!usuarios.containsKey(usuario.getNome())){
            usuarios.put(usuario.getNome(), usuario);
            return true;
        } else {
            throw new UsuarioJaCadastradoException("Usuario já cadastrado.");
        }
    }

    @Override
    public Usuario buscarUsuario(String nomeUsuario) throws UsuarioNaoEncontradoException {
        if (usuarios.containsKey(nomeUsuario)){
            return usuarios.get(nomeUsuario);
        } else {
            throw new UsuarioNaoEncontradoException("Usuario não encontrado.");
        }
    }

    @Override
    public boolean removerUsuario(String nomeUsuario) throws UsuarioNaoEncontradoException {
        if (usuarios.containsKey(nomeUsuario)){
            usuarios.remove(nomeUsuario);
            return true;
        }
        throw new UsuarioNaoEncontradoException("Usuario não encontrado");
    }

    @Override
    public String listarUsuarios(){
        if (usuarios.isEmpty()){
            return "Nenhum usuario foi adicionado ainda.";
        }

        StringBuilder sb2 = new StringBuilder();
        sb2.append("Usuarios ").append(":\n");
        for (Usuario u : usuarios.values()){
            sb2.append(u.toString()).append("\n");
        }
        return sb2.toString();
    }

    @Override
    public boolean adicionarGasto(String nomeUsuario, GastoPessoal gasto) throws GastoJaAdicionadoException, UsuarioNaoEncontradoException {
        List<GastoPessoal> gastos = gastosDoUsuario(nomeUsuario);
        if (!gastos.contains(gasto)){
            gastos.add(gasto);
            return true;
        }
        throw new GastoJaAdicionadoException("Gasto já adicionado ao usuario "+nomeUsuario);
    }

    @Override
    public String listarGastosUsuario(String nomeUsuario) throws UsuarioNaoEncontradoException {
        List<GastoPessoal> gastos = gastosDoUsuario(nomeUsuario);
        if (gastos.isEmpty()){
            return "Nenhum gasto foi cadastrado ainda para o usuario "+ nomeUsuario;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Gastos do Usuario ").append(nomeUsuario).append(":\n");
        for (GastoPessoal g: gastos){
            sb.append(g.toString()).append("\n");
        }

        return sb.toString();
    }

    @Override
    public GastoPessoal pesquisarGasto(String nomeUsuario, String descricaoGasto) throws UsuarioNaoEncontradoException, GastoNaoEncontradoException {
        List<GastoPessoal> gastos = gastosDoUsuario(nomeUsuario);
        for (GastoPessoal g: gastos){
            if (g.getDescricao().equalsIgnoreCase(descricaoGasto)){
                return g;
            }
        }
        throw new GastoNaoEncontradoException("Gasto não encontrado.");
    }

    @Override
    public boolean removerGasto(String nomeUsuario, String descricaoGasto) throws UsuarioNaoEncontradoException, GastoNaoEncontradoException {
        List<GastoPessoal> gastos = gastosDoUsuario(nomeUsuario);
        GastoPessoal g = pesquisarGasto(nomeUsuario, descricaoGasto);
        gastos.remove(g);
        return true;
    }

    public List<GastoPessoal> gastosDoUsuario(String nomeUsuario) throws UsuarioNaoEncontradoException {
        Usuario u = buscarUsuario(nomeUsuario);
        return u.getGastos();
    }

    public void salvarDados() throws IOException {
        try {
            this.gravador.salvarUsuarios(this.usuarios);
        } catch(IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
    public void recuperarDados() throws IOException {
        try {
            this.usuarios = this.gravador.recuperarUsuarios();
        } catch(IOException ex) {
            System.err.println(ex.getMessage());
            this.usuarios = new HashMap<>();
        }
    }
}
