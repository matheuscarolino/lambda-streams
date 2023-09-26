package br.com.alura.fipe.model.screenmatch.service;

public interface IConverteDados {

    <T> T obterDados(String json, Class<T> classe);
}
