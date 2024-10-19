package br.dcx.ufpb.gustavo.controledegastos;

import br.dcx.ufpb.gustavo.controledegastos.exceptions.*;

import java.io.IOException;


public interface SistemaGastosInterface {


    /**
     * Cadastra um novo usuário no sistema
     * @param usuario
     * @return true ou false caso o usuário tenha sido cadastrado
     * @throws UsuarioJaCadastradoException
     */
    public boolean cadastrarUsuario(Usuario usuario)  throws UsuarioJaCadastradoException;

    /**
     * Busca um usuário no sistema
     * @param nomeUsuario
     * @return Usuario encontrado
     * @throws UsuarioNaoEncontradoException
     */
    public Usuario buscarUsuario(String nomeUsuario)  throws UsuarioNaoEncontradoException;

    /**
     * Remove um usuário do sistema
     * @param nomeUsuario
     * @return true ou false caso o usuário tenha sido removido
     * @throws UsuarioNaoEncontradoException
     */
    public boolean removerUsuario(String nomeUsuario) throws UsuarioNaoEncontradoException;

    /**
     * Lista todos os usuários cadastrados no sistema
     * @return ToStrings dos usuários
     */
    public String listarUsuarios();

    /**
     * Adiciona um gasto para um usuário
     * @param nomeUsuario
     * @param gasto
     * @return true ou false caso o gasto tenha sido adicionado
     * @throws GastoJaAdicionadoException
     * @throws UsuarioNaoEncontradoException
     */

    public boolean adicionarGasto(String nomeUsuario, GastoPessoal gasto) throws GastoJaAdicionadoException, UsuarioNaoEncontradoException;

    /**
     * Lista todos os gastos de um usuário
     * @param nomeUsuario
     * @return ToStrings dos gastos
     * @throws UsuarioNaoEncontradoException
     */

    public String listarGastosUsuario(String nomeUsuario)   throws UsuarioNaoEncontradoException;

    /**
     * Pesquisa um gasto de um usuário
     * @param nomeUsuario
     * @param descricaoGasto
     * @return Gasto encontrado
     * @throws UsuarioNaoEncontradoException
     * @throws GastoNaoEncontradoException
     */
    public GastoPessoal pesquisarGasto(String nomeUsuario, String descricaoGasto)   throws UsuarioNaoEncontradoException, GastoNaoEncontradoException;

    /**
     * Remove um gasto de um usuário
     * @param nomeUsuario
     * @param descricaoGasto
     * @return true ou false caso o gasto tenha sido removido
     * @throws UsuarioNaoEncontradoException
     * @throws GastoNaoEncontradoException
     */

    public boolean removerGasto(String nomeUsuario, String descricaoGasto)   throws UsuarioNaoEncontradoException, GastoNaoEncontradoException;

    /**
     * Salva os dados do sistema
     * @throws IOException
     */
    public void salvarDados() throws IOException;

    /**
     * Recupera os dados do sistema
     * @throws IOException
     */
    public void recuperarDados() throws IOException;
}
