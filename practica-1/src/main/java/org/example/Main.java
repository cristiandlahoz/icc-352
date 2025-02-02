package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Favor de introducir la URL: ");
        String url = sc.nextLine();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String contentType = response.headers().firstValue("Content-Type").orElse("Unknown");

        System.out.println("Tipo de documento: " + contentType);

        if (contentType.contains("text/html")) {
            Document document = Jsoup.parse(response.body());

            System.out.println(
                    "Cantidad de lineas contenidad en el recurso retornado: " + response.body().split("\n").length);

            Elements paragraphs = document.select("p");
            System.out.println("Cantidad de párrafos que contiene el documento:" + paragraphs.size());

            int imgInsideParagraphs = 0;
            for (Element paragraph : paragraphs) {
                imgInsideParagraphs += paragraph.select("img").size();
            }
            System.out.println("Cantidad de imagenes dentro de parrafos: " + imgInsideParagraphs);
            Elements forms = document.select("form");
            for (Element form : forms) {
                String method = form.attr("method").toUpperCase();
                String action = form.attr("action");

                action = validarUrl(url, action);
                System.out.println("Action del formulario tipo: " + method);
                Elements inputs = form.select("input");

                for (Element input : inputs) {
                    String campo = input.attr("name");
                    String tipo = input.attr("type");
                    System.out.println(("Campo del input dentro del formulario: " + campo + " tipo: " + tipo));
                }
                if (method.equals("POST")) {
                    String bodyParam = "asignatura=practica1";
                    HttpRequest postRequest = HttpRequest.newBuilder().uri(URI.create(action))
                            .header("matricula-id", "10149779")
                            .header("Content-type", "application/x-www-form-urlencoded")
                            .POST(HttpRequest.BodyPublishers.ofString(bodyParam)).build();
                    HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
                    System.out.println("Status code del response: " + postResponse.statusCode());
                    System.out.println("Headers:");
                    postResponse.headers().map().forEach((key, value) -> {
                        System.out.println(key + ": " + value);
                    });
                    System.out.println("Body del formulario: \n" + postResponse.body());
                }
            }
        }
        sc.close();
    }

    public static String validarUrl(String url, String action) {
        if (!action.startsWith("http://") && !action.startsWith("https://")) {
            if (url.endsWith("/") && !action.startsWith("/") || !url.endsWith("/") && action.startsWith("/"))
                return action = url + action;
            else if (url.endsWith("/") && action.startsWith("/")) {
                StringBuilder sb = new StringBuilder(action);
                sb.deleteCharAt(0);
                action = url + sb.toString();
                return action;
            } else
                return action = url + "/" + action;
        }
        return action;
    }
}
