package br.com.mecaniqa;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class ExportadorCsv {
    private ExportadorCsv() {
    }

    public static void exportar(Gerenciador gerenciador, Path pastaDestino) throws IOException {
        Files.createDirectories(pastaDestino);

        escrever(pastaDestino.resolve("clientes.csv"), linhasClientes(gerenciador));
        escrever(pastaDestino.resolve("carros.csv"), linhasCarros(gerenciador));
        escrever(pastaDestino.resolve("pecas.csv"), linhasPecas(gerenciador));
        escrever(pastaDestino.resolve("servicos.csv"), linhasServicos(gerenciador));
        escrever(pastaDestino.resolve("pedidos.csv"), linhasPedidos(gerenciador));
        escrever(pastaDestino.resolve("itens_pedido.csv"), linhasItensPedido(gerenciador));
        escrever(pastaDestino.resolve("ordens_servico.csv"), linhasOrdensServico(gerenciador));
        escrever(pastaDestino.resolve("ordens_servico_servicos.csv"), linhasServicosDasOrdens(gerenciador));
    }

    private static List<String> linhasClientes(Gerenciador gerenciador) {
        List<String> linhas = new ArrayList<>();
        linhas.add("codigo;nome;telefoneWhatsapp;email");
        for (Cliente cliente : gerenciador.clientes) {
            linhas.add(cliente.toCsv());
        }
        return linhas;
    }

    private static List<String> linhasCarros(Gerenciador gerenciador) {
        List<String> linhas = new ArrayList<>();
        linhas.add("codigoCliente;modelo;placa;ano;estilo");
        for (Cliente cliente : gerenciador.clientes) {
            for (Carro carro : cliente.carros) {
                linhas.add(carro.toCsv(cliente.codigo));
            }
        }
        return linhas;
    }

    private static List<String> linhasPecas(Gerenciador gerenciador) {
        List<String> linhas = new ArrayList<>();
        linhas.add("codigo;nome;fabricante;precoCusto;precoVenda;quantidadeEstoque");
        for (Peca peca : gerenciador.pecas) {
            linhas.add(peca.toCsv());
        }
        return linhas;
    }

    private static List<String> linhasServicos(Gerenciador gerenciador) {
        List<String> linhas = new ArrayList<>();
        linhas.add("codigo;descricao;precoServico;duracaoEstimada");
        for (Servico servico : gerenciador.servicos) {
            linhas.add(servico.toCsv());
        }
        return linhas;
    }

    private static List<String> linhasPedidos(Gerenciador gerenciador) {
        List<String> linhas = new ArrayList<>();
        linhas.add("codigo;status;valorTotal");
        for (Pedido pedido : gerenciador.pedidos) {
            linhas.add(pedido.toCsv());
        }
        return linhas;
    }

    private static List<String> linhasItensPedido(Gerenciador gerenciador) {
        List<String> linhas = new ArrayList<>();
        linhas.add("codigoPedido;codigoPeca;quantidade;subtotal");
        for (Pedido pedido : gerenciador.pedidos) {
            for (ItemPedido item : pedido.itens) {
                linhas.add(item.toCsv(pedido.codigo));
            }
        }
        return linhas;
    }

    private static List<String> linhasOrdensServico(Gerenciador gerenciador) {
        List<String> linhas = new ArrayList<>();
        linhas.add("codigo;status;quantidadeServicos;valorTotal");
        for (OrdemServico ordem : gerenciador.ordensServico) {
            linhas.add(ordem.toCsv());
        }
        return linhas;
    }

    private static List<String> linhasServicosDasOrdens(Gerenciador gerenciador) {
        List<String> linhas = new ArrayList<>();
        linhas.add("codigoOrdemServico;codigoServico");
        for (OrdemServico ordem : gerenciador.ordensServico) {
            for (Servico servico : ordem.servicos) {
                linhas.add(ordem.codigo + ";" + servico.codigo);
            }
        }
        return linhas;
    }

    private static void escrever(Path arquivo, List<String> linhas) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(arquivo, StandardCharsets.UTF_8)) {
            for (String linha : linhas) {
                writer.write(linha);
                writer.newLine();
            }
        }
    }
}
