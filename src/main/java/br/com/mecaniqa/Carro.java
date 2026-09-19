package br.com.mecaniqa;

public class Carro {
    public String modelo;
    public String placa;
    public int ano;
    public EstiloCarro estilo;

    public Carro(String modelo, String placa, int ano, EstiloCarro estilo) {
        this.modelo = modelo;
        this.placa = placa;
        this.ano = ano;
        this.estilo = estilo;
    }

}
