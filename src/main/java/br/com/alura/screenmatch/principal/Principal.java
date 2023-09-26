package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private static final String BASE_URL = "https://parallelum.com.br/fipe/api/v1/";

    private final ConsumoAPI consumoAPI = new ConsumoAPI();
    private final ConverteDados converteDados = new ConverteDados();

    private Scanner leitura = new Scanner(System.in);

    public void exibeMenu() {
        var opcaoMenu = """
                Digite uma opção para consulta: (carros/motos/caminhoes):
                """;

        System.out.println(opcaoMenu);
        var opcao = leitura.nextLine();

        if(opcao.toLowerCase().contains("car")) {
            var marcas = getMarcas("carros");
            resultFormatted(marcas);
        } else if(opcao.toLowerCase().contains("mot")) {
            var marcas = getMarcas("motos");
            resultFormatted(marcas);
        }else if(opcao.toLowerCase().contains("cam")) {
            var marcas = getMarcas("caminhoes");
            resultFormatted(marcas);
        }

        var marcasMenu = """
                Informe o código da marca:
                """;

        System.out.println(marcasMenu);
        var marca = leitura.nextLine();

        var modelos = getModelos(opcao, marca);
        resultFormatted(modelos.modelos());

        var modelosMenu = """
                Informe um trecho do nome do carro a ser buscado:
                """;

        System.out.println(modelosMenu);
        var nomeVeiculo = leitura.nextLine();

        List<Dados> modelosFiltrados = modelos.modelos().stream()
                .filter(m-> m.nome().toLowerCase().contains(nomeVeiculo.toLowerCase()))
                .collect(Collectors.toList());

        modelosFiltrados.forEach(System.out::println);


        var anosMenu = """
                Digite o código do modelo:
                """;
        System.out.println(anosMenu);
        var codigoModelo = leitura.nextLine();

         getVeiculos(opcao, marca, codigoModelo);

    }

    private void resultFormatted(List<Dados> marcas) {
        marcas.stream()
                        .sorted(Comparator.comparing(Dados::codigo))
                                .forEach(m -> System.out.println(m.codigo() + " - " + m.nome()));
    }

    private List<Dados> getMarcas(String tipoAutomovel) {
        var json = consumoAPI.obterDados(BASE_URL + tipoAutomovel + "/marcas");
        return converteDados.obterLista(json, Dados.class);
    }

    private Modelos getModelos(String tipoAutomovel, String marca) {
        var json = consumoAPI.obterDados( BASE_URL + tipoAutomovel + "/marcas/" + Integer.parseInt(marca) + "/modelos");
        return converteDados.obterDados(json, Modelos.class);
    }

    private Veiculo getVeiculos(String tipoAutomovel, String marca, String modelo) {
        String endereco = BASE_URL + tipoAutomovel + "/marcas/" + marca + "/modelos/" + modelo + "/anos";
        var jsonAnos = consumoAPI.obterDados(endereco);
        List<Dados> anos =  converteDados.obterLista(jsonAnos, Dados.class);
        List<Veiculo> veiculos = new ArrayList<>();



        for (int i = 0; i < anos.size(); i++) {
            var enderecoAnos = endereco + "/" + anos.get(i).codigo();
            var json = consumoAPI.obterDados(enderecoAnos);
            veiculos.add(converteDados.obterDados(json, Veiculo.class));
        }

        veiculos.forEach(System.out::println);

        return null;
    }

}
