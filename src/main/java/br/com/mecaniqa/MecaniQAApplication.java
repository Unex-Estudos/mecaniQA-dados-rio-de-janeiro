package br.com.mecaniqa;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

public class MecaniQAApplication {
    private static final Scanner entrada = new Scanner(System.in);
    private static int inteiro(String rotulo) { return Integer.parseInt(texto(rotulo)); }
    private static double decimal(String rotulo) { return Double.parseDouble(texto(rotulo).replace(',', '.')); }
    private static String texto(String rotulo) {
        System.out.print(rotulo + ": ");
        String valor = entrada.nextLine();
        if (valor.contains(";") || valor.contains("\n") || valor.contains("\r"))
            throw new IllegalArgumentException("O formato CSV nao aceita ponto e virgula ou quebra de linha nos campos");
        return valor;
    }
    private static Peca peca(int id) {
        return new Peca(id, texto("Nome"), texto("Fabricante"), decimal("Custo"), decimal("Venda"), inteiro("Estoque"));
    }
    private static Servico servico(int id) {
        return new Servico(id, texto("Descricao"), decimal("Preco"), inteiro("Duracao em minutos"));
    }
    private static void mostrar(Object registro) {
        if (registro == null) { System.out.println("Registro nao encontrado."); return; }
        if (registro instanceof Peca) System.out.println(((Peca) registro).toCsv());
        else if (registro instanceof Servico) System.out.println(((Servico) registro).toCsv());
        else if (registro instanceof Cliente) System.out.println(((Cliente) registro).toCsv());
        else if (registro instanceof OrdemServico) System.out.println(((OrdemServico) registro).toCsv());
    }
    public static void main(String[] args) {
        Path pasta = Path.of(args.length > 0 ? args[0] : "dados");
        Gerenciador g;
        try { g = ImportadorCsv.carregar(pasta); }
        catch (IOException e) {
            System.err.println("Falha ao carregar. CSV preservados: " + e.getMessage());
            return;
        }
        System.out.println("Dados carregados e indexados em arvores AVL.");
        while (true) {
            System.out.println("\n1 Adicionar peca | 2 Catalogo em-ordem | 3 Atualizar peca | 4 Remover peca | 5 Buscar peca");
            System.out.println("6 Adicionar servico | 7 Servicos em-ordem | 8 Atualizar servico | 9 Remover servico | 10 Buscar servico");
            System.out.println("11 Adicionar cliente | 12 Clientes em-ordem | 13 Buscar cliente");
            System.out.println("14 Abrir OS | 15 OS em-ordem | 16 Buscar OS | 17 Adicionar servico a OS | 0 Salvar e sair");
            if (!entrada.hasNextLine()) return;
            try {
                int opcao = inteiro("Opcao");
                switch (opcao) {
                    case 1: g.adicionarPeca(peca(inteiro("ID"))); break;
                    case 2: g.listarPecas(MecaniQAApplication::mostrar); break;
                    case 3: { int id = inteiro("ID"); g.atualizarPeca(id, peca(id)); break; }
                    case 4: g.removerPeca(inteiro("ID")); break;
                    case 5: mostrar(g.buscarPeca(inteiro("ID"))); break;
                    case 6: g.adicionarServico(servico(inteiro("ID"))); break;
                    case 7: g.listarServicos(MecaniQAApplication::mostrar); break;
                    case 8: { int id = inteiro("ID"); g.atualizarServico(id, servico(id)); break; }
                    case 9: g.removerServico(inteiro("ID")); break;
                    case 10: mostrar(g.buscarServico(inteiro("ID"))); break;
                    case 11: g.adicionarCliente(new Cliente(inteiro("ID"), texto("Nome"), texto("WhatsApp"), texto("Email"))); break;
                    case 12: g.listarClientes(MecaniQAApplication::mostrar); break;
                    case 13: mostrar(g.buscarCliente(inteiro("ID"))); break;
                    case 14: { OrdemServico os = new OrdemServico(); g.registrarOrdemServico(os); mostrar(os); break; }
                    case 15: g.listarOrdemServicos(MecaniQAApplication::mostrar); break;
                    case 16: mostrar(g.buscarOrdemServico(inteiro("ID"))); break;
                    case 17: {
                        OrdemServico os = g.buscarOrdemServico(inteiro("ID da OS"));
                        Servico servico = g.buscarServico(inteiro("ID do servico"));
                        if (os == null || servico == null) throw new IllegalArgumentException("OS ou servico inexistente");
                        os.adicionarServico(servico); break;
                    }
                    case 0:
                        ExportadorCsv.exportar(g, pasta);
                        System.out.println("CSV salvos. Encerrado."); return;
                    default: System.out.println("Opcao invalida.");
                }
            } catch (IllegalArgumentException | IOException e) { System.out.println("Erro: " + e.getMessage()); }
        }
    }
}
