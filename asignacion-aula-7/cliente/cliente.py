import grpc
import asignacion_aula_7_pb2 as pb2
import asignacion_aula_7_pb2_grpc as pb2_grpc
import os

# Códigos ANSI para colores
RESET = "\033[0m"
CYAN = "\033[96m"
YELLOW = "\033[33m"
RED = "\033[31m"
BLUE = "\033[34m"
MAGENTA = "\033[95m"


def obtener_estudiante(stub):
    matricula = int(input(CYAN + "Ingrese la matricula del estudiante: " + RESET))
    request = pb2.EstudianteRequest(matricula=matricula)
    try:
        response = stub.ObtenerEstudiante(request)
        print(
            BLUE
            + f"> Estudiante encontrado: {response.nombre} (matricula: {
                response.matricula
            })"
            + RESET
        )
    except grpc.RpcError as e:
        print(RED + f"Error: {e.details()}" + RESET)


def listar_estudiantes(stub):
    response = stub.ListarEstudiantes(pb2.Empty())
    print(YELLOW + "📋 Lista de estudiantes:" + RESET)
    for est in response.estudiantes:
        print(BLUE + f"- {est.nombre} (matricula: {est.matricula})" + RESET)


def crear_estudiante(stub):
    nombre = input(CYAN + "Ingrese el nombre del nuevo estudiante: " + RESET)
    matricula = int(input(CYAN + "Ingrese la matricula del nuevo estudiante: " + RESET))
    nuevo = pb2.EstudianteReply(nombre=nombre, matricula=matricula)
    response = stub.CrearEstudiante(nuevo)
    print(
        BLUE
        + f"Estudiante creado: {response.nombre} (matricula: {response.matricula})"
        + RESET
    )


def borrar_estudiante(stub):
    matricula = int(
        input(CYAN + "Ingrese la matricula del estudiante a borrar: " + RESET)
    )
    request = pb2.EstudianteRequest(matricula=matricula)
    response = stub.BorrarEstudiante(request)
    print(BLUE + f"{response.mensaje} (Exito: {response.exito})" + RESET)


def main():
    port = os.getenv("GRPC_PORT", "8080")
    channel = grpc.insecure_channel(f"localhost:{port}")
    stub = pb2_grpc.EstudianteServiceStub(channel)

    while True:
        print(RED + "\n=== MENU gRPC Estudiantes ===" + RESET)
        print(YELLOW + "1. Consultar estudiante" + RESET)
        print(YELLOW + "2. Listar todos los estudiantes" + RESET)
        print(YELLOW + "3. Crear nuevo estudiante" + RESET)
        print(YELLOW + "4. Borrar estudiante" + RESET)
        print(YELLOW + "5. Salir" + RESET)

        opcion = input(CYAN + "Selecciona una opcion: " + RESET)

        if opcion == "1":
            obtener_estudiante(stub)
        elif opcion == "2":
            listar_estudiantes(stub)
        elif opcion == "3":
            crear_estudiante(stub)
        elif opcion == "4":
            borrar_estudiante(stub)
        elif opcion == "5":
            print(MAGENTA + "Saliendo del cliente gRPC..." + RESET)
            break
        else:
            print(RED + "Opción inválida." + RESET)


if __name__ == "__main__":
    main()
