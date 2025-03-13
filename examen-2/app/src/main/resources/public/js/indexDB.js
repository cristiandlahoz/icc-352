// Nombre de la base de datos y objeto almacenado
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

    // Actualización de la base de datos o creación inicial
    request.onupgradeneeded = (event) => {
      const db = event.target.result;

      // Verificar si el almacén de objetos ya existe
      if (!db.objectStoreNames.contains(OBJECT_STORE_NAME)) {
        const store = db.createObjectStore(OBJECT_STORE_NAME, {
          keyPath: 'id',
          autoIncrement: true
        });

        // Índice por nombre para búsquedas rápidas
        store.createIndex('name', 'name', { unique: false });
      }
    };

    request.onsuccess = (event) => resolve(event.target.result);
    request.onerror = (event) => reject(`Error al abrir la base de datos: ${event.target.errorCode}`);
  });
}

/**
 * Guarda una encuesta en la base de datos.
 * @param {Object} survey - El objeto encuesta a guardar.
 * @returns {Promise<number>} El ID de la encuesta guardada.
 */
async function saveSurvey(survey) {
  const db = await openDatabase();
  return new Promise((resolve, reject) => {
    const transaction = db.transaction(OBJECT_STORE_NAME, 'readwrite');
    const store = transaction.objectStore(OBJECT_STORE_NAME);
    const request = store.add(survey);

    request.onsuccess = (event) => resolve(event.target.result);
    request.onerror = (event) => reject(`Error al guardar la encuesta: ${event.target.errorCode}`);
  });
}

/**
 * Obtiene todas las encuestas almacenadas en la base de datos.
 * @returns {Promise<Array>} Una promesa que resuelve con un array de encuestas.
 */
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

/**
 * Actualiza una encuesta existente en la base de datos.
 * @param {Object} survey - El objeto encuesta con ID actualizado.
 * @returns {Promise<void>}
 */
async function updateSurvey(survey) {
  const db = await openDatabase();
  return new Promise((resolve, reject) => {
    const transaction = db.transaction(OBJECT_STORE_NAME, 'readwrite');
    const store = transaction.objectStore(OBJECT_STORE_NAME);
    const request = store.put(survey);

    request.onsuccess = () => resolve();
    request.onerror = (event) => reject(`Error al actualizar la encuesta: ${event.target.errorCode}`);
  });
}

/**
 * Elimina una encuesta de la base de datos.
 * @param {number} id - El ID de la encuesta a eliminar.
 * @returns {Promise<void>}
 */
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

/**
 * Manejo de errores generales en operaciones de la base de datos.
 * @param {ErrorEvent} event - El evento de error.
 */
function handleError(event) {
  console.error('IndexedDB error:', event.target.errorCode);
}
