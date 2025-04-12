import os


PORT = int(os.getenv("PORT", 8080))
BASE_URL = f"http://localhost:{PORT}/api/estudiante"


def listar_estudiantes():
    response = requests.get(BASE_URL)
    if response.status_code == 200:
        estudiantes = response.json()
        print("Lista de estudiantes:")
        for est in estudiantes:
            print(f"- {est['nombre']} (matricula: {est['matricula']})")
    else:
        print(f"Error: {response.status_code}")

def consultar_estudiante():
    matricula = input("Ingrese matricula del estudiante: ")
    response = requests.get(f"{BASE_URL}/{matricula}")
    if response.status_code == 200:
        est = response.json()
        print(f"Encontrado: {est['nombre']} (matricula: {est['matricula']})")
    else:
        print("Estudiante no encontrado.")

def crear_estudiante():
    nombre = input("Nombre del estudiante: ")
    matricula = int(input("Matricula: "))
    payload = {
        "nombre": nombre,
        "matricula": matricula
    }
    response = requests.post(BASE_URL, json=payload)
    if response.status_code == 201 or response.status_code == 200:
        print("Estudiante creado correctamente.")
    else:
        print(f"Error al crear: {response.status_code} {response.text}")

def borrar_estudiante():
    matricula = input("Matricula del estudiante a eliminar: ")
    response = requests.delete(f"{BASE_URL}/{matricula}")
    if response.status_code == 204 or response.status_code == 200:
        print("Estudiante eliminado.")
    else:
        print("No se pudo eliminar (¿Existe?).")

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
