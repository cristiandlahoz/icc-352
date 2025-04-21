import os
import urllib.request
import urllib.parse
import json

PORT = int(os.getenv("PORT", 8080))
BASE_URL = f"http://localhost:{PORT}/api/estudiante"
GREEN = "\033[32m"
RED = "\033[31m"
YELLOW = "\033[33m"
BLUE = "\033[34m"
RESET = "\033[0m"


def listar_estudiantes():
    try:
        response = urllib.request.urlopen(BASE_URL)
        if response.status == 200:
            estudiantes = json.loads(response.read().decode())
            print(f"{BLUE}Lista de estudiantes:{RESET}")
            for est in estudiantes:
                print(
                    f"{GREEN}- {est['nombre']} (matricula: {est['matricula']}){RESET}"
                )
        else:
            print(f"{RED}Error: {response.status}{RESET}")
    except Exception as e:
        print(f"{RED}Error al listar estudiantes: {e}{RESET}")


def consultar_estudiante():
    matricula = input("Ingrese matricula del estudiante: ")
    try:
        response = urllib.request.urlopen(f"{BASE_URL}/{matricula}")
        if response.status == 200:
            est = json.loads(response.read().decode())
            print(
                f"{GREEN}Encontrado: {est['nombre']} (matricula: {est['matricula']}){
                    RESET
                }"
            )
        else:
            print(f"{YELLOW}Estudiante no encontrado.{RESET}")
    except Exception as e:
        print(f"{RED}Error al consultar estudiante: {e}{RESET}")


def crear_estudiante():
    nombre = input("Nombre del estudiante: ")
    matricula = int(input("Matricula: "))
    payload = json.dumps({"nombre": nombre, "matricula": matricula}).encode()
    try:
        req = urllib.request.Request(BASE_URL, data=payload, method="POST")
        req.add_header("Content-Type", "application/json")
        response = urllib.request.urlopen(req)
        if response.status == 201 or response.status == 200:
            print(f"{GREEN}Estudiante creado correctamente.{RESET}")
        else:
            print(
                f"{RED}Error al crear: {response.status} {response.read().decode()}{
                    RESET
                }"
            )
    except Exception as e:
        print(f"{RED}Error al crear estudiante: {e}{RESET}")


def borrar_estudiante():
    matricula = input("Matricula del estudiante a eliminar: ")
    try:
        req = urllib.request.Request(f"{BASE_URL}/{matricula}", method="DELETE")
        response = urllib.request.urlopen(req)
        if response.status == 204 or response.status == 200:
            print(f"{GREEN}Estudiante eliminado.{RESET}")
        else:
            print(f"{YELLOW}No se pudo eliminar (¿Existe?).{RESET}")
    except Exception as e:
        print(f"{RED}Error al borrar estudiante: {e}{RESET}")


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
            print(f"{BLUE}Cerrando cliente REST...{RESET}")
            break
        else:
            print(f"{RED}Opcion invalida.{RESET}")


if __name__ == "__main__":
    main()
