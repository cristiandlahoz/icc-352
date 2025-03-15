const DB_NAME = 'SurveyDB';
const DB_VERSION = 1;
const OBJECT_STORE_NAME = 'surveys';

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
        const store = db.createObjectStore(OBJECT_STORE_NAME, {
          keyPath: 'id',
          autoIncrement: true
        });
        store.createIndex('username', 'username', { unique: false });
      }
    };

    request.onsuccess = (event) => resolve(event.target.result);
    request.onerror = (event) => reject(`Error al abrir la base de datos: ${event.target.errorCode}`);
  });
}
async function saveSurvey(survey) {
  const db = await openDatabase();
  return new Promise((resolve, reject) => {
    const transaction = db.transaction(OBJECT_STORE_NAME, 'readwrite');
    const store = transaction.objectStore(OBJECT_STORE_NAME);
    const request = store.add(survey);

    request.onsuccess = () => resolve(request.result);
    request.onerror = (event) => reject(`Error al guardar la encuesta: ${event.target.errorCode}`);
  });
}
async function getAllSurveys() {
  const db = await openDatabase();
  return new Promise((resolve, reject) => {
    const transaction = db.transaction(OBJECT_STORE_NAME, 'readonly');
    const store = transaction.objectStore(OBJECT_STORE_NAME);
    const request = store.getAll();

    request.onsuccess = (event) => resolve(event.target.result);
    request.onerror = (event) => reject(`Error al obtener encuestas: ${event.target.errorCode}`);
  });
}
window.addEventListener('online', async () => {
  const review = confirm('¿Desea sincronizar los datos almacenados localmente?');
  if (review) {
    window.location.href = '/forms/forms';
    return;
  }

  const encuestasPendientes = await getAllSurveys();

  for (const encuesta of encuestasPendientes) {
    try {
      const encuestadoId = encuesta.id;
      delete encuesta['id'];
      await fetch('/forms/create', {
        method: 'POST',
        body: JSON.stringify(encuesta),
        headers: { 'Content-Type': 'application/json' }
      });

      await deleteSurvey(encuestadoId); // Elimina la encuesta después de sincronizarse
      console.log(`Encuesta ID ${encuestadoId} sincronizada correctamente.`);
    } catch (error) {
      console.error(`Error al sincronizar la encuesta ID ${encuesta.id}:`, error);
    }
  }

  alert('Se sincronizaron los datos almacenados localmente.');
});
async function deleteSurvey(id) {
  const db = await openDatabase();
  return new Promise((resolve, reject) => {
    const transaction = db.transaction(OBJECT_STORE_NAME, 'readwrite');
    const store = transaction.objectStore(OBJECT_STORE_NAME);
    const request = store.delete(id);

    request.onsuccess = () => resolve();
    request.onerror = (event) => reject(`Error al eliminar la encuesta: ${event.target.errorCode}`);
  });
}
