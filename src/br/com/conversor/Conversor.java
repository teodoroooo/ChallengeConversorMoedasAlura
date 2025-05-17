package br.com.conversor;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Conversor {
    private static final String API_KEY = "188b4e5dac3cdcdfb9131651";
    private static final Logger logger = Logger.getLogger(Conversor.class.getName());

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                System.out.println("=== CONVERSOR DE MOEDAS ===");
                System.out.println("Moedas disponíveis:");
                System.out.println("1 - USD (Dólar americano)");
                System.out.println("2 - EUR (Euro)");
                System.out.println("3 - BRL (Real)");
                System.out.println("4 - GBP (Libra esterlina)");
                System.out.println("5 - JPY (Iene japonês)");
                System.out.println("6 - AUD (Dólar australiano)");
                System.out.println("0 - Sair");

                String from = null;
                System.out.println("Escolha a moeda de origem (1-6): ");
                int opcaoOrigem = scanner.nextInt();

                if (opcaoOrigem == 0) {
                    System.out.println("Saindo do programa...");
                    break;
                }

                from = getCodigoMoeda(opcaoOrigem);
                if (from == null) {
                    System.out.println("Opção inválida para moeda de origem.");
                    continue;
                }

                String to = null;
                System.out.println("Escolha a moeda de destino (1-6): ");
                int opcaoDestino = scanner.nextInt();

                to = getCodigoMoeda(opcaoDestino);
                if (to == null) {
                    System.out.println("Opção inválida para moeda de destino.");
                    continue;
                }

                double valor = 0;
                System.out.println("Digite o valor a ser convertido: " + valor);
                valor = scanner.nextDouble();
                if (valor <= 0) {
                    System.out.println("O valor deve ser maior que zero.");
                    continue;
                }

                String urlStr = (
                        "https://v6.exchangerate-api.com/v6/" +
                                API_KEY + "/pair/" + from + "/" + to + "/" + valor
                );

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(urlStr))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    Gson gson = new Gson();
                    ConversorResponse conversao = gson.fromJson(response.body(), ConversorResponse.class);

                    if ("success".equalsIgnoreCase(conversao.result)) {
                        System.out.printf("Resultado: %.2f %s = %.2f %s\n\n",
                                valor, from, conversao.getConversion_result(), to);
                    } else {
                        System.out.println("Erro ao processar conversão. Verifique a chave da API ou parâmetros.");
                    }
                } else {
                    System.out.println("Erro na API. Código HTTP: " + response.statusCode());
                }

            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, insira apenas números.");
                scanner.nextLine(); // limpar buffer do scanner
            } catch (IOException | InterruptedException e) {
                logger.log(Level.SEVERE, "Erro na comunicação com a API");
                System.out.println("Erro ao se comunicar com a API: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Erro inesperado: " + e.getMessage());
            }
        }

        scanner.close();
    }

    private static String getCodigoMoeda(int opcao) {
        return switch (opcao) {
            case 1 -> "USD";
            case 2 -> "EUR";
            case 3 -> "BRL";
            case 4 -> "GBP";
            case 5 -> "JPY";
            case 6 -> "AUD";
            default -> null;
        };
    }
}
