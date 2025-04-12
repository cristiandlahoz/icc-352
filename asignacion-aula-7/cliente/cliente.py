import grpc
import asignacion_aula_7_pb2 as pb2
import asignacion_aula_7_pb2_grpc as pb2_grpc
import os

def obtener_estudiante(stub):
    matricula = int(input("Ingrese la matricula del estudiante: "))
    request = pb2.EstudianteRequest(matricula=matricula)
    try:
        response = stub.ObtenerEstudiante(request)
        print(f"> Estudiante encontrado: {response.nombre} (matricula: {response.matricula})")
    except grpc.RpcError as e:
        print(f"Error: {e.details()}")

def listar_estudiantes(stub):
    response = stub.ListarEstudiantes(pb2.Empty())
    print("📋 Lista de estudiantes:")
    for est in response.estudiantes:
        print(f"- {est.nombre} (matricula: {est.matricula})")

def crear_estudiante(stub):
    nombre = input("Ingrese el nombre del nuevo estudiante: ")
    matricula = int(input("Ingrese la matricula del nuevo estudiante: "))
    nuevo = pb2.EstudianteReply(nombre=nombre, matricula=matricula)
    response = stub.CrearEstudiante(nuevo)
    print(f"Estudiante creado: {response.nombre} (matricula: {response.matricula})")

def borrar_estudiante(stub):
    matricula = int(input("Ingrese la matricula del estudiante a borrar: "))
    request = pb2.EstudianteRequest(matricula=matricula)
    response = stub.BorrarEstudiante(request)
    print(f"{response.mensaje} (Exito: {response.exito})")

def main():
    port = os.getenv("GRPC_PORT", "8080")
    channel = grpc.insecure_channel(f'localhost:{port}')
    stub = pb2_grpc.EstudianteServiceStub(channel)

    while True:
        print("\n=== MENU gRPC Estudiantes ===")
        print("1. Consultar estudiante")
        print("2. Listar todos los estudiantes")
        print("3. Crear nuevo estudiante")
        print("4. Borrar estudiante")
        print("5. Salir")

        opcion = input("Selecciona una opcion: ")

        if opcion == "1":
            obtener_estudiante(stub)
        elif opcion == "2":
            listar_estudiantes(stub)
        elif opcion == "3":
            crear_estudiante(stub)
        elif opcion == "4":
            borrar_estudiante(stub)
        elif opcion == "5":
            print("Saliendo del cliente gRPC...")
            break
        else:
            print("Opción inválida.")

if __name__ == "__main__":
    main()
