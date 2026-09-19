package br.com.mecaniqa;

public class ItemPedido {
    public Peca peca;
    public int quantidade;

    public ItemPedido(Peca peca, int quantidade) {
        this.peca = peca;
        this.quantidade = quantidade;
    }

    public double subtotal() {
        return peca.precoVenda * quantidade;
    }

    public String toCsv(int codigoPedido) {
        return codigoPedido + ";" + peca.codigo + ";" + quantidade + ";" + subtotal();
    }
}
