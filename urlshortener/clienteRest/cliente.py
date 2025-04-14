import requests

BASE_URL = "http://localhost:7000"
USERNAME = "eliana"
PASSWORD = "eliana"
USER_ID = "67ebf32287070010986e4cf8"  # reemplaza con el ObjectId real

# (1) AUTENTICACIÓN
def login():
    url = f"{BASE_URL}/auth/login"
    data = {"username": USERNAME, "password": PASSWORD}
    response = requests.post(url, json=data)
    if response.status_code == 200:
        token = response.json()["token"]
        print("✅ Token obtenido")
        return token
    else:
        print("❌ Error al autenticar:", response.text)
        return None

# (2) CREACIÓN DE URL
def create_url(token):
    url = f"{BASE_URL}/urls/full"
    headers = {"Authorization": f"Bearer {token}"}
    payload = {
        "originalUrl": "https://vaadin.com/docs/v14/flow/guide/start/gradle",
        "createdBy": {
            "id": USER_ID
        }
    }
    response = requests.post(url, json=payload, headers=headers)
    if response.status_code == 201:
        data = response.json()
        print("\n✅ URL creada:")
        print("  - Original:", data['originalUrl'])
        print("  - Acortada:", data['shortenedUrl'])
        print("  - Fecha creación:", data['createdAt'])
        print("  - Clicks:", data['stats']['clickCount'])
        print("  - Vista previa (base64):", data['sitePreviewBase64'][:30], "...")
    else:
        print("❌ Error al crear URL:", response.text)

# (3) LISTADO DE URLS POR USUARIO
def list_urls(token):
    url = f"{BASE_URL}/users/{USER_ID}/urls"
    headers = {"Authorization": f"Bearer {token}"}
    response = requests.get(url, headers=headers)
    if response.status_code == 200:
        print("\n📋 URLs del usuario:")
        for item in response.json():
            print("────────────────────────────")
            print("Original:", item['originalUrl'])
            print("Acortada:", item['shortenedUrl'])
            print("Clicks:", item['clickCount'])
            print("Visitantes únicos:", item['uniqueVisitors'])
    else:
        print("❌ Error al listar URLs:", response.text)

# MAIN
if __name__ == "__main__":
    token = login()
    if token:
        create_url(token)
        list_urls(token)
