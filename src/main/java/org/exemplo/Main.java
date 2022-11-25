package org.exemplo;

import org.exemplo.models.Produto;

import java.sql.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Properties;

public class Main {

    static final String DRIVER_BD = "com.mysql.cj.jdbc.Driver";
    static final String URL_BD = "jdbc:mysql://localhost:3306/estoque";
    static final String USUARIO_BD = "root";
    static final String SENHA_BD = "12345";

    /**
     * Registra o driver MySql para conexão com o SGBD.
     * Somente após o registro no método main, podemos
     * estabelecer a conexão com o banco de dados MySQL.
     */
    static void carregarDriverMySql() {

        try {

            Class.forName(DRIVER_BD);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /***
     * Estabelece uma conexão com o banco de dados.
     * @return um objeto {@link Connection} que representa uma conexão com o SGBD
     * @throws SQLException se houver um erro ou falha de conexão
     */
    static Connection criarConexao() throws SQLException {

        Connection conexao = null;
        Properties propriedadesDaConexao = new Properties();

        propriedadesDaConexao.put("user", USUARIO_BD);
        propriedadesDaConexao.put("password", SENHA_BD);

        try {

            conexao = DriverManager.getConnection(URL_BD, propriedadesDaConexao);

        } catch (SQLException e) {

            System.err.println("SQLException: " + e.getMessage());
            System.err.println("SQLState: " + e.getSQLState());
            System.err.println("VendorError: " + e.getErrorCode());

        }
        return conexao;
    }

    /**
     * Adiciona um novo registro no banco de dados
     * @param produto o novo produto que será adicionado
     * @return o id do cliente recém-criado ou -1 em caso de erro
     */
    static long inserirProduto(Produto produto) {

        long idProduto = -1L;
        String query = "INSERT INTO Produto(nome, preco, quantidade) VALUES (?, ?, ?);";

        try (Connection conexao = criarConexao()) {

            PreparedStatement comando = conexao.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            comando.setString(1, produto.getNome());
            comando.setDouble(2, produto.getPreco());
            comando.setInt(3, produto.getQuantidade());

            int linhasAfetadas = comando.executeUpdate();

            if (linhasAfetadas == 1) {

                ResultSet resultado = comando.getGeneratedKeys();

                while (resultado.next())
                    idProduto = resultado.getLong(1);

            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return idProduto;
    }

    /**
     * Apaga um registro no banco de dados
     * @param id chave primária do produto
     * @return true em caso de sucesso ou false em caso de falha
     */
    static boolean excluirProduto(long id) {

        boolean excluido = false;
        String query = "DELETE FROM Produto WHERE id = ?;";

        try (Connection conexao = criarConexao()) {

            PreparedStatement comando = conexao.prepareStatement(query);
            comando.setLong(1, id);

            int linhasAfetadas = comando.executeUpdate();

            if (linhasAfetadas == 1)
                excluido = true;

        } catch (SQLException e) {

            System.err.println(e.getMessage());

        }

        return excluido;
    }

    /**
     * Seleciona um produto do banco de dados através do seu ID
     * @param id chave primária do produto
     * @return um objeto do Produto encontrado, ou null se não encontrado ou em caso de erro
     */
    static Produto buscarProduto(long id) {

        Produto produto = null;

        String query = "SELECT * FROM Produto WHERE id = ?;";

        try (Connection conexao = criarConexao()) {

            PreparedStatement comando = conexao.prepareStatement(query);
            comando.setLong(1, id);

            ResultSet resultado = comando.executeQuery();

            while (resultado.next()) {

                long idProduto = resultado.getLong("id");
                String nomeProduto = resultado.getString("nome");
                double preco = resultado.getDouble("preco");
                int quantidade = resultado.getInt("quantidade");

                produto = new Produto(idProduto, nomeProduto, preco, quantidade);
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return produto;
    }

    /**
     * Obtém todos os produtos
     * @return um objeto do Produto encontrado, ou null se não encontrado ou em caso de erro
     */
    static ArrayList<Produto> buscarTodosProdutos() {

        ArrayList<Produto> produtos = new ArrayList<Produto>();
        String query = "SELECT * FROM Produto;";

        try (Connection conexao = criarConexao()) {

            Statement comando = conexao.createStatement();
            ResultSet resultado = comando.executeQuery(query);

            while (resultado.next()) {

                Produto produto = new Produto();

                long id = resultado.getLong("id");
                String nomeProduto = resultado.getString("nome");
                double preco = resultado.getDouble("preco");
                int quantidade = resultado.getInt("quantidade");

                produtos.add(new Produto(id, nomeProduto, preco, quantidade));

            }


        } catch (SQLException e) {

            System.err.println("SQLException: " + e.getMessage());

        }

        return produtos;
    }

    /**
     * Atualiza um produto no banco de dados
     * @param produto produto com dados atualizados
     * @return true em caso de sucesso ou false em caso de falha ou erro
     */
    static boolean atualizarProduto(Produto produto) {

        boolean produtoAtualizado = false;

        String query = "UPDATE Produto SET nome = ?, preco = ?, quantidade = ? WHERE id = ?;";

        try (Connection conexao = criarConexao()) {

            PreparedStatement comando = conexao.prepareStatement(query);

            comando.setString(1, produto.getNome());
            comando.setDouble(2, produto.getPreco());
            comando.setInt(3, produto.getQuantidade());
            comando.setLong(4, produto.getId());

            int linhasAfetadas = comando.executeUpdate();

            if (linhasAfetadas == 1)
                produtoAtualizado = true;

        } catch (SQLException e) {

            System.err.println(e.getMessage());

        }

        return produtoAtualizado;
    }

    /**
     * Mostra todas as informações de um produto na tela.
     * @param produto o produto que será mostrado na tela.
     */
    static void mostrarProduto(Produto produto) {

        NumberFormat moeda = NumberFormat.getCurrencyInstance();
        String precoFormatado = moeda.format(produto.getPreco());

        System.out.println();
        System.out.format(" Nome: %S%n", produto.getNome());
        System.out.format("Preço: %s%n", precoFormatado);
        System.out.format(" Qtde: %d%n", produto.getQuantidade());
        System.out.println();
    }

    /**
     * Exibe na tela uma tabela com todos os produtos da lista
     * @param produtos a lista de produtos que será exibida na tela.
     */
    static void mostrarTabelaDeProdutos(ArrayList<Produto> produtos) {

        int largura = 112;
        String linhaDivisoria = "—".repeat(largura);

        System.out.format("%s%n", linhaDivisoria);
        System.out.format("%6s\t%-80s\t%5s\t%12s", "Código", "Nome", "Qtde", "Preço");
        System.out.format("%n%s%n", linhaDivisoria);

        for (Produto produto : produtos)
            System.out.println(produto.toString());

        System.out.format("%s%n", linhaDivisoria);

    }

    public static void main(String[] args) {

        carregarDriverMySql();

        // Objetos que vamos usar para testarmos a interação com o banco de dados.
        Produto iphone = new Produto(1, "iPhone 11 Apple 64GB Branco 6,1” 12MP iOS", 3059.10, 250);
        Produto macbook = new Produto(2, "Macbook Air Apple 13.3, Processador M1, 8GB, SSD 256GB, Space Grey - MGN63BZ/A", 6949.99, 2510);
        Produto monitor = new Produto(3, "Monitor Dell de 27\" 4K com Hub USB-C P2723QE", 2899.00, 500);
        Produto bicicleta = new Produto(4, "Bicicleta Aro 29 Shimano MTB Pontal Blitz Alumínio - Cinza", 999.00, 1);

        // ArrayList<Produto> produtos = new ArrayList<Produto>();
        // produtos.add(iphone);
        // produtos.add(macbook);
        // produtos.add(monitor);
        // produtos.add(bicicleta);

        // mostrarProduto(bicicleta);
        // mostrarTabelaDeProdutos(produtos);
        // mostrarProduto(bicicleta);

        /**********************************************************************************
         *                               TESTANDO O CRUD                                  *
         * CRUD são as quatro operações básicas utilizadas em bancos de dados relacionais *
         *              C — Create • R — READ • U — UPDATE • D — DELETE                   *
         **********************************************************************************/

        // CREATE: vamos cadastrar, adicionar, inserir ou salvar um registro no banco de dados.
        // long idIphone = inserirProduto(iphone);
        // long idMacbook = inserirProduto(macbook);
        // long idMonitor = inserirProduto(monitor);
        // long idBicicleta = inserirProduto(bicicleta);


        // READ: vamos Ler, recuperar ou selecionar os dados no banco de dados.
        ArrayList<Produto> produtos = buscarTodosProdutos();
        mostrarTabelaDeProdutos(produtos);
        // Produto p = buscarProduto(1);
        // mostrarProduto(p);


        // UPDATE: vamos atualizar, modificar ou alterar um registro no banco de dados.
        Produto monitorDesatualizado = buscarProduto(10);
        mostrarProduto(monitorDesatualizado);
        monitorDesatualizado.setQuantidade(300);
        monitorDesatualizado.setPreco(2612.72);
        atualizarProduto(monitorDesatualizado);
        mostrarProduto(monitorDesatualizado);

        // DELETE: vamos excluir, remover ou apagar um registro no banco de dados.
        // boolean excluido = excluirProduto(8);
        // System.out.println("Produto Exlcuído? " + excluido);
        // mostrarTabelaDeProdutos(produtos);
        

    } // Fim do método main

} // Fim da classe Main
