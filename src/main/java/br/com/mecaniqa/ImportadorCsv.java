package br.com.mecaniqa;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/** Carrega em um gerenciador novo; qualquer erro impede a abertura e a sobrescrita dos CSV. */
public final class ImportadorCsv {
    private ImportadorCsv() { }
    @FunctionalInterface private interface Linha { void ler(String[] campos); }
    private static void ler(Path pasta, String nome, int colunas, Linha acao) throws IOException {
        Path arquivo = pasta.resolve(nome);
        if (!Files.exists(arquivo)) return;
        var linhas = Files.readAllLines(arquivo, StandardCharsets.UTF_8);
        for (int i = 1; i < linhas.size(); i++) {
            if (linhas.get(i).isBlank()) continue;
            try {
                String[] campos = linhas.get(i).split(";", -1);
                if (campos.length != colunas) throw new IllegalArgumentException("Quantidade de campos invalida");
                acao.ler(campos);
            } catch (RuntimeException e) {
                throw new IOException(nome + ": linha " + (i + 1) + ": " + e.getMessage(), e);
            }
        }
    }
    private static <T> T exigir(T registro) {
        if (registro == null) throw new IllegalArgumentException("Referencia inexistente no CSV");
        return registro;
    }
    public static Gerenciador carregar(Path pasta) throws IOException {
        Gerenciador g = new Gerenciador();
        ler(pasta, "clientes.csv", 4, c -> g.adicionarCliente(new Cliente(Integer.parseInt(c[0]), c[1], c[2], c[3])));
        ler(pasta, "pecas.csv", 6, c -> g.adicionarPeca(new Peca(Integer.parseInt(c[0]), c[1], c[2], Double.parseDouble(c[3]), Double.parseDouble(c[4]), Integer.parseInt(c[5]))));
        ler(pasta, "servicos.csv", 4, c -> g.adicionarServico(new Servico(Integer.parseInt(c[0]), c[1], Double.parseDouble(c[2]), Integer.parseInt(c[3]))));
        ler(pasta, "carros.csv", 5, c -> exigir(g.buscarCliente(Integer.parseInt(c[0]))).adicionarCarro(new Carro(c[1], c[2], Integer.parseInt(c[3]), EstiloCarro.valueOf(c[4]))));
        Map<Integer, Pedido> pedidos = new HashMap<>();
        ler(pasta, "pedidos.csv", 3, c -> {
            Pedido pedido = new Pedido(Integer.parseInt(c[0]));
            pedido.status = StatusPedido.valueOf(c[1]);
            if (pedidos.putIfAbsent(pedido.codigo, pedido) != null) throw new IllegalArgumentException("Pedido duplicado");
            g.registrarPedido(pedido);
        });
        ler(pasta, "itens_pedido.csv", 4, c -> exigir(pedidos.get(Integer.parseInt(c[0]))).itens.add(new ItemPedido(exigir(g.buscarPeca(Integer.parseInt(c[1]))), Integer.parseInt(c[2]))));
        ler(pasta, "ordens_servico.csv", 4, c -> {
            OrdemServico os = new OrdemServico(Integer.parseInt(c[0]));
            os.status = StatusOrdemServico.valueOf(c[1]);
            g.registrarOrdemServico(os);
        });
        ler(pasta, "ordens_servico_servicos.csv", 2, c -> exigir(g.buscarOrdemServico(Integer.parseInt(c[0]))).servicos.add(exigir(g.buscarServico(Integer.parseInt(c[1])))));
        for (OrdemServico os : g.ordensServico) {
            if (os.status == StatusOrdemServico.AGUARDANDO_EXECUCAO) g.filaAtendimento.addAll(os.servicos);
        }
        return g;
    }
}
