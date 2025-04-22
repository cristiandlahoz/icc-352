# CRUD de Estudiantes - Proyecto REST

Este proyecto implementa un sistema CRUD para la gestión de estudiantes utilizando un servidor REST desarrollado en Java (con Gradle) y un cliente interactivo en Python.

## 📁 Estructura del proyecto

```
.
├── cliente/
│   └── cliente.py         # Cliente interactivo en Python
├── build.gradle
├── settings.gradle
└── src/
    └── main/
        └── java/
            └── ...        # Código fuente del servidor
```

---

## 🚀 Requisitos

- Java 21+
- Gradle
- Python 3

---

## 🔧 Cómo ejecutar el proyecto

### 1️⃣ Iniciar el servidor Java

Abre una **primera terminal** y ejecuta el siguiente comando desde la raíz del proyecto:

```bash
./gradlew run
```

Esto iniciará el servidor REST que expone los endpoints necesarios para la gestión de estudiantes.

---

### 2️⃣ Ejecutar el cliente en Python

Abre una **segunda terminal** y navega a la carpeta `cliente/`:

```bash
cd cliente
python3 cliente.py
```

Esto abrirá un **menú interactivo** desde el cual puedes:

- Crear un nuevo estudiante
- Consultar estudiantes existentes
- Actualizar información de un estudiante
- Eliminar un estudiante

---

## ✅ Notas adicionales

- Asegúrate de tener ambos procesos corriendo al mismo tiempo: el servidor en una terminal y el cliente en la otra.
- El cliente se comunica con el servidor usando HTTP, por lo que es necesario que el servidor esté activo antes de usar el cliente.

---

## 🧑‍💻 Autor

Carolina Bencosme y Cristian de la Hoz

---

## 📜 Licencia

MIT License
