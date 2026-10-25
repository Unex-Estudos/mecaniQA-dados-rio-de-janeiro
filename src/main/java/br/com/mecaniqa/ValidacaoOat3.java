package br.com.mecaniqa;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Testes executaveis com JDK, sem dependencias externas. */
public final class ValidacaoOat3 {
    private static int verificacoes;
    private static void conferir(boolean condicao, String mensagem) {
        verificacoes++;
        if (!condicao) throw new AssertionError(mensagem);
    }
    private static int validar(No<?> n, long min, long max) {
        if (n == null) return 0;
        conferir(n.id > min && n.id < max, "Regra BST");
        int e = validar(n.esquerda, min, n.id), d = validar(n.direita, n.id, max);
        conferir(Math.abs(e-d) <= 1, "Balanceamento AVL");
        conferir(n.altura == 1 + Math.max(e,d), "Altura armazenada");
        return n.altura;
    }
    public static void main(String[] args) throws Exception {
        No<String> no = new No<>(10, "Cliente");
        conferir(no.altura == 1 && no.esquerda == null && no.direita == null, "QA-01");
        for (int[] sequencia : new int[][]{{3,2,1},{1,2,3},{3,1,2},{1,3,2}}) {
            No<Integer> raiz = null;
            for (int id : sequencia) raiz = ArvoreAvl.inserir(raiz, id, id);
            conferir(raiz.id == 2, "Raiz apos rotacao");
            validar(raiz, Long.MIN_VALUE, Long.MAX_VALUE);
        }
        System.out.println("QA-01 e QA-04: no generico e quatro rotacoes OK");
        Indexador<String> indice = new Indexador<>(Integer::parseInt);
        indice.inserir("50"); indice.inserir("30"); indice.inserir("70");
        conferir("30".equals(indice.buscar(30)) && indice.buscar(99) == null, "QA-02");
        System.out.println("QA-02: busca de 30 na arvore 50,30,70 OK");
        indice.limpar();
        for (String id : new String[]{"10","5","20","2"}) indice.inserir(id);
        List<Integer> ordem = new ArrayList<>();
        indice.emOrdem(r -> ordem.add(Integer.parseInt(r)));
        conferir(ordem.equals(List.of(2,5,10,20)), "QA-03");
        System.out.println("QA-03: em-ordem = " + ordem);
        try { indice.inserir("10"); throw new AssertionError("Duplicado aceito"); }
        catch (IllegalArgumentException esperado) { }
        try { indice.inserir(null); throw new AssertionError("Nulo aceito"); }
        catch (IllegalArgumentException esperado) { }
        No<Integer> grande = null;
        for (int i=1; i<=10000; i++) grande = ArvoreAvl.inserir(grande,i,i);
        validar(grande, Long.MIN_VALUE, Long.MAX_VALUE);
        int passos=0; No<Integer> atual=grande;
        while (atual != null) { passos++; if(atual.id==10000)break; atual=10000<atual.id?atual.esquerda:atual.direita; }
        conferir(ArvoreAvl.buscar(grande,10000)==10000 && passos<=grande.altura,"Busca limitada pela altura");
        System.out.println("10.000 IDs crescentes: altura="+grande.altura+", busca ID 10000="+passos+" nos visitados");
        Random random=new Random(71); No<Integer> aleatoria=null;
        for(int id:random.ints(-10000,10000).distinct().limit(2000).toArray()) aleatoria=ArvoreAvl.inserir(aleatoria,id,id);
        validar(aleatoria,Long.MIN_VALUE,Long.MAX_VALUE);
        Gerenciador g=new Gerenciador();
        Peca p=new Peca(10,"Filtro","Teste",10,20,5);
        Servico s=new Servico(20,"Troca",30,10);
        Cliente c=new Cliente(5,"Cliente ficticio","","teste@example.invalid");
        g.adicionarPeca(p);g.adicionarServico(s);g.adicionarCliente(c);
        OrdemServico os=new OrdemServico();os.adicionarServico(s);g.registrarOrdemServico(os);
        Pedido pedido=new Pedido();pedido.adicionarItem(p,2);g.registrarPedido(pedido);
        conferir(g.buscarPeca(10)==p && g.buscarServico(20)==s && g.buscarCliente(5)==c && g.buscarOrdemServico(os.codigo)==os,"Quatro tipos");
        g.atualizarPeca(10,new Peca(10,"Filtro novo","Teste",11,21,4));
        conferir(pedido.itens.get(0).peca.nome.equals("Filtro novo"),"Referencia preservada");
        Peca removivel=new Peca(99,"Extra","Teste",1,2,1);g.adicionarPeca(removivel);g.removerPeca(99);
        conferir(g.buscarPeca(99)==null,"Indice apos remocao");
        try{g.removerPeca(10);throw new AssertionError("Removeu peca referenciada");}catch(IllegalArgumentException esperado){}
        var pasta=Files.createTempDirectory("oat3-");
        ExportadorCsv.exportar(g,pasta);
        Gerenciador restaurado=ImportadorCsv.carregar(pasta);
        conferir(restaurado.buscarPeca(10).nome.equals("Filtro novo"),"CSV peca");
        conferir(restaurado.buscarCliente(5).nome.equals(c.nome),"CSV cliente");
        conferir(restaurado.buscarOrdemServico(os.codigo).servicos.get(0)==restaurado.buscarServico(20),"CSV vinculo OS");
        conferir(restaurado.pedidos.get(0).itens.get(0).peca==restaurado.buscarPeca(10),"CSV vinculo pedido");
        conferir(new OrdemServico().codigo>os.codigo,"Sequencia OS");
        System.out.println("Integracao, atualizacao, remocao e recarga CSV: OK");
        Files.writeString(pasta.resolve("pecas.csv"),"codigo;nome;fabricante;precoCusto;precoVenda;quantidadeEstoque\nlinha invalida\n");
        try{ImportadorCsv.carregar(pasta);throw new AssertionError("CSV invalido aceito");}catch(java.io.IOException esperado){}
        System.out.println("CSV invalido rejeitado: OK");
        System.out.println("PASSOU: "+verificacoes+" verificacoes");
    }
}
