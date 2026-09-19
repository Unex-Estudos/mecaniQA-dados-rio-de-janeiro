package br.com.mecaniqa;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private static int proximoCodigo = 1;

    public int codigo;
    public StatusPedido status = StatusPedido.EM_ABERTO;
    public List<ItemPedido> itens = new ArrayList<>();

    public Pedido() {
        codigo = proximoCodigo++;
    }

    public boolean adicionarItem(Peca peca, int quantidade) {
        if (status == StatusPedido.FINALIZADO) {
            System.out.println("Pedido finalizado. Nao e possivel adicionar novas pecas.");
            return false;
        }
        if (quantidade <= 0) {
            System.out.println("A quantidade deve ser maior que zero.");
            return false;
        }

        itens.add(new ItemPedido(peca, quantidade));
        return true;
    }

    public double calcularTotal() {
        double total = 0;
        for (ItemPedido item : itens) {
            total += item.subtotal();
        }
        return total;
    }

    public double calcularTotalComDesconto(double percentualDesconto) {
        return calcularTotal() * (1 - percentualDesconto / 100);
    }

    public void finalizar() {
        status = StatusPedido.FINALIZADO;
    }

    public String toCsv() {
        return codigo + ";" + status + ";" + calcularTotal();
    }
}
