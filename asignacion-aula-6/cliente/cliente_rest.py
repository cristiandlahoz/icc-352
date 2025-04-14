import os
import urllib.request
import urllib.parse
import json

PORT = int(os.getenv("PORT", 8080))
BASE_URL = f"http://localhost:{PORT}/api/estudiante"


def listar_estudiantes():
    try:
        response = urllib.request.urlopen(BASE_URL)
        if response.status == 200:
            estudiantes = json.loads(response.read().decode())
            print("Lista de estudiantes:")
            for est in estudiantes:
                print(f"- {est['nombre']} (matricula: {est['matricula']})")
        else:
            print(f"Error: {response.status}")
    except Exception as e:
        print(f"Error al listar estudiantes: {e}")


def consultar_estudiante():
    matricula = input("Ingrese matricula del estudiante: ")
    try:
        response = urllib.request.urlopen(f"{BASE_URL}/{matricula}")
        if response.status == 200:
            est = json.loads(response.read().decode())
            print(f"Encontrado: {est['nombre']} (matricula: {est['matricula']})")
        else:
            print("Estudiante no encontrado.")
    except Exception as e:
        print(f"Error al consultar estudiante: {e}")


def crear_estudiante():
    nombre = input("Nombre del estudiante: ")
    matricula = int(input("Matricula: "))
    payload = json.dumps({"nombre": nombre, "matricula": matricula}).encode()
    try:
        req = urllib.request.Request(BASE_URL, data=payload, method="POST")
        req.add_header("Content-Type", "application/json")
        response = urllib.request.urlopen(req)
        if response.status == 201 or response.status == 200:
            print("Estudiante creado correctamente.")
        else:
            print(f"Error al crear: {response.status} {response.read().decode()}")
    except Exception as e:
        print(f"Error al crear estudiante: {e}")


def borrar_estudiante():
    matricula = input("Matricula del estudiante a eliminar: ")
    try:
        req = urllib.request.Request(f"{BASE_URL}/{matricula}", method="DELETE")
        response = urllib.request.urlopen(req)
        if response.status == 204 or response.status == 200:
            print("Estudiante eliminado.")
        else:
            print("No se pudo eliminar (¿Existe?).")
    except Exception as e:
        print(f"Error al borrar estudiante: {e}")


def main():
    while True:
        print("\n=== MENU REST Estudiantes ===")
        print("1. Listar todos los estudiantes")
        print("2. Consultar estudiante")
        print("3. Crear nuevo estudiante")
        print("4. Borrar estudiante")
        print("5. Salir")

        opcion = input("Selecciona una opcion: ")

        if opcion == "1":
            listar_estudiantes()
        elif opcion == "2":
            consultar_estudiante()
        elif opcion == "3":
            crear_estudiante()
        elif opcion == "4":
            borrar_estudiante()
        elif opcion == "5":
            print("Cerrando cliente REST...")
            break
        else:
            print("Opcion invalida.")


if __name__ == "__main__":
    main()
