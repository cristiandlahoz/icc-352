import os
import json
import urllib.request
import urllib.parse
import jwt

# Configuración
PORT = int(os.getenv("PORT_REST", 7000))
BASE_URL = f"http://localhost:{PORT}"
LOGIN_URL = f"{BASE_URL}/auth/login"
FULL_URLS_ENDPOINT = f"{BASE_URL}/urls"
USER_URLS_ENDPOINT = f"{BASE_URL}/users/{{user_id}}/urls"

# Colores
GREEN = "\033[32m"
RED = "\033[31m"
YELLOW = "\033[33m"
BLUE = "\033[34m"
RESET = "\033[0m"

# Variables globales
token = None
user_id = None


def login():
    global token, user_id
    print("Inicio de sesión")
    username = input("Usuario: ")
    password = input("Contraseña: ")
    data = json.dumps({"username": username, "password": password}).encode()
    try:
        req = urllib.request.Request(LOGIN_URL, data=data, method="POST")
        req.add_header("Content-Type", "application/json")
        response = urllib.request.urlopen(req)
        if response.status == 200:
            res_data = json.loads(response.read().decode())
            token = res_data["token"]
            decoded = jwt.decode(token, options={"verify_signature": False})
            user_id = decoded.get("user_id")
            print(f"{GREEN} Login exitoso. ID de usuario: {user_id}{RESET}")
        else:
            print(f"{RED} Error al autenticar.{RESET}")
    except Exception as e:
        print(f"{RED}Error durante login: {e}{RESET}")


def crear_url():
    original = input(" Ingresa la URL a acortar: ")
    payload = json.dumps(
        {"originalUrl": original, "createdBy": {"id": user_id}}
    ).encode()

    try:
        req = urllib.request.Request(FULL_URLS_ENDPOINT, data=payload, method="POST")
        req.add_header("Content-Type", "application/json")
        req.add_header("Authorization", f"Bearer {token}")
        response = urllib.request.urlopen(req)
        if response.status == 201:
            res_data = json.loads(response.read().decode())
            print(f"{GREEN} URL creada:")
            print(f" - Original: {res_data['originalUrl']}")
            print(f" - Acortada: {res_data['shortenedUrl']}")
            print(f" - Fecha: {res_data['createdAt']}")
            print(f" - Clicks: {res_data['stats']['clickCount']}")
            print(f" - Vista previa: {res_data['sitePreviewBase64'][:30]}...{RESET}")
        else:
            print(f"{RED} Error al crear URL.{RESET}")
    except Exception as e:
        print(f"{RED}Error al crear URL: {e}{RESET}")


def listar_urls():
    url = USER_URLS_ENDPOINT.format(user_id=user_id)
    try:
        req = urllib.request.Request(url, method="GET")
        req.add_header("Authorization", f"Bearer {token}")
        response = urllib.request.urlopen(req)
        if response.status == 200:
            urls = json.loads(response.read().decode())
            print(f"\n{BLUE} URLs del usuario:{RESET}")
            for u in urls:
                print("────────────────────────────")
                print(f"Original: {u['originalUrl']}")
                print(f"Acortada: {u['shortenedUrl']}")
                print(f"Clicks: {u['clickCount']}")
                print(f"Visitantes únicos: {u['uniqueVisitors']}")
        else:
            print(f"{YELLOW} No se pudieron listar las URLs.{RESET}")
    except Exception as e:
        print(f"{RED}Error al listar URLs: {e}{RESET}")


def main():
    print(f"{BLUE}=== Cliente REST: Acortador de URLs ==={RESET}")
    login()
    if not token or not user_id:
        print(f"{RED} No se pudo autenticar. Saliendo...{RESET}")
        return

    while True:
        print("\nMenú:")
        print("1. Crear nueva URL")
        print("2. Listar mis URLs")
        print("3. Salir")

        opcion = input("Selecciona una opción: ")

        if opcion == "1":
            crear_url()
        elif opcion == "2":
            listar_urls()
        elif opcion == "3":
            print(f"{YELLOW}Saliendo del cliente...{RESET}")
            break
        else:
            print(f"{RED} Opción inválida{RESET}")


if __name__ == "__main__":
    main()
