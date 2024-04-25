package br.com.conversormoedas;

import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MoneyAPI {
    public static String sendRequestAPI(String moedaOriginal, String moedaConversora) {

        // Cria o Cliente HTTP, que realizará as requisições;
        HttpClient client = HttpClient.newHttpClient();

        // Define a URI da API.
        String URIdaAPI = "https://economia.awesomeapi.com.br/json/last/" + moedaConversora + "-" + moedaOriginal;

        // Cria objeto request e emn seguida anexa a URI do JSON
        HttpRequest request = HttpRequest.newBuilder().uri( URI.create(URIdaAPI) ).build();

        // manda a requisição pelo Cliente HTTP, de forma assíncrona,
        // e retorna o req.body no formato String, em seguida é aplicado o parse do JSON que obtém os dados desejados do objeto.

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body) // retorna o body da resposta
                .thenApply(MoneyAPI::JSONParse) // aplica o método parse ao conteúdo vindo no body
                .join();  // junta tudo e retorna os dados do JSON.
    }

    // Faz o parse do JSON para Object.
    public static String JSONParse(String responseBody) {
        JSONObject dados = new JSONObject(responseBody); // Transforma a resposta do body em Object
        String moedaURL = dados.toString().substring(2, 8); // Faz um substring do Objeto, para pegar o "USDURL"

        JSONObject dadosDaMoedaConversora = dados.getJSONObject(moedaURL); // Seleciona o Objeto USDBRL vindo da API

        return dadosDaMoedaConversora.getString("ask"); //Seleciona o valor da moedaConversora
    }
}
