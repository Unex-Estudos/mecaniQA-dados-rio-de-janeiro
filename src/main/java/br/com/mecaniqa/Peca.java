package br.com.mecaniqa;

public class Peca {
    public int codigo;
    public String nome;
    public String fabricante;
    public double precoCusto;
    public double precoVenda;
    public int quantidadeEstoque;

    public Peca() {
    }

    public Peca(int codigo, String nome, String fabricante,
                double precoCusto, double precoVenda, int quantidadeEstoque) {
        this.codigo = codigo;
        this.nome = nome;
        this.fabricante = fabricante;
        this.precoCusto = precoCusto;
        this.precoVenda = precoVenda;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public String toCsv() {
        return codigo + ";" + nome + ";" + fabricante + ";" + precoCusto + ";"
                + precoVenda + ";" + quantidadeEstoque;
    }
}
