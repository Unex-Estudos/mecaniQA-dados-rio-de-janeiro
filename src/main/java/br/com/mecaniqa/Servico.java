package br.com.mecaniqa;

public class Servico {
    public int codigo;
    public String descricao;
    public double precoServico;
    public int duracaoEstimada;

    public Servico() {
    }

    public Servico(int codigo, String descricao, double precoServico, int duracaoEstimada) {
        this.codigo = codigo;
        this.descricao = descricao;
        this.precoServico = precoServico;
        this.duracaoEstimada = duracaoEstimada;
    }

    public String toCsv() {
        return codigo + ";" + descricao + ";" + precoServico + ";" + duracaoEstimada;
    }
}
