import os
import json
import jwt
import urllib.request
import grpc
import urlshortener_pb2
import urlshortener_pb2_grpc

# Configuración
PORT_GRPC = int(os.getenv("PORT_GRPC", 9090))
SERVER_ADDRESS = f"localhost:{PORT_GRPC}"
PORT_REST = int(os.getenv("PORT_REST", 7000))
REST_LOGIN_URL = f"http://localhost:{PORT_REST}/auth/login"

GREEN = "\033[32m"
RED = "\033[31m"
YELLOW = "\033[33m"
BLUE = "\033[34m"
RESET = "\033[0m"

# Variables globales
user_id = None
token = None

def login_rest():
    global user_id, token
    print("Inicio de sesión")
    username = input("Usuario: ")
    password = input("Contraseña: ")

    payload = json.dumps({"username": username, "password": password}).encode()

    try:
        req = urllib.request.Request(REST_LOGIN_URL, data=payload, method="POST")
        req.add_header("Content-Type", "application/json")
        response = urllib.request.urlopen(req)
        if response.status == 200:
            data = json.loads(response.read().decode())
            token = data["token"]
            decoded = jwt.decode(token, options={"verify_signature": False})
            user_id = decoded.get("user_id")
            print(f"{GREEN}Login exitoso. ID del usuario: {user_id}{RESET}")
        else:
            print(f"{RED}Error al autenticar.{RESET}")
    except Exception as e:
        print(f"{RED}Error al autenticar: {e}{RESET}")

def crear_url():
    channel = grpc.insecure_channel(SERVER_ADDRESS)
    stub = urlshortener_pb2_grpc.UrlShortenerServiceStub(channel)

    original_url = input("Ingresa la URL original: ")

    create_request = urlshortener_pb2.CreateUrlRequest(
        user_id=user_id,
        original_url=original_url
    )

    try:
        response = stub.CreateUrl(create_request)
        print(f"{GREEN}URL creada:")
        print(f"  - Original: {response.original_url}")
        print(f"  - Corta: {response.short_url}")
        print(f"  - Fecha: {response.creation_date}")
        print(f"  - Clicks: {response.stats.clicks}")
        print(f"  - Visitantes únicos: {response.stats.unique_visitors}")
        print(f"  - Vista previa: {response.site_image[:30]}...{RESET}")
    except grpc.RpcError as e:
        print(f"{RED}Error en CreateUrl: {e}{RESET}")
    finally:
        channel.close()

def listar_urls():
    channel = grpc.insecure_channel(SERVER_ADDRESS)
    stub = urlshortener_pb2_grpc.UrlShortenerServiceStub(channel)

    list_request = urlshortener_pb2.ListUserUrlsRequest(user_id=user_id)

    try:
        response = stub.ListUserUrls(list_request)
        print(f"{GREEN}\nURLs encontradas:{RESET}")
        for url_data in response.user_urls:
            print("────────────────────────────")
            print(f"Original: {url_data.original_url}")
            print(f"Acortada: {url_data.short_url}")
            print(f"Fecha: {url_data.creation_date}")
            print(f"Clicks: {url_data.stats.clicks}")
            print(f"Visitantes únicos: {url_data.stats.unique_visitors}")
            print(f"Vista previa: {url_data.site_image[:30]}...")
    except grpc.RpcError as e:
        print(f"{RED}Error en ListUserUrls: {e}{RESET}")
    finally:
        channel.close()

def main():
    login_rest()

    if not user_id:
        print(f"{RED}No se pudo obtener ID del usuario. Terminando...{RESET}")
        return

    while True:
        print(f"\n{YELLOW}=== Cliente gRPC: Acortador de URLs ==={RESET}")
        print("1. Crear nueva URL")
        print("2. Listar URLs del usuario")
        print("3. Salir")

        opcion = input("Selecciona una opción: ")

        if opcion == "1":
            crear_url()
        elif opcion == "2":
            listar_urls()
        elif opcion == "3":
            print(f"{BLUE}Cerrando cliente gRPC...{RESET}")
            break
        else:
            print(f"{RED}Opción inválida.{RESET}")

if __name__ == "__main__":
    main()
