package br.com.mecaniqa;

import java.util.ArrayList;
import java.util.List;

public class OrdemServico {
    private static int proximoCodigo = 1;

    public int codigo;
    public StatusOrdemServico status = StatusOrdemServico.EM_ABERTO;
    public List<Servico> servicos = new ArrayList<>();

    public OrdemServico() {
        codigo = proximoCodigo++;
    }

    public boolean adicionarServico(Servico servico) {
        if (status != StatusOrdemServico.EM_ABERTO) {
            System.out.println("OS fechada. Nao e possivel adicionar novos servicos.");
            return false;
        }

        servicos.add(servico);
        return true;
    }

    public boolean removerServico(Servico servico) {
        if (status != StatusOrdemServico.EM_ABERTO) {
            System.out.println("OS fechada. Nao e possivel remover servicos.");
            return false;
        }

        return servicos.remove(servico);
    }

    public int quantidadeServicos() {
        return servicos.size();
    }

    public double valorTotal() {
        double total = 0;
        for (Servico servico : servicos) {
            total += servico.precoServico;
        }
        return total;
    }

    public void finalizar() {
        status = StatusOrdemServico.FINALIZADA;
    }

}
