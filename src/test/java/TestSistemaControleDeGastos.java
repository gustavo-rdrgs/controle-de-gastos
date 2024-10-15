import br.dcx.ufpb.gustavo.controledegastos.*;
import br.dcx.ufpb.gustavo.controledegastos.exceptions.GastoJaAdicionadoException;
import br.dcx.ufpb.gustavo.controledegastos.exceptions.UsuarioJaCadastradoException;
import br.dcx.ufpb.gustavo.controledegastos.exceptions.UsuarioNaoEncontradoException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Collection;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class TestSistemaControleDeGastos {

    SistemaGastosMap sistema;

    @BeforeEach
    void setUp(){
        this.sistema = new SistemaGastosMap();
    }


    @Test
    public void testRemoverECadastrarUsuario() throws UsuarioNaoEncontradoException, UsuarioJaCadastradoException {
        Usuario u1 = new Usuario("Kaua", "kaua.vidal@dcx.ufpb.br");


        try {
            boolean cadastrou = sistema.cadastrarUsuario(u1);
            assertTrue(cadastrou);
        } catch (UsuarioJaCadastradoException e){
            fail("Não deveria falhar");
        }


        try {
            boolean removeu = sistema.removerUsuario("Kaua");
            assertTrue(removeu);
        } catch (UsuarioNaoEncontradoException e){
            fail ("Não deveria lançar exceção");
        }
    }

    @Test
    public void testBuscarUsuario() throws UsuarioNaoEncontradoException{
        try {
            sistema.buscarUsuario("Kaua");
            fail("Deveria falhar pois não existe nenhum usuario ainda");
        } catch (UsuarioNaoEncontradoException e){
           //OK
        }
    }

    @Test
    public void testListarUsuariosComNenhumUsuario() {
        String resultado = sistema.listarUsuarios();
        assertEquals("Nenhum usuario foi adicionado ainda.", resultado);
    }

    @Test
    public void testListarUsuariosComUsuarios() {
        try {
            // Cadastro de usuários
            sistema.cadastrarUsuario(new Usuario("User1", "user1@example.com"));
            sistema.cadastrarUsuario(new Usuario("User2", "user2@example.com"));

            // Chamando o método listarUsuarios
            String resultado = sistema.listarUsuarios();

            // Resultado esperado
            String esperado =
                    "Usuário: User1 | Email: user1@example.com | Total de gastos: R$0,00\n" +
                    "Usuário: User2 | Email: user2@example.com | Total de gastos: R$0,00";

            // Verificação
            assertEquals(esperado, resultado);
        } catch (UsuarioJaCadastradoException e) {
            fail("Não deveria lançar exceção: " + e.getMessage());
        }
    }

    @Test
    public void testListarGastosDoUsuarios() {
        try {
            sistema.cadastrarUsuario(new Usuario("User1", "user1@example.com"));
            // Cadastro de usuários
            sistema.adicionarGasto("User1",new GastoPessoal("Gasto1", 0.00, new Data(), Pagamentos.BOLETO));

            // Chamando o método listarUsuarios
            String resultado = sistema.listarGastosUsuario("User1");

            // Resultado esperado
            String esperado =
                    "Gastos do Usuario User1:\n" +
                    "Descrição: Gasto1 | Valor: R$0,00 | Data: 01/01/2001 | Forma de pagamento: BOLETO\n";

            // Verificação
            assertEquals(esperado, resultado);
        } catch (GastoJaAdicionadoException | UsuarioNaoEncontradoException | UsuarioJaCadastradoException e) {
            fail("Não deveria lançar exceção: " + e.getMessage());
        }
    }

}
