import br.dcx.ufpb.gustavo.controledegastos.SistemaGastosMap;
import br.dcx.ufpb.gustavo.controledegastos.Usuario;
import br.dcx.ufpb.gustavo.controledegastos.exceptions.UsuarioJaCadastradoException;
import br.dcx.ufpb.gustavo.controledegastos.exceptions.UsuarioNaoEncontradoException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Collection;
import static org.junit.jupiter.api.Assertions.*;

public class TestSistemaControleDeGastos {

    SistemaGastosMap sistema;

    @BeforeEach
    void setUp(){
        this.sistema = new SistemaGastosMap();
    }


    @Test
    public void testRemoverUsuario() throws UsuarioNaoEncontradoException, UsuarioJaCadastradoException {
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
    public void testListarUsuario(){

    }
}
