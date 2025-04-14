# CRUD de Estudiantes - Proyecto gRPC

Este proyecto implementa un sistema CRUD para la gestión de estudiantes utilizando **gRPC**. El servidor está desarrollado en Java (Gradle + mínimo Java 21), y el cliente es un script interactivo en Python.

---

## 📁 Estructura del proyecto

```
.
├── cliente/
│   ├── asignacion_aula_7_pb2.py         # Código generado por gRPC a partir del .proto
│   ├── asignacion_aula_7_pb2_grpc.py    # Código generado por gRPC a partir del .proto
│   └── cliente.py                        # Cliente interactivo gRPC en Python
├── protos/
│   └── asignacion_aula_7.proto          # Definición del servicio y mensajes gRPC
├── build.gradle
├── settings.gradle
└── src/
    └── main/
        └── java/
            └── ...                      # Código fuente del servidor
```

---

## 🚀 Requisitos

- Java 21 o superior
- Gradle
- Python 3.10 o superior
- `virtualenv` (opcional pero recomendado)

---

## 🔧 Cómo ejecutar el proyecto

### 1️⃣ Iniciar el servidor gRPC en Java

En una **primera terminal**, desde la raíz del proyecto, ejecuta:

```bash
./gradlew run
```

Esto levantará el servidor gRPC que gestiona las operaciones CRUD de estudiantes.

---

### 2️⃣ Ejecutar el cliente Python

Abre una **segunda terminal** y navega a la carpeta `cliente/`:

```bash
cd cliente
python3 -m venv venv
source venv/bin/activate  # En Windows: venv\Scripts\activate
pip install grpcio grpcio-tools
```

Una vez instaladas las dependencias, ejecuta el cliente:

```bash
python3 cliente.py
```

Este script desplegará un **menú interactivo** para realizar las siguientes operaciones:

- Crear un estudiante
- Listar todos los estudiantes
- Editar un estudiante
- Eliminar un estudiante

---

## ⚙️ Generar nuevamente los archivos `*_pb2.py`

Si realizas cambios en el archivo `.proto`, puedes regenerar los archivos necesarios con:

```bash
python -m grpc_tools.protoc -I../src/main/proto --python_out=. --grpc_python_out=. ../src/main/proto/asignacion-aula-7.proto
```

---

## Notas

- Asegúrate de que el servidor Java esté ejecutándose antes de lanzar el cliente Python.
- El archivo `.proto` define los contratos entre el cliente y el servidor, por lo tanto, debe mantenerse sincronizado en ambos entornos.

---

## 👤 Autor

Cristian de la Hoz y Carolina Bencosme

---

## 📜 Licencia

MIT License
