package br.com.mecaniqa;

import java.util.ArrayList;
import java.util.List;

public class Cliente {
    public int codigo;
    public String nome;
    public String telefoneWhatsapp;
    public String email;
    public List<Carro> carros = new ArrayList<>();

    public Cliente(int codigo, String nome, String telefoneWhatsapp, String email) {
        this.codigo = codigo;
        this.nome = nome;
        this.telefoneWhatsapp = telefoneWhatsapp;
        this.email = email;
    }

    public void adicionarCarro(Carro carro) {
        carros.add(carro);
    }

    public String toCsv() {
        return codigo + ";" + nome + ";" + telefoneWhatsapp + ";" + email;
    }
}
