package br.com.alura.screenmatch.principal;


import com.fasterxml.jackson.annotation.JsonAlias;

public record Dados(@JsonAlias("codigo") String codigo,
                    @JsonAlias("nome") String nome) {
}
