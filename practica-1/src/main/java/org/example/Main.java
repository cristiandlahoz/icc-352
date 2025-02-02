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

		if (contentType.contains("text/html")) {
			Document document = Jsoup.parse(response.body());

			System.out.println("Cantidad de lineas contenidad en el recurso retornado: " + response.body().split("\n").length);

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

				System.out.println("Action del formulario tipo: " + method);
				Elements inputs = form.select("input");

				for (Element input : inputs) {
					String campo = input.attr("name");
					String tipo = input.attr("type");
					System.out.println(("Campo del input dentro del formulario: " + campo + " tipo: " + tipo));
				}
				if (method.equals("POST")) {
					HttpRequest postRequest = HttpRequest.newBuilder().uri(URI.create(url)).header("matricula-id", "10149779").POST(HttpRequest.BodyPublishers.ofString("asignatura=practica1")).build();
					HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
					System.out.println("Respuesta a traves del formulario: " + postResponse.body());
				}
			}
		}
		sc.close();
	}
}