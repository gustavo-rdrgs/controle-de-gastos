package br.dcx.ufpb.gustavo.controledegastos;

public interface SistemaGastosInterface {

    public boolean cadastrarUsuario(Usuario usuario)  throws UsuarioJaCadastradoException;
    public Usuario buscarUsuario(String nomeUsuario)  throws UsuarioNaoEncontradoException;
    public boolean removerUsuario(String nomeUsuario) throws UsuarioNaoEncontradoException;

    public boolean adicionarGasto(String nomeUsuario, GastoPessoal gasto) throws GastoJaAdicionadoException, UsuarioNaoEncontradoException;
    public String listarGastosUsuario(String nomeUsuario)   throws UsuarioNaoEncontradoException;
    public GastoPessoal pesquisarGasto(String nomeUsuario, String descricaoGasto)   throws UsuarioNaoEncontradoException, GastoNaoEncontradoException;
    public boolean removerGasto(String nomeUsuario, String descricaoGasto)   throws UsuarioNaoEncontradoException, GastoNaoEncontradoException;
}
