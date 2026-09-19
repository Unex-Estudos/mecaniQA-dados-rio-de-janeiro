package br.com.mecaniqa;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

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

    public boolean aguardarExecucao(Queue<Servico> filaAtendimento) {
        if (status != StatusOrdemServico.EM_ABERTO) {
            System.out.println("Somente uma OS aberta pode entrar na fila.");
            return false;
        }

        status = StatusOrdemServico.AGUARDANDO_EXECUCAO;
        for (Servico servico : servicos) {
            filaAtendimento.add(servico);
        }
        return true;
    }

    public void iniciarExecucao() {
        status = StatusOrdemServico.EM_EXECUCAO;
    }

    public void finalizar() {
        status = StatusOrdemServico.FINALIZADA;
    }

    public String toCsv() {
        return codigo + ";" + status + ";" + quantidadeServicos() + ";" + valorTotal();
    }
}
