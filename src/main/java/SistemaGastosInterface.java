public interface SistemaGastosInterface {

    public boolean cadastrarUsuario(Usuario usuario);
    public Usuario buscarUsuario(String nome);

    public boolean adicionarGasto(GastoPessoal gasto);
    public GastoPessoal pesquisarGasto(String descricaoGasto);
    public String listarGastosUsuario(String nomeUsuario);

    public boolean removerUsuario(String nomeUsuario);
    public boolean removerGasto(String descricaoGasto);
}
