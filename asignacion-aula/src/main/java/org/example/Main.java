package org.example;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

public class Main {
	enum Routes {
		LOGIN("/login.html");

		private final String value;

		Routes(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	public static void main(String[] args) {

		var app = Javalin.create(config -> {
			// configurando los documentos estaticos.
			config.staticFiles.add(staticFileConfig -> {
				staticFileConfig.hostedPath = "/";
				staticFileConfig.directory = "/public";
				staticFileConfig.location = Location.CLASSPATH;
				staticFileConfig.precompress = false;
				staticFileConfig.aliasCheck = null;
			});
		}).start(8080);

		app.before(ctx -> {
			if ((ctx.cookie("user") == null || ctx.cookie("password") == null)
					&& !ctx.path().equalsIgnoreCase(Routes.LOGIN.getValue())
					&& !ctx.path().equalsIgnoreCase("/login-cookies")) {
				ctx.redirect(Routes.LOGIN.getValue());
				return;
			}
		});

		app.get("/", ctx -> {
			ctx.redirect("/home.html");
		});
		app.post("/login-cookies", ctx -> {

			String usuario = ctx.formParam("user");
			String contrasena = ctx.formParam("password");
			if (usuario == null || contrasena == null) {
				// errror para procesar la información.
				ctx.redirect(Routes.LOGIN.getValue());
				return;
			}
			// Estamos haciendo fake de un servicio de autenticacion, busque en un servicio.
			ctx.cookie("user", usuario, 120);
			ctx.cookie("password", contrasena, 120);
			// enviando a la vista.
			ctx.redirect("/");
		});
	}
}
