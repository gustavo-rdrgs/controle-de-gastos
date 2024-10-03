package br.dcx.ufpb.gustavo.controledegastos;

import br.dcx.ufpb.gustavo.controledegastos.exceptions.GastoJaAdicionadoException;
import br.dcx.ufpb.gustavo.controledegastos.exceptions.GastoNaoEncontradoException;
import br.dcx.ufpb.gustavo.controledegastos.exceptions.UsuarioJaCadastradoException;
import br.dcx.ufpb.gustavo.controledegastos.exceptions.UsuarioNaoEncontradoException;

public interface SistemaGastosInterface {

    public boolean cadastrarUsuario(Usuario usuario)  throws UsuarioJaCadastradoException;
    public Usuario buscarUsuario(String nomeUsuario)  throws UsuarioNaoEncontradoException;
    public boolean removerUsuario(String nomeUsuario) throws UsuarioNaoEncontradoException;
    public String listarUsuarios();

    public boolean adicionarGasto(String nomeUsuario, GastoPessoal gasto) throws GastoJaAdicionadoException, UsuarioNaoEncontradoException;
    public String listarGastosUsuario(String nomeUsuario)   throws UsuarioNaoEncontradoException;
    public GastoPessoal pesquisarGasto(String nomeUsuario, String descricaoGasto)   throws UsuarioNaoEncontradoException, GastoNaoEncontradoException;
    public boolean removerGasto(String nomeUsuario, String descricaoGasto)   throws UsuarioNaoEncontradoException, GastoNaoEncontradoException;

}
