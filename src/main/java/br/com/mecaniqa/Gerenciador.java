package br.com.mecaniqa;

public class Gerenciador {
    Peca[] pecas = new Peca[100];
    Servico[] servicos = new Servico[50];

    private int contadorPecas = 0;
    private int contadorServicos = 0;   

    /* Gerenciamento de Peças */
    public void adicionarPeca(Peca peca) {
        if (contadorPecas < pecas.length) {
            pecas[contadorPecas++] = peca;
        } else {
            System.out.println("Limite de peças atingido.");
        }
    }

    public int buscarPeca(int codigo) {
        for (int i = 0; i < contadorPecas; i++) {
            if (pecas[i].codigo == codigo) {
                return i;
            }
        }
        return -1;
    }

    public void atualizarPeca(int codigo, Peca novaPeca) {
        int index = buscarPeca(codigo);
        if (index != -1) {
            pecas[index] = novaPeca;
            System.out.println("Peça atualizada com sucesso.");
        } else {
            System.out.println("Peça não encontrada.");
        }
    }

    public void removerPeca(int codigo) {
        int index = buscarPeca(codigo);
        if (index != -1) {
            for (int i = index; i < contadorPecas - 1; i++) {
                pecas[i] = pecas[i + 1];
            }
            pecas[--contadorPecas] = null;
            System.out.println("Peça removida com sucesso.");
        } else {
            System.out.println("Peça não encontrada.");
        }
    }

    /* Gerenciamento de Serviços */
    public void adicionarServico(Servico servico) {
        if (contadorServicos < servicos.length) {
            servicos[contadorServicos++] = servico;
        } else {
            System.out.println("Limite de serviços atingido.");
        }
    }

    public int buscarServico(int codigo) {
        for (int i = 0; i < contadorServicos; i++) {
            if (servicos[i].codigo == codigo) {
                return i;
            }
        }
        return -1;
    }

    public void atualizarServico(int codigo, Servico novoServico) {
        int index = buscarServico(codigo);
        if (index != -1) {
            servicos[index] = novoServico;
            System.out.println("Serviço atualizado com sucesso.");
        } else {
            System.out.println("Serviço não encontrado.");
        }
    }

    public void removerServico(int codigo) {
        int index = buscarServico(codigo);
        if (index != -1) {
            for (int i = index; i < contadorServicos - 1; i++) {
                servicos[i] = servicos[i + 1];
            }
            servicos[--contadorServicos] = null;
            System.out.println("Serviço removido com sucesso.");
        } else {
            System.out.println("Serviço não encontrado.");
        }
    }   
}
