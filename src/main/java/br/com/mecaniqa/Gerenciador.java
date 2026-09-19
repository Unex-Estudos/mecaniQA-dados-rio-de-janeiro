package br.com.mecaniqa;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Gerenciador {
    public List<Peca> pecas = new ArrayList<>();
    public List<Servico> servicos = new ArrayList<>();
    public List<Cliente> clientes = new ArrayList<>();
    public List<Pedido> pedidos = new ArrayList<>();
    public List<OrdemServico> ordensServico = new ArrayList<>();
    public Queue<Servico> filaAtendimento = new LinkedList<>();

    public void adicionarPeca(Peca peca) {
        pecas.add(peca);
    }

    public int buscarPeca(int codigo) {
        for (int i = 0; i < pecas.size(); i++) {
            if (pecas.get(i).codigo == codigo) {
                return i;
            }
        }
        return -1;
    }

    public void atualizarPeca(int codigo, Peca novaPeca) {
        int index = buscarPeca(codigo);
        if (index != -1) {
            pecas.set(index, novaPeca);
            System.out.println("Peca atualizada com sucesso.");
        } else {
            System.out.println("Peca nao encontrada.");
        }
    }

    public void removerPeca(int codigo) {
        int index = buscarPeca(codigo);
        if (index != -1) {
            pecas.remove(index);
            System.out.println("Peca removida com sucesso.");
        } else {
            System.out.println("Peca nao encontrada.");
        }
    }

    public void adicionarServico(Servico servico) {
        servicos.add(servico);
    }

    public int buscarServico(int codigo) {
        for (int i = 0; i < servicos.size(); i++) {
            if (servicos.get(i).codigo == codigo) {
                return i;
            }
        }
        return -1;
    }

    public void atualizarServico(int codigo, Servico novoServico) {
        int index = buscarServico(codigo);
        if (index != -1) {
            servicos.set(index, novoServico);
            System.out.println("Servico atualizado com sucesso.");
        } else {
            System.out.println("Servico nao encontrado.");
        }
    }

    public void removerServico(int codigo) {
        int index = buscarServico(codigo);
        if (index != -1) {
            servicos.remove(index);
            System.out.println("Servico removido com sucesso.");
        } else {
            System.out.println("Servico nao encontrado.");
        }
    }

    public void adicionarCliente(Cliente cliente) {
        clientes.add(cliente);
    }

    public void registrarPedido(Pedido pedido) {
        pedidos.add(pedido);
    }

    public void registrarOrdemServico(OrdemServico ordemServico) {
        ordensServico.add(ordemServico);
    }

    public void enviarParaFila(OrdemServico ordemServico) {
        ordemServico.aguardarExecucao(filaAtendimento);
    }

    public Servico executarProximoServico() {
        return filaAtendimento.poll();
    }

    public void ordenarPecas(ChaveOrdenacao chave) {
        for (int i = 0; i < pecas.size() - 1; i++) {
            for (int j = 0; j < pecas.size() - i - 1; j++) {
                if (compararPecas(pecas.get(j), pecas.get(j + 1), chave) > 0) {
                    Peca temporaria = pecas.get(j);
                    pecas.set(j, pecas.get(j + 1));
                    pecas.set(j + 1, temporaria);
                }
            }
        }
    }

    public void ordenarServicos(ChaveOrdenacao chave) {
        for (int i = 0; i < servicos.size() - 1; i++) {
            for (int j = 0; j < servicos.size() - i - 1; j++) {
                if (compararServicos(servicos.get(j), servicos.get(j + 1), chave) > 0) {
                    Servico temporario = servicos.get(j);
                    servicos.set(j, servicos.get(j + 1));
                    servicos.set(j + 1, temporario);
                }
            }
        }
    }

    private int compararPecas(Peca primeira, Peca segunda, ChaveOrdenacao chave) {
        if (chave == ChaveOrdenacao.CODIGO_IDENTIFICADOR) {
            return Integer.compare(primeira.codigo, segunda.codigo);
        }
        return primeira.nome.compareToIgnoreCase(segunda.nome);
    }

    private int compararServicos(Servico primeiro, Servico segundo, ChaveOrdenacao chave) {
        if (chave == ChaveOrdenacao.CODIGO_IDENTIFICADOR) {
            return Integer.compare(primeiro.codigo, segundo.codigo);
        }
        return primeiro.descricao.compareToIgnoreCase(segundo.descricao);
    }
}
