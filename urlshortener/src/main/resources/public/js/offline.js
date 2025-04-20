//const DB_NAME = 'WornuxDashboardDB';
//const DB_VERSION = 1;
//const OBJECT_STORE_NAME = 'urls';

/**
 * Abre o crea la base de datos IndexedDB.
 * @returns {Promise<IDBDatabase>} Una promesa que resuelve con la base de datos abierta.
 */
function openDatabase() {
    return new Promise((resolve, reject) => {
        const request = indexedDB.open(DB_NAME, DB_VERSION);

        request.onupgradeneeded = (event) => {
            const db = event.target.result;

            if (!db.objectStoreNames.contains(OBJECT_STORE_NAME)) {
                db.createObjectStore(OBJECT_STORE_NAME, {
                    keyPath: 'id',
                    autoIncrement: true
                });
            }
        };

        request.onsuccess = (event) => resolve(event.target.result);
        request.onerror = (event) => reject(`Error al abrir la base de datos: ${event.target.errorCode}`);
    });
}

/**
 * Guarda varias URLs en IndexedDB (sobreescribiendo las anteriores).
 */
async function saveUrls(urls) {
    const db = await openDatabase();
    const transaction = db.transaction(OBJECT_STORE_NAME, 'readwrite');
    const store = transaction.objectStore(OBJECT_STORE_NAME);

    // Limpia primero el store (opcional: si quieres mantener siempre actualizado)
    await store.clear();

    for (const url of urls) {
        store.add(url);
    }
}

/**
 * Obtiene todas las URLs almacenadas en IndexedDB.
 */
async function getAllUrls() {
    const db = await openDatabase();
    return new Promise((resolve, reject) => {
        const transaction = db.transaction(OBJECT_STORE_NAME, 'readonly');
        const store = transaction.objectStore(OBJECT_STORE_NAME);
        const request = store.getAll();

        request.onsuccess = (event) => resolve(event.target.result);
        request.onerror = (event) => reject(`Error al obtener URLs: ${event.target.errorCode}`);
    });
}

// No necesitamos ningún "window.addEventListener('online')" aquí
// Porque la sincronización de URLs se maneja aparte o al cargar.
