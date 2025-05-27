# 🧪 Code Review – Sistema de Controle de Gastos

## 📑 Sumário

1. [✅ Boas Práticas Gerais](#-1-boas-práticas-gerais)
2. [🧠 Avaliação dos Controllers](#-2-controllers--avaliação-individual)
3. [🧩 Classe GUI](#-3-classe-gui--sistemagastosgui)
4. [♻️ Refatoração por Classe](#-4-análise-detalhada-e-sugestões-de-refatoração-por-classe)
5. [🎨Design Possível: Singleton](#5)
6. [🤔 Análise da Classe de Testes](#6)
7. [🧼 Sugestões Adicionais de Melhoria](#7)
8. [🎯 Resumo Final](#8)

--- 

## ✅ 1. Boas Práticas Gerais

- Separação de responsabilidades está bem definida inicialmente:
  - `SistemaGastosGUI` cuida da interface.
  - Controllers manipulam eventos.
  - `SistemaGastosMap` centraliza a lógica de domínio (embora com pontos de melhoria, ver seção de refatoração).
- Uso consistente do padrão `ActionListener`.
- Diálogos (`JOptionPane`) usados adequadamente para interações com o usuário.
- Mensagens de erro são exibidas de forma clara (com sugestões de padronização).

---

## 🧠 2. Controllers – Avaliação Individual

### `UsuarioAddController`
-   🔴 **Situação**: Refatoração recomendada.
-   ✔️ Validação de campos vazios implementada.
-   ❗ **Problemas**:
    -   O código mistura lógica de input da UI, validação e criação de objeto num único bloco no `actionPerformed`.
    -   `nome.trim().isEmpty()` e `email.trim().isEmpty()` são verificados *após* a tentativa de construir o objeto `Usuario` via `sistema.usuarioBuilder(nome, email)`. Se `usuarioBuilder` realizar validações ou lançar exceções com base em campos vazios, a lógica atual pode não ser ideal. A validação de campos deveria preceder a chamada ao builder ou ser tratada consistentemente.
-   🛠️ **Sugestão de refatoração mínima**:
    ```java
    String nome = JOptionPane.showInputDialog(janelaPrincipal, "Digite o nome do usuário");
    String email = JOptionPane.showInputDialog(janelaPrincipal, "Digite o email do usuário");

    if (nome == null || nome.trim().isEmpty() || email == null || email.trim().isEmpty()) {
        JOptionPane.showMessageDialog(janelaPrincipal, "Nome e email não podem ser vazios. Tente novamente!", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Considerar validar se o nome/email já existe ANTES de construir o objeto Usuario,
    // se o builder não tratar isso ou se for uma verificação de pré-condição importante.
    // Ex: if (sistema.existeUsuario(nome, email)) { ... }

    try {
        Usuario novoUsuario = sistema.usuarioBuilder(nome, email); // Supondo que o builder pode lançar exceção
        sistema.cadastrarUsuario(novoUsuario);
        JOptionPane.showMessageDialog(janelaPrincipal, "Novo usuário cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    } catch (UsuarioJaCadastradoException ex) {
        JOptionPane.showMessageDialog(janelaPrincipal, ex.getMessage(), "Erro ao Cadastrar", JOptionPane.ERROR_MESSAGE);
    } catch (Exception ex) { // Captura para outros erros inesperados no builder ou cadastro
        JOptionPane.showMessageDialog(janelaPrincipal, "Ocorreu um erro inesperado: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }
    ```

### `UsuarioRemoveController`
-   🟡 **Situação**: Refatoração opcional, mas recomendada.
-   ✔️ Conciso e claro.
-   ❗ **Sugestões**:
    -   Extrair a solicitação do nome do usuário para um método, se essa lógica se repetir ou para maior clareza.
    -   Verificar se o nome retornado é `null` (usuário cancelou o diálogo) ou vazio antes de prosseguir.
        ```java
        String nome = JOptionPane.showInputDialog(janelaPrincipal, "Digite o nome do usuário que deseja remover");
        if (nome != null && !nome.trim().isEmpty()) {
            try {
                sistema.removerUsuario(nome);
                JOptionPane.showMessageDialog(janelaPrincipal, "Usuário removido com sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (UsuarioNaoExisteException ex) {
                JOptionPane.showMessageDialog(janelaPrincipal, ex.getMessage(), "Erro ao Remover", JOptionPane.ERROR_MESSAGE);
            }
        } else if (nome != null) { // string vazia após trim
             JOptionPane.showMessageDialog(janelaPrincipal, "O nome do usuário não pode ser vazio.", "Entrada Inválida", JOptionPane.WARNING_MESSAGE);
        }
        // Se nome for null, o usuário cancelou, nenhuma ação é necessária.
        ```
    -   Padronizar mensagens de erro e sucesso com `JOptionPane` (usando tipos `ERROR_MESSAGE`, `INFORMATION_MESSAGE`).

### `UsuarioSearchController`
-   🟡 **Situação**: Mesma refatoração opcional que `UsuarioRemoveController`.
-   ✔️ Conciso e claro.
-   ❗ **Sugestões**:
    -   Verificar `null`/vazio no input.
    -   Padronizar mensagens de erro e sucesso.
    -   Reutilizar método de input em uma possível classe utilitária (ex: `DialogUtils`).

### `UsuarioListController`
-   🟢 **Situação**: Não precisa de refatoração.
-   ✔️ Simples, legível e direto ao ponto. Cumpre bem sua função.
-   ❗ **Sugestão (menor)**: Padronizar mensagens (se houver alguma além da lista em si) e considerar o que exibir se a lista estiver vazia (atualmente parece exibir "Lista de Usuários:\n").

### `GastoAddController`
-   🔴 **Situação**: Refatoração altamente recomendada.
-   ✔️ Funcional.
-   ❗ **Problemas e Sugestões**:
    1.  **Extração de métodos**: O método `actionPerformed` é muito longo. Dividir em métodos menores para cada input:
        * `String solicitarNomeUsuario()`
        * `String solicitarDescricaoGasto()`
        * `double solicitarValorGasto()`
        * `Data solicitarDataGasto()`
        * `Pagamentos solicitarFormaPagamento()`
    2.  **Validação de forma de pagamento**: Validar a forma de pagamento *antes* de `Pagamentos.valueOf()`.
        ```java
        // Dentro de um método solicitarFormaPagamento()
        String formaDePagamentoStr = JOptionPane.showInputDialog(janelaPrincipal, "Formas de pagamento disponíveis: PIX, BOLETO, CREDITO, DEBITO, ESPECIE\nDigite a forma de pagamento:");
        if (formaDePagamentoStr == null) return null; // Usuário cancelou

        Pagamentos pagamento = null;
        for (Pagamentos p : Pagamentos.values()) {
            if (p.name().equalsIgnoreCase(formaDePagamentoStr.trim())) {
                pagamento = p;
                break;
            }
        }
        if (pagamento == null) {
            JOptionPane.showMessageDialog(janelaPrincipal, "Forma de pagamento inválida.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
            return null; // Ou lançar exceção
        }
        return pagamento;
        ```
    3.  **Validação de entrada com métodos utilitários**: Criar métodos para solicitar e validar tipos específicos (inteiro, double, data).
        Exemplo para data (sugestão compacta):
        ```java
        // Dentro de um método solicitarDataGasto()
        String dataStr = JOptionPane.showInputDialog(janelaPrincipal, "Digite a data do gasto (dd/mm/aaaa):");
        if (dataStr == null) return null; // Usuário cancelou

        try {
            String[] partes = dataStr.split("/");
            if (partes.length != 3) throw new IllegalArgumentException("Formato de data inválido.");
            int dia = Integer.parseInt(partes[0]);
            int mes = Integer.parseInt(partes[1]);
            int ano = Integer.parseInt(partes[2]);
            // Adicionar validação da Data aqui, se a classe Data não o fizer, ou criar Data e deixar ela validar
            return new Data(dia, mes, ano); // Supondo que Data valide seus próprios dados
        } catch (NumberFormatException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(janelaPrincipal, "Data inválida: " + e.getMessage() + ". Use o formato dd/mm/aaaa.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
            return null; // Ou repetir a solicitação
        }
        ```
    4.  **Mensagens como constantes ou enums**: Evitar strings literais repetidas.
    5.  **Tratamento de exceções**: Capturar exceções mais específicas (`NumberFormatException`, `NullPointerException` se não tratadas antes) e ter um `catch (Exception ex)` genérico para erros inesperados.

### `GastoRemoveController`, `GastoSearchController`, `GastosListController`
-   🟡 **Situação**: Funcionais, mas podem ser levemente refatoradas para melhoria.
-   ✔️ **Pontos positivos**: Diretos, tratam exceções esperadas. `GastosListController` é bem enxuto.
-   ❗ **Sugestões de Melhoria (aplicáveis às três onde couber)**:
    -   **Extrair método para solicitar input**:
        ```java
        private String solicitarInput(String mensagem, String titulo) {
            return JOptionPane.showInputDialog(janelaPrincipal, mensagem, titulo, JOptionPane.PLAIN_MESSAGE);
        }
        // Uso: String nome = solicitarInput("Digite o nome do usuário:", "Remover Gasto");
        ```
    -   Validar `null` ou string vazia do input (caso o usuário cancele ou não digite nada).
    -   **`GastoSearchController`**: Separar lógica em etapas nomeadas (solicitar nome, solicitar descrição, pesquisar, exibir).
    -   **`GastosListController`**: Considerar o que exibir se a lista de gastos de um usuário estiver vazia.

---

## 🧩 3. Classe GUI – `SistemaGastosGUI`

-   ✔️ A instância de `SistemaGastosMap` é criada uma única vez e passada corretamente aos controllers.
-   ✔️ O uso de `addWindowListener` para persistência de dados no encerramento é adequado.
-   ❗ **Sugestão de melhoria**:
    -   Separar a recuperação de dados (carregamento inicial) do construtor para um método dedicado (melhora testabilidade e clareza). Ex: `carregarDadosIniciais()`.
    -   Adicionar um fallback visual (ex: um placeholder ou mensagem) caso a imagem do logo não possa ser carregada.
---

## ♻️ 4. Análise Detalhada e Sugestões de Refatoração por Classe

### `SistemaGastosMap.java`
-   ⚠️ **Princípio de Responsabilidade Única (SRP)**: A classe atualmente concentra:
    -   Lógica de negócio (gerenciamento de usuários e gastos).
    -   Entrada de dados e interação com o usuário via `JOptionPane` (em alguns métodos auxiliares, se houver).
    -   Persistência de dados (chamadas ao `GravadorDeDados`).
-   🛠️ **Sugestão de Divisão**:
    -   **`SistemaGastosService` (ou `GerenciadorUsuariosGastos`)**: Conteria as regras de negócio puras (adicionar usuário, buscar gasto, etc.), sem acoplamento com UI ou persistência direta. Receberia dados e retornaria resultados ou exceções de negócio.
    -   **Interação com Usuário (UI Layer)**: Os `Controllers` já fazem parte disso, recebendo dados da GUI e chamando o `Service`. `JOptionPane` para inputs complexos ou feedback direto poderiam ser encapsulados em classes utilitárias de UI ou mantidos nos controllers de forma mais estruturada.
    -   **`RepositorioDeUsuarios` (ou `UsuarioRepository`)**: Interface e implementação para acesso a dados, abstraindo o `GravadorDeDados`. O `Service` usaria este repositório.

### `Usuario.java`
-   ✔️ Representa um modelo de dados.
-   ❗ **Melhorias Possíveis**:
    1.  **Comportamentos Encapsulados**:
        -   `public double calcularTotalDeGastos()`
        -   `public List<Gasto> filtrarGastosPorDescricao(String descricao)`
        -   `public void adicionarGasto(GastoPessoal gasto)`: Encapsula a lógica de manipulação da lista `gastos`, melhora a segurança e clareza.
        -   `public void removerGasto(GastoPessoal gasto)`
    2.  **Método `equals()`**: Atualmente compara a lista de gastos (`gastos`). Isso pode ser problemático se o objetivo for comparar usuários pela identidade (ex: nome/email), independentemente dos seus gastos. Refatorar `equals()` (e `hashCode()`) para usar apenas os campos que definem a identidade de um usuário (geralmente `email` ou um `id` único).
    3.  **Mutabilidade e Thread Safety**: A classe é mutável. Se o sistema for evoluir para um ambiente com múltiplas threads acessando os mesmos objetos `Usuario`, será necessário considerar sincronização ou estratégias de imutabilidade para partes da classe.

### `Gasto.java`
-   ❗ **Melhorias Possíveis**:
    1.  **Validação no Construtor**: Garantir que nenhum objeto `Gasto` inválido seja criado (ex: valor negativo, data nula, descrição vazia), lançando exceções apropriadas (`IllegalArgumentException`).

### `GastoPessoal.java`
-   ❗ **Problemas e Melhorias**:
    1.  **Validação de Valor**: Considerar validar `valor >= 0` no construtor e no setter `setValor(double valor)` para evitar gastos com valor negativo.

### `Data.java`
-   ❗ **Problemas e Melhorias**:
    1.  **Validação de Datas Inválidas**: Falta validação para datas como `Data(32, 13, 2024)`.
        -   🛠️ Adicionar lógica de validação no construtor (ex: lançar `IllegalArgumentException`). Um método `isValid(int dia, int mes, int ano)` estático privado pode auxiliar.
    2.  **Imutabilidade**: Tornar a classe imutável (campos `final`, sem setters públicos, construtor define todos os valores) seria mais seguro e robusto, especialmente para uma classe que representa um valor como data.
    3.  **`equals()` e `hashCode()`**: Essenciais para comparações corretas (ex: em coleções, ou se `GastoPessoal.equals()` depender da igualdade de `Data`). Devem ser implementados baseando-se nos campos `dia`, `mes`, `ano`.

### `Pagamentos` (Enum)
-   ✅ Nenhum problema identificado. Bem implementado para representar um conjunto fixo de opções.

### `SistemaGastosInterface.java`
-   ❗ **Melhorias de Legibilidade**:
    1.  **Comentários Javadoc**: Remover comentários redundantes (`@param`, `@return`, `@throws`) que apenas repetem a assinatura do método sem adicionar informação extra sobre regras de negócio, comportamento esperado em casos específicos, ou pré/pós-condições não óbvias.
    2.  **Clareza nas Descrições**: Simplificar descrições, evitando repetição da palavra "sistema" se o contexto já estiver claro. Focar no "quê" e "porquê" da operação.

### `GravadorDeDados.java`
-   ✔️ Funcional e claro em sua proposta inicial.
-   ❗ **Sugestões de Refatoração para Robustez e Flexibilidade**:
    1.  **Nome do Arquivo Configurável**:
        ```java
        private final String nomeArquivo;

        public GravadorDeDados() {
            this("usuarios.dat"); // Construtor padrão
        }

        public GravadorDeDados(String nomeArquivo) {
            this.nomeArquivo = nomeArquivo;
        }
        // Usar this.nomeArquivo ao invés de uma constante.
        ```
    2.  **Casting Seguro e Validação de Tipo**:
        ```java
        // Dentro de recuperarUsuarios()
        Object obj = entrada.readObject();
        if (obj instanceof HashMap<?, ?>) {
            HashMap<?, ?> mapaLido = (HashMap<?, ?>) obj;
            // Validação adicional para checar se as chaves são String e valores são Usuario
            for (Map.Entry<?, ?> entry : mapaLido.entrySet()) {
                if (!(entry.getKey() instanceof String) || !(entry.getValue() instanceof Usuario)) {
                    throw new IOException("Formato de dados inválido no HashMap: tipos de chave/valor incorretos.");
                }
            }
            @SuppressWarnings("unchecked")
            HashMap<String, Usuario> usuarios = (HashMap<String, Usuario>) mapaLido;
            return usuarios;
        } else if (obj == null && !new File(nomeArquivo).exists()) { // Arquivo não existe ou está vazio inicialmente
             return new HashMap<>(); // Retorna um mapa vazio, comportamento esperado.
        }
        else {
            throw new IOException("Formato de arquivo inválido: esperado HashMap<String, Usuario>, mas encontrado " + (obj != null ? obj.getClass().getName() : "null"));
        }
        ```
    3.  **Separar Responsabilidades (SRP)**: Para maior desacoplamento, considerar:
        -   Uma classe de serialização genérica: `GravadorBinario<T>`.
        -   Uma classe especializada: `PersistenciaUsuarios implements RepositorioDeUsuarios` que usaria `GravadorBinario<Map<String, Usuario>>`.
    4.  **Logging de Erros**: Ao capturar exceções, usar um logger (como `java.util.logging.Logger` ou SLF4J) em vez de `System.err.println` para melhor controle e formatação.
        ```java
        // private static final Logger logger = Logger.getLogger(GravadorDeDados.class.getName());
        // } catch (ClassNotFoundException e) {
        //     logger.log(Level.SEVERE, "Erro ao ler os dados do arquivo: classe não encontrada.", e);
        //     throw new IOException("Erro ao desserializar dados: classe não encontrada.", e);
        // }
        ```
    5.  **Interface Genérica para Persistência (Opcional Avançado)**:
        ```java
        public interface Repositorio<K, V> {
            void salvar(Map<K, V> dados) throws IOException;
            Map<K, V> recuperar() throws IOException;
        }
        // public class RepositorioUsuarios implements Repositorio<String, Usuario>
        ```

### Classes de Exceção (Ex: `UsuarioJaCadastradoException`, `UsuarioNaoExisteException`)
-   ❗ **Melhoria**:
    1.  **`serialVersionUID`**: Adicionar `private static final long serialVersionUID = 1L;` é uma boa prática para classes `Exception` que são `Serializable` (o que todas as exceções são implicitamente), especialmente se o sistema usar serialização para RMI, EJB, ou outras formas de comunicação distribuída, ou persistência de sessões.

---

## 🎨 5. Design Possível: Singleton para `SistemaGastosMap` (ou Service Layer)

**Status atual**: Instância única mantida na GUI e passada aos controllers.
**Alternativa/Reforço**: Se a lógica de `SistemaGastosMap` for movida para uma classe de serviço (ex: `SistemaGastosService`), essa classe de serviço poderia ser um Singleton, garantindo uma única fonte de verdade para a lógica de negócios e estado (se houver estado gerenciado por ela).

```java
public class SistemaGastosService {
    private static SistemaGastosService instance;
    // private final RepositorioDeUsuarios repositorio; // Injetado ou instanciado

    private SistemaGastosService() {
        // this.repositorio = new RepositorioUsuariosImpl(new GravadorDeDados("usuarios.dat"));
        // Carregar dados iniciais aqui se necessário
    }

    public static synchronized SistemaGastosService getInstance() {
        if (instance == null) {
            instance = new SistemaGastosService();
        }
        return instance;
    }
    // Métodos de negócio aqui...
}
```
**Nota**: A injeção de dependência (passar o repositório pelo construtor, por exemplo) é geralmente preferível a Singletons para facilitar testes e flexibilidade, mas um Singleton pode ser uma forma simples de garantir unicidade em aplicações menores. A abordagem atual de instanciar na GUI e passar adiante também é válida e testável.

---
## 🤔 6. Análise da Classe de Testes (`TestSistemaControleDeGastos`)

A classe de testes fornecida (`TestSistemaControleDeGastos`) demonstra um bom começo na garantia da qualidade do sistema.

**✅ Pontos Positivos:**

-   **Estrutura JUnit 5:** Uso correto de anotações (`@Test`, `@BeforeEach`) e asserções.
-   **Isolamento de Teste:** `new SistemaGastosMap()` em `@BeforeEach` garante que cada teste execute em um estado limpo, o que é uma excelente prática.
-   **Nomes Descritivos:** Métodos de teste possuem nomes claros que indicam seu propósito.
-   **Cobertura Básica:** Funções CRUD para usuários e listagens básicas estão sendo testadas.
-   **Teste de Exceção Inicial:** O `testBuscarUsuario` verifica corretamente o lançamento de `UsuarioNaoEncontradoException` para um usuário inexistente.

**⚠️ Pontos de Melhoria e Sugestões:**

1.  **Testando Exceções com `assertThrows`:**
    -   Para maior clareza e concisão ao testar exceções, prefira `assertThrows` do JUnit 5 em vez de blocos `try-catch` com `fail()`.
    -   Exemplo: `assertThrows(UsuarioNaoEncontradoException.class, () -> sistema.buscarUsuario("Inexistente"));`

2.  **Fragilidade dos Testes de Listagem (Asserts em Strings Formatadas):**
    -   Testes como `testListarUsuariosComUsuariosCadastrados` e `testListarGastosDoUsuarios` fazem `assertEquals` em strings formatadas completas. Isso torna os testes muito frágeis a qualquer alteração mínima na formatação da saída (espaços, ordem, etc.).
    -   **Recomendação:**
        -   **Primária:** Modificar os métodos em `SistemaGastosMap` (ou na camada de serviço) para retornarem coleções de objetos (e.g., `List<Usuario>`, `Map<String, List<GastoPessoal>>`). Os testes então validariam o conteúdo dessas coleções (tamanho, presença de elementos específicos). A formatação da string seria responsabilidade da camada de UI/Controller. Isso também se alinha com o Princípio da Responsabilidade Única (SRP).
        -   **Secundária (se a primária não for viável agora):** Em vez de `assertEquals` na string inteira, use `assertTrue(resultado.contains("substring esperada"))` para verificar a presença de partes essenciais da informação, tornando o teste menos sensível a formatação.

3.  **Expandir Cobertura de Cenários de Falha e Casos Limites:**
    -   **Cadastro:** Testar tentativa de cadastrar usuário já existente (esperar `UsuarioJaCadastradoException`). Testar cadastro com dados inválidos (ex: nome/email nulos ou vazios, se houver validação na classe `Usuario` ou `SistemaGastosMap`).
    -   **Remoção:** Testar remoção de usuário inexistente (esperar `UsuarioNaoEncontradoException`).
    -   **Busca:** Testar busca por usuário existente.
    -   **Gastos:**
        -   Testar adição de gasto a usuário inexistente.
        -   Testar listagem de gastos de usuário sem gastos.
        -   Testar remoção/busca de gastos (se essas funcionalidades existirem).
        -   A data "01/01/2001" em `testListarGastosDoUsuarios` deve ser tornada explícita (ex: `new Data(1,1,2001)`) para evitar falhas se `new Data()` usar a data atual.

4.  **Verificação de Estado Pós-Ação:**
    -   Após uma remoção, verificar se o item realmente não existe mais (ex: tentar buscar e esperar exceção, ou verificar se não está na lista).
    -   Após uma adição, buscar o item adicionado e verificar seus atributos.

5.  **Consistência de Dados de Teste:**
    -   O uso de `@BeforeEach` já ajuda muito no isolamento. Certifique-se de que os dados usados (nomes, emails) sejam claros e intencionais para cada cenário de teste.

**Conclusão sobre os Testes:**
A suíte de testes atual é um bom ponto de partida. Implementar as sugestões acima aumentará a robustez, a confiabilidade e a cobertura dos testes, tornando-os menos frágeis a mudanças na formatação e garantindo que mais cenários de erro sejam tratados corretamente pelo sistema. A principal melhoria seria desvincular os testes da formatação exata das strings de saída.

---

## 🧼 7. Sugestões Adicionais de Melhoria


-   [ ] **Isolamento de Dados de Teste**: Ao escrever testes, usar dados únicos para cada teste ou métodos de setup/teardown (`@BeforeEach`, `@AfterEach`) para evitar interferência. Evitar o uso repetido de "User1" sem resetar o estado, como mencionado. Considerar `TestFactory` ou métodos `setupUsuario()` privados nos testes.
-   [ ] **Constantes para Mensagens**: Usar `public static final String` ou enums para mensagens comuns exibidas ao usuário (`"Usuário removido com sucesso"`, mensagens de erro, títulos de diálogo). Isso facilita a manutenção e futuras traduções.
-   [ ] **Utilitário de Diálogos/Alertas**: Centralizar a criação de `JOptionPane` em uma classe utilitária (ex: `AlertaUtils` ou `DialogUtils`) com métodos como `exibirErro(Component pai, String mensagem)`, `exibirInfo(Component pai, String mensagem)`, `confirmarAcao(Component pai, String mensagem)`. Isso padroniza a aparência e o comportamento dos diálogos.

---

## 🎯 8. Resumo Final

| Categoria              | Status                                  | Observações Chave                                                                 |
| :----------------------- | :-------------------------------------- | :-------------------------------------------------------------------------------- |
| Controllers (UI Lógica)  | ⚠️ Requer Atenção / Pode Melhorar     | Alguns controllers longos (`GastoAddController`, `UsuarioAddController`), validações a aprimorar, padronização de diálogos. |
| GUI (`SistemaGastosGUI`) | ✅ Correta                              | Instancia única do sistema bem gerenciada. Pequenas melhorias de UI/robustez sugeridas. |
| Modelo (`Usuario`, `Gasto`, `Data`) | ⚠️ Requer Atenção / Pode Melhorar     | Adicionar validações, `equals/hashCode`, encapsular comportamento, bug em `GastoPessoal`. |
| Persistência (`GravadorDeDados`) | ✅ Funcional / Pode Melhorar          | Sugestões para robustez (casting, nome de arquivo) e SRP.                     |
| Interface (`SistemaGastosInterface`) | 🟡 Opcional Melhoria                  | Limpeza de Javadoc.                                                             |
| Exceções                 | 🟡 Opcional Melhoria                  | Adicionar `serialVersionUID`.                                                   |
| Design Geral           | ⚠️ Pode ser significativamente aprimorado | SRP na `SistemaGastosMap`, introdução de camada de serviço, testes, constantes.   |
| Testabilidade            | ❗ Baixa                                | Necessidade de testes unitários e de integração. Estrutura atual dificulta testes isolados. |

---
