package org.exemplo.models;

import java.text.NumberFormat;

/***
 * Representa um Produto vendido por uma loja qualquer
 * @author Prof. Diego Augusto Barros
 * @version %I%, %G%
 */
public class Produto {

    private long id;

    private String nome;
    private double preco;
    private int quantidade;

    /**
     * Inicializa um novo Produto
     */
    public Produto() {
        id = 0L;
        nome = "";
        preco = 0.0;
        quantidade = 0;
    }

    /**
     * Inicializa um novo Produto
     * @param nome O nome do produto.
     * @param preco O preço em reais (R$) do produto.
     * @param quantidade A quantidade em estoque do produto.
     */
    public Produto(String nome, double preco, int quantidade) {
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    /***
     * Inicializa um novo Produto
     * @param id O identificador único do produto
     * @param nome O nome do produto.
     * @param preco O preço em reais (R$) do produto.
     * @param quantidade A quantidade em estoque do produto.
     */
    public Produto(long id, String nome, double preco, int quantidade) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    /**
     * Obtém o ID do produto
     * @return a chave primária do produto
     */
    public long getId() {
        return id;
    }

    /**
     * Obtém o nome do produto
     * @return Uma @{@link String} representando o nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * Atualiza o nome do produto.
     * @param nome O nome do produto
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Obtém o nome do produto
     * @return O preço do produto em reais (R$)
     */
    public double getPreco() {
        return preco;
    }

    /**
     * Atualiza o preço do produto.
     * @param preco O preço do produto
     * @throws IllegalArgumentException Se o preço do produto for menor do que zero.
     */
    public void setPreco(double preco) throws IllegalArgumentException {

        if (preco < 0)
            throw new IllegalArgumentException("O preço do produto não pode ser negativo.");

        this.preco = preco;
    }

    /**
     * Obtém a quantidade em estoque do produto
     * @return A quantidade em estoque do produto
     */
    public int getQuantidade() {
        return quantidade;
    }

    /**
     * Atualiza a quantidade em estoque do produto.
     * @param quantidade A nova quantidade em estoque do produto.
     * @throws IllegalArgumentException Se a quantidade em estoque for menor do que zero.
     */
    public void setQuantidade(int quantidade) throws IllegalArgumentException {

        if (quantidade < 0)
            throw new IllegalArgumentException("A quantidade em estoque não pode ser negativa.");

        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        NumberFormat moeda = NumberFormat.getCurrencyInstance();
        String precoFormatado = moeda.format(preco);
        return String.format("%06d\t%-80S\t%5d\t%12s", id, nome, quantidade, precoFormatado);
    }
}
