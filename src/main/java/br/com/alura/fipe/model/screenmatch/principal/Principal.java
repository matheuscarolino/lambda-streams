package br.com.alura.fipe.model.screenmatch.principal;

import br.com.alura.fipe.model.screenmatch.model.DadosEpisodio;
import br.com.alura.fipe.model.screenmatch.model.DadosSerie;
import br.com.alura.fipe.model.screenmatch.model.DadosTemporada;
import br.com.alura.fipe.model.screenmatch.model.Episodio;
import br.com.alura.fipe.model.screenmatch.service.ConsumoAPI;
import br.com.alura.fipe.model.screenmatch.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private final String NOME_SERIE = "euphoria";
    private static final String URL_BASE = "http://www.omdbapi.com/?t=%s&apikey=6585022c";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();

    public DadosSerie obterDadosSerie() {
        var json = consumoAPI.obterDados(String.format(URL_BASE, getNomeSerieFormatted(NOME_SERIE)));
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);
        return dados;

    }

    public void obterDadosEpisodio() {
        var json = consumoAPI.obterDados(String.format(URL_BASE, getNomeSerieFormatted(NOME_SERIE) + "&season=1&episode=2"));
        DadosEpisodio episodio = conversor.obterDados(json, DadosEpisodio.class);
        System.out.println(episodio);
    }

    public void obterDadosEpisodio(Integer totalTemporadas) {
        List<DadosTemporada> temporadas = new ArrayList<>();
        var json = "";
        for (int i =1; i<= totalTemporadas; i++) {
            json = consumoAPI.obterDados(String.format(URL_BASE, getNomeSerieFormatted(NOME_SERIE) + "&season=" + i));
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }
//        temporadas.forEach(System.out::println);
//
//        temporadas.forEach(t ->
//                t.episodios().forEach(e ->
//                        System.out.println(e.titulo())
//                )
//        );

        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        System.out.println(" \n TOp 5 episódios");
        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equals("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t->t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d)))
                .collect(Collectors.toList());

        episodios.forEach(System.out::println);

        var ano = 2020;
        System.out.println("Episodios a partir de : " + ano);

//        LocalDate dataBusca = LocalDate.of(ano,1,1);
//
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
//        episodios.stream()
//                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
//                .forEach(e -> {
//                    System.out.println(
//                            "Temporada" + e.getTemporada() +
//                                    "Episódio" + e.getNumeroEpisodio() +
//                                    "Data Lançamento" + e.getDataLancamento().format(dateTimeFormatter)
//                    );
//                });

        Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0)
                        .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));

        System.out.println(est);
    }


    private static String getNomeSerieFormatted(String nomeSerie) {
        return nomeSerie.replace(" ", "+");
    }
}
