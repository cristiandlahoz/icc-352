import grpc
import urlshortener_pb2
import urlshortener_pb2_grpc
from google.protobuf.timestamp_pb2 import Timestamp

# Dirección del servidor gRPC y puerto
SERVER_ADDRESS = 'localhost:9090'

def run_create_url():
    channel = grpc.insecure_channel(SERVER_ADDRESS)
    stub = urlshortener_pb2_grpc.UrlShortenerServiceStub(channel)

    # Construir la solicitud para crear la URL
    create_request = urlshortener_pb2.CreateUrlRequest(
        user_id="67ebf32287070010986e4cf8",  # Usando snake_case
        original_url="http://example.com"     # Usando snake_case
    )


    try:
        response = stub.CreateUrl(create_request)
        print("Creación de URL:")
        print("  - URL original:", response.original_url)
        print("  - URL corta:", response.short_url)
        print("  - Fecha de creación (Timestamp):", response.creation_date)
        print("  - Imagen del sitio (base64):", response.site_image)
        print("  - Estadísticas: Clicks =", response.stats.clicks,
              ", Unique Visitors =", response.stats.unique_visitors)
    except grpc.RpcError as e:
        print("Error en CreateUrl:", e)
    finally:
        channel.close()

def run_list_user_urls():
    channel = grpc.insecure_channel(SERVER_ADDRESS)
    stub = urlshortener_pb2_grpc.UrlShortenerServiceStub(channel)

    # Construir la solicitud para listar las URLs de un usuario
    list_request = urlshortener_pb2.ListUserUrlsRequest(
        user_id="67ebf32287070010986e4cf8"
    )

    try:
        response = stub.ListUserUrls(list_request)
        print("\nListado de URLs del usuario:")
        for url_data in response.user_urls:
            print("  - URL original:", url_data.original_url)
            print("    URL corta:", url_data.short_url)
            print("    Fecha de creación:", url_data.creation_date)
            print("    Imagen del sitio (base64):", url_data.site_image)
            print("    Estadísticas: Clicks =", url_data.stats.clicks,
                  ", Unique Visitors =", url_data.stats.unique_visitors)
            print("----------------------------------------------------------")
    except grpc.RpcError as e:
        print("Error en ListUserUrls:", e)
    finally:
        channel.close()

if __name__ == '__main__':
    # Primero, prueba la creación de una URL
    run_create_url()

    # Luego, prueba el listado de URLs del mismo usuario (o de otro usuario, si así lo deseas)
    run_list_user_urls()
