import os
import grpc
import urlshortener_pb2
import urlshortener_pb2_grpc

GREEN = "\033[32m"
RED = "\033[31m"
YELLOW = "\033[33m"
BLUE = "\033[34m"
RESET = "\033[0m"

PORT = int(os.getenv("PORT_GRPC", 9090))
SERVER_ADDRESS = f"localhost:{PORT}"

def crear_url():
    channel = grpc.insecure_channel(SERVER_ADDRESS)
    stub = urlshortener_pb2_grpc.UrlShortenerServiceStub(channel)

    print(f"{BLUE}Crear nueva URL{RESET}")
    user_id = input("Ingrese el ID del usuario: ")
    original_url = input("Ingrese la URL original: ")

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
        print(f"  - Vista previa (base64): {response.site_image[:30]}...{RESET}")
    except grpc.RpcError as e:
        print(f"{RED}Error en CreateUrl: {e}{RESET}")
    finally:
        channel.close()

def listar_urls():
    channel = grpc.insecure_channel(SERVER_ADDRESS)
    stub = urlshortener_pb2_grpc.UrlShortenerServiceStub(channel)

    print(f"{BLUE}Listar URLs por usuario{RESET}")
    user_id = input("Ingrese el ID del usuario: ")

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
