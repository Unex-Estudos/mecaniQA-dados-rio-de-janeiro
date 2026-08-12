package br.com.mecaniqa;

public class Gerenciador {
    Peca[] pecas = new Peca[100];
    Servico[] servicos = new Servico[50];

    private int contadorPecas = 0;
    private int contadorServicos = 0;   

    public void adicionarPeca(Peca peca) {
        if (contadorPecas < pecas.length) {
            pecas[contadorPecas++] = peca;
        } else {
            System.out.println("Limite de peças atingido.");
        }
    }

    public void removerPeca(int codigo) {
        for (int i = 0; i < contadorPecas; i++) {
            if (pecas[i].codigo == codigo) {
                for (int j = i; j < contadorPecas - 1; j++) {
                    pecas[j] = pecas[j + 1];
                }
                pecas[--contadorPecas] = null;
                System.out.println("Peça removida com sucesso.");
                return;
            }
        }
        System.out.println("Peça não encontrada.");
    }

    public void adicionarServico(Servico servico) {
        if (contadorServicos < servicos.length) {
            servicos[contadorServicos++] = servico;
        } else {
            System.out.println("Limite de serviços atingido.");
        }
    }

    public void removerServico(int codigo) {
        for (int i = 0; i < contadorServicos; i++) {
            if (servicos[i].codigo == codigo) {
                for (int j = i; j < contadorServicos - 1; j++) {
                    servicos[j] = servicos[j + 1];
                }
                servicos[--contadorServicos] = null;
                System.out.println("Serviço removido com sucesso.");
                return;
            }
        }
        System.out.println("Serviço não encontrado.");
    }
}
