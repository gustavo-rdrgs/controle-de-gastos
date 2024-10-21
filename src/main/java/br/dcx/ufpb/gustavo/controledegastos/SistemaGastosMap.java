package br.dcx.ufpb.gustavo.controledegastos;

import br.dcx.ufpb.gustavo.controledegastos.exceptions.*;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class SistemaGastosMap implements SistemaGastosInterface {
    private HashMap<String, Usuario> usuarios;

    private GravadorDeDados gravador;


    public SistemaGastosMap(){
        this.usuarios = new LinkedHashMap<>();
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

        return usuarios.values()
                .stream()
                .map(Usuario::toString)
                .collect(Collectors.joining("\n"));
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

        List<GastoPessoal> gastosComAMesmaDescricao = new ArrayList<>();
        for(GastoPessoal gastoPessoal : gastos){
            if (gastoPessoal.getDescricao().equalsIgnoreCase(descricaoGasto)){
                gastosComAMesmaDescricao.add(gastoPessoal);
            }
        }

        if (gastosComAMesmaDescricao.size() > 1){
            StringBuilder mensagem = new StringBuilder("Selecione o número do gasto que deseja pesquisar:\n");

            int index = 1;
            for (GastoPessoal gasto : gastosComAMesmaDescricao){
                mensagem.append(index).append(". Descrição: ").append(gasto.getDescricao())
                        .append(". Data: ").append(gasto.getData()).append("\n");
                index++;
            }

            String opcao = JOptionPane.showInputDialog(mensagem.toString()).trim();

            if (!opcao.isEmpty()){
                int escolha = Integer.parseInt(opcao);
                if (escolha >= 1 && escolha <= gastosComAMesmaDescricao.size()) {
                    JOptionPane.showMessageDialog(null,gastosComAMesmaDescricao.get(escolha - 1));
                }
            } else {
                JOptionPane.showMessageDialog(null, "Entrada inválida.");
            }


        } else {
            return gastosDoUsuario(nomeUsuario)
                .stream()
                .filter(g -> g.getDescricao().equalsIgnoreCase(descricaoGasto))
                .findFirst()
                .orElseThrow(() -> new GastoNaoEncontradoException("Gasto não encontrado."));
        }








//        return gastosDoUsuario(nomeUsuario)
//                .stream()
//                .filter(g -> g.getDescricao().equalsIgnoreCase(descricaoGasto))
//                .findFirst()
//                .orElseThrow(() -> new GastoNaoEncontradoException("Gasto não encontrado."));
        return null;
    }

    @Override
    public boolean removerGasto(String nomeUsuario, String descricaoGasto) throws UsuarioNaoEncontradoException, GastoNaoEncontradoException{
        List<GastoPessoal> gastos = gastosDoUsuario(nomeUsuario);


        List<GastoPessoal> gastosComAMesmaDescricao = new ArrayList<>();
        for (GastoPessoal gastoPessoal : gastos){
            if (gastoPessoal.getDescricao().equals(descricaoGasto)){
                gastosComAMesmaDescricao.add(gastoPessoal);
            }
        }


        if (gastosComAMesmaDescricao.size() > 1){
            StringBuilder mensagem = new StringBuilder("Selecione o número do gasto que deseja remover:\n");

            int index = 1;
            for (GastoPessoal gasto : gastosComAMesmaDescricao){
                mensagem.append(index).append(". Descrição: ").append(gasto.getDescricao())
                        .append(". Data: ").append(gasto.getData()).append(", Valor: R$ ").append(gasto.getValor())
                        .append("\n");
                index++;
            }

            String opcao = JOptionPane.showInputDialog(mensagem.toString()).trim();

            if (!opcao.isEmpty()){
                int escolha = Integer.parseInt(opcao);
                if (escolha >= 1 && escolha <= gastosComAMesmaDescricao.size()) {
                    gastos.remove(gastosComAMesmaDescricao.get(escolha - 1));
                    JOptionPane.showMessageDialog(null, "Gasto removido com sucesso!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Entrada inválida.");
            }


        } else {
            GastoPessoal g = pesquisarGasto(nomeUsuario, descricaoGasto);
            gastos.remove(g);
            JOptionPane.showMessageDialog(null, "Gasto removido com sucesso!");
        }
        return true;
    }

    public List<GastoPessoal> gastosDoUsuario(String nomeUsuario) throws UsuarioNaoEncontradoException {
        Usuario u = buscarUsuario(nomeUsuario);
        return u.getGastos();
    }


    //MÉTODOS A PARTE

    public String formatarVirgula(String valorString) {
        return valorString.replace(',', '.');
    }


    public GastoPessoal gastoPessoalBuilder(String descricao, double valor, Data data, Pagamentos pagamentos) {
        return new GastoPessoal(descricao, valor, data, pagamentos);
    }

    public Usuario usuarioBuilder(String nome, String email) {
        return new Usuario(nome, email);
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
