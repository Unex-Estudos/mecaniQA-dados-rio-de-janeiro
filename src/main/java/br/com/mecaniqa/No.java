package br.com.mecaniqa;

/** Nó genérico: a chave é independente do tipo de registro. Folha tem altura 1. */
public class No<T> {
    public int id;
    public T registro;
    public int altura = 1;
    public No<T> esquerda;
    public No<T> direita;

    public No(int id, T registro) {
        if (registro == null) throw new IllegalArgumentException("Registro nulo");
        this.id = id;
        this.registro = registro;
    }
}
