import requests
import jwt

BASE_URL = "http://localhost:7000"

def login():
    print("Inicio de sesión")
    username = input("Usuario: ")
    password = input("Contraseña: ")

    url = f"{BASE_URL}/auth/login"
    data = {"username": username, "password": password}
    response = requests.post(url, json=data)

    if response.status_code == 200:
        token = response.json()["token"]
        print("Token obtenido")
        return token
    else:
        print("Error al autenticar:", response.text)
        return None

# Decodificar user_id desde el JWT (sin verificar firma)
def decode_user_id_from_token(token):
    try:
        decoded = jwt.decode(token, options={"verify_signature": False})
        return decoded.get("user_id")
    except Exception as e:
        print("Error al decodificar el token:", e)
        return None


def create_url(token, user_id):
    url = f"{BASE_URL}/urls/full"
    headers = {"Authorization": f"Bearer {token}"}
    original = input("Ingresa la URL a acortar: ")

    payload = {
        "originalUrl": original,
        "createdBy": {
            "id": user_id
        }
    }

    response = requests.post(url, json=payload, headers=headers)
    if response.status_code == 201:
        data = response.json()
        print("\nURL creada:")
        print("  - Original:", data['originalUrl'])
        print("  - Acortada:", data['shortenedUrl'])
        print("  - Fecha creacion:", data['createdAt'])
        print("  - Clicks:", data['stats']['clickCount'])
        print("  - Vista previa (base64):", data['sitePreviewBase64'][:30], "...")
    else:
        print("Error al crear URL:", response.text)


def list_urls(token, user_id):
    url = f"{BASE_URL}/users/{user_id}/urls"
    headers = {"Authorization": f"Bearer {token}"}
    response = requests.get(url, headers=headers)
    if response.status_code == 200:
        print("\nURLs del usuario:")
        for item in response.json():
            print("────────────────────────────")
            print("Original:", item['originalUrl'])
            print("Acortada:", item['shortenedUrl'])
            print("Clicks:", item['clickCount'])
            print("Visitantes únicos:", item['uniqueVisitors'])
    else:
        print("Error al listar URLs:", response.text)

def main():
    token = login()
    if not token:
        return

    user_id = decode_user_id_from_token(token)
    if not user_id:
        print("No se pudo obtener el ID del usuario desde el token.")
        return

    print(f"ID del usuario autenticado: {user_id}")

    while True:
        print("\nMenú:")
        print("1. Crear URL")
        print("2. Listar URLs")
        print("3. Salir")
        opcion = input("Selecciona una opción: ")

        if opcion == "1":
            create_url(token, user_id)
        elif opcion == "2":
            list_urls(token, user_id)
        elif opcion == "3":
            print("¡Hasta luego!")
            break
        else:
            print("Opción inválida")

if __name__ == "__main__":
    main()
