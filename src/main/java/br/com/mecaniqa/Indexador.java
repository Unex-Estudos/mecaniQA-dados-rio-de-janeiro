package br.com.mecaniqa;

import java.util.function.Consumer;
import java.util.function.ToIntFunction;

/** Recebe o extrator do identificador; não conhece Cliente, Peça, Serviço ou OS. */
public final class Indexador<T> {
    private No<T> raiz;
    private final ToIntFunction<T> identificador;
    public Indexador(ToIntFunction<T> identificador) {
        this.identificador = java.util.Objects.requireNonNull(identificador);
    }
    public void inserir(T registro) {
        if (registro == null) throw new IllegalArgumentException("Registro nulo");
        raiz = ArvoreAvl.inserir(raiz, identificador.applyAsInt(registro), registro);
    }
    public T buscar(int id) { return ArvoreAvl.buscar(raiz, id); }
    public void emOrdem(Consumer<T> exibir) { ArvoreAvl.exibirEmOrdem(raiz, exibir); }
    public int altura() { return ArvoreAvl.calcularAltura(raiz); }
    public void limpar() { raiz = null; }
}
