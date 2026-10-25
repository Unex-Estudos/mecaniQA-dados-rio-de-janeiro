package br.com.mecaniqa;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Gerenciador {
    private final List<Peca> pecasInternos = new ArrayList<>();
    public final List<Peca> pecas = java.util.Collections.unmodifiableList(pecasInternos);
    private final List<Servico> servicosInternos = new ArrayList<>();
    public final List<Servico> servicos = java.util.Collections.unmodifiableList(servicosInternos);
    private final List<Cliente> clientesInternos = new ArrayList<>();
    public final List<Cliente> clientes = java.util.Collections.unmodifiableList(clientesInternos);
    public List<Pedido> pedidos = new ArrayList<>();
    private final List<OrdemServico> ordensServicoInternos = new ArrayList<>();
    public final List<OrdemServico> ordensServico = java.util.Collections.unmodifiableList(ordensServicoInternos);
    public Queue<Servico> filaAtendimento = new LinkedList<>();

    private final Indexador<Peca> indicePeca = new Indexador<>(r -> r.codigo);
    public void adicionarPeca(Peca registro) {
        indicePeca.inserir(registro);
        pecasInternos.add(registro);
    }
    public Peca buscarPeca(int codigo) { return indicePeca.buscar(codigo); }
    public void listarPecas(java.util.function.Consumer<Peca> exibir) { indicePeca.emOrdem(exibir); }
    public void atualizarPeca(int codigo, Peca novo) {
        if (novo == null || novo.codigo != codigo) throw new IllegalArgumentException("O ID nao pode mudar");
        Peca atual = buscarPeca(codigo);
        if (atual == null) throw new IllegalArgumentException("ID nao encontrado");
        atual.nome = novo.nome;
        atual.fabricante = novo.fabricante;
        atual.precoCusto = novo.precoCusto;
        atual.precoVenda = novo.precoVenda;
        atual.quantidadeEstoque = novo.quantidadeEstoque;
    }
    public void removerPeca(int codigo) {
        Peca atual = buscarPeca(codigo);
        if (atual == null) throw new IllegalArgumentException("ID nao encontrado");
        for (Pedido pedido : pedidos) for (ItemPedido item : pedido.itens)
            if (item.peca == atual) throw new IllegalArgumentException("Peca vinculada a pedido");
        pecasInternos.remove(atual);
        // Remocao fisica por reconstrucao: O(n log n), fora do caminho de busca.
        indicePeca.limpar();
        for (Peca registro : pecas) indicePeca.inserir(registro);
    }
    private final Indexador<Servico> indiceServico = new Indexador<>(r -> r.codigo);
    public void adicionarServico(Servico registro) {
        indiceServico.inserir(registro);
        servicosInternos.add(registro);
    }
    public Servico buscarServico(int codigo) { return indiceServico.buscar(codigo); }
    public void listarServicos(java.util.function.Consumer<Servico> exibir) { indiceServico.emOrdem(exibir); }
    public void atualizarServico(int codigo, Servico novo) {
        if (novo == null || novo.codigo != codigo) throw new IllegalArgumentException("O ID nao pode mudar");
        Servico atual = buscarServico(codigo);
        if (atual == null) throw new IllegalArgumentException("ID nao encontrado");
        atual.descricao = novo.descricao;
        atual.precoServico = novo.precoServico;
        atual.duracaoEstimada = novo.duracaoEstimada;
    }
    public void removerServico(int codigo) {
        Servico atual = buscarServico(codigo);
        if (atual == null) throw new IllegalArgumentException("ID nao encontrado");
        for (OrdemServico os : ordensServico) if (os.servicos.contains(atual))
            throw new IllegalArgumentException("Servico vinculado a OS");
        servicosInternos.remove(atual);
        // Remocao fisica por reconstrucao: O(n log n), fora do caminho de busca.
        indiceServico.limpar();
        for (Servico registro : servicos) indiceServico.inserir(registro);
    }
    private final Indexador<Cliente> indiceCliente = new Indexador<>(r -> r.codigo);
    public void adicionarCliente(Cliente registro) {
        indiceCliente.inserir(registro);
        clientesInternos.add(registro);
    }
    public Cliente buscarCliente(int codigo) { return indiceCliente.buscar(codigo); }
    public void listarClientes(java.util.function.Consumer<Cliente> exibir) { indiceCliente.emOrdem(exibir); }
    private final Indexador<OrdemServico> indiceOrdemServico = new Indexador<>(r -> r.codigo);
    public void registrarOrdemServico(OrdemServico registro) {
        indiceOrdemServico.inserir(registro);
        ordensServicoInternos.add(registro);
    }
    public OrdemServico buscarOrdemServico(int codigo) { return indiceOrdemServico.buscar(codigo); }
    public void listarOrdemServicos(java.util.function.Consumer<OrdemServico> exibir) { indiceOrdemServico.emOrdem(exibir); }
    public void registrarPedido(Pedido pedido) { pedidos.add(pedido); }

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
                    pecasInternos.set(j, pecas.get(j + 1));
                    pecasInternos.set(j + 1, temporaria);
                }
            }
        }
    }

    public void ordenarServicos(ChaveOrdenacao chave) {
        for (int i = 0; i < servicos.size() - 1; i++) {
            for (int j = 0; j < servicos.size() - i - 1; j++) {
                if (compararServicos(servicos.get(j), servicos.get(j + 1), chave) > 0) {
                    Servico temporario = servicos.get(j);
                    servicosInternos.set(j, servicos.get(j + 1));
                    servicosInternos.set(j + 1, temporario);
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
