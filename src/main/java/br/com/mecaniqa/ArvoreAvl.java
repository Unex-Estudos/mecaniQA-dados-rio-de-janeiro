package br.com.mecaniqa;

import java.util.function.Consumer;

/** Motor implementado do zero, com operações estáticas no estilo struct. */
public final class ArvoreAvl {
    private ArvoreAvl() { }

    public static int calcularAltura(No<?> no) { return no == null ? 0 : no.altura; }
    public static int fatorBalanceamento(No<?> no) {
        return no == null ? 0 : calcularAltura(no.esquerda) - calcularAltura(no.direita);
    }
    private static void atualizarAltura(No<?> no) {
        no.altura = 1 + Math.max(calcularAltura(no.esquerda), calcularAltura(no.direita));
    }
    public static <T> No<T> rotacaoDireita(No<T> raiz) {
        No<T> novaRaiz = raiz.esquerda;
        raiz.esquerda = novaRaiz.direita;
        novaRaiz.direita = raiz;
        atualizarAltura(raiz);
        atualizarAltura(novaRaiz);
        return novaRaiz;
    }
    public static <T> No<T> rotacaoEsquerda(No<T> raiz) {
        No<T> novaRaiz = raiz.direita;
        raiz.direita = novaRaiz.esquerda;
        novaRaiz.esquerda = raiz;
        atualizarAltura(raiz);
        atualizarAltura(novaRaiz);
        return novaRaiz;
    }
    public static <T> No<T> rotacaoEsquerdaDireita(No<T> raiz) {
        raiz.esquerda = rotacaoEsquerda(raiz.esquerda);
        return rotacaoDireita(raiz);
    }
    public static <T> No<T> rotacaoDireitaEsquerda(No<T> raiz) {
        raiz.direita = rotacaoDireita(raiz.direita);
        return rotacaoEsquerda(raiz);
    }
    public static <T> No<T> inserir(No<T> raiz, int id, T registro) {
        if (registro == null) throw new IllegalArgumentException("Registro nulo");
        if (raiz == null) return new No<>(id, registro);
        if (id < raiz.id) raiz.esquerda = inserir(raiz.esquerda, id, registro);
        else if (id > raiz.id) raiz.direita = inserir(raiz.direita, id, registro);
        else throw new IllegalArgumentException("ID duplicado: " + id);
        atualizarAltura(raiz);
        int fator = fatorBalanceamento(raiz);
        if (fator > 1) return id < raiz.esquerda.id
                ? rotacaoDireita(raiz) : rotacaoEsquerdaDireita(raiz);
        if (fator < -1) return id > raiz.direita.id
                ? rotacaoEsquerda(raiz) : rotacaoDireitaEsquerda(raiz);
        return raiz;
    }
    public static <T> T buscar(No<T> raiz, int id) {
        while (raiz != null) {
            if (id == raiz.id) return raiz.registro;
            raiz = id < raiz.id ? raiz.esquerda : raiz.direita;
        }
        return null;
    }
    public static <T> void exibirEmOrdem(No<T> raiz, Consumer<T> exibir) {
        if (raiz == null) return;
        exibirEmOrdem(raiz.esquerda, exibir);
        exibir.accept(raiz.registro);
        exibirEmOrdem(raiz.direita, exibir);
    }
}
