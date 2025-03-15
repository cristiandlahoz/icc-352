const DB_NAME = 'SurveyDB';
const DB_VERSION = 1;
const OBJECT_STORE_NAME = 'surveys';

async function openDatabase() {
  return new Promise((resolve, reject) => {
    const request = indexedDB.open(DB_NAME, DB_VERSION);
    request.onupgradeneeded = (event) => {
      const db = event.target.result;
      if (!db.objectStoreNames.contains(OBJECT_STORE_NAME)) {
        db.createObjectStore(OBJECT_STORE_NAME, { keyPath: 'id', autoIncrement: true });
      }
    };
    request.onsuccess = (event) => resolve(event.target.result);
    request.onerror = (event) => reject(`Error al abrir la base de datos: ${event.target.errorCode}`);
  });
}

async function loadForms() {
  const db = await openDatabase();
  const transaction = db.transaction(OBJECT_STORE_NAME, 'readonly');
  const store = transaction.objectStore(OBJECT_STORE_NAME);
  const tableBody = document.getElementById('formsTableBody');
  tableBody.innerHTML = '';

  store.openCursor().onsuccess = (event) => {
    const cursor = event.target.result;
    if (cursor) {
      const form = cursor.value;
      const row = `
                <tr class="border-b border-gray-200 hover:bg-gray-100">
                    <td class="py-3 px-6 text-left">${form.id}</td>
                    <td class="py-3 px-6 text-left">${form.username}</td>
                    <td class="py-3 px-6 text-left">${form.fullname}</td>
                    <td class="py-3 px-6 text-left">${form.sector}</td>
                    <td class="py-3 px-6 text-left">${form.education}</td>
                    <td class="py-3 px-6 text-left">${form.isSynchronized ? 'Yes' : 'No'}</td>
                    <td class="py-3 px-6 text-center">
                        <button onclick="loadUpdateForm(${form.id})" class="w-4 mr-2 transform hover:text-blue-500 hover:scale-110">✎</button>
                        <button onclick="deleteForm(${form.id})" class="w-4 mr-2 transform hover:text-red-500 hover:scale-110">🗑️</button>
                    </td>
                </tr>`;
      tableBody.insertAdjacentHTML('beforeend', row);
      cursor.continue();
    }
  };
}
/**
 * Synchronizes offline surveys stored in IndexedDB with the server.
 * Automatically triggered when the application goes online.
 */
async function syncSurveys() {
  let db;

  try {
    db = await new Promise((resolve, reject) => {
      const request = indexedDB.open(DB_NAME, 1);
      request.onsuccess = () => resolve(request.result);
      request.onerror = () => reject(new Error('Failed to open the IndexedDB database'));
    });

    const pendingSurveys = await new Promise((resolve, reject) => {
      const transaction = db.transaction(OBJECT_STORE_NAME, 'readonly');
      const store = transaction.objectStore(OBJECT_STORE_NAME);
      const request = store.getAll();
      request.onsuccess = () => resolve(request.result);
      request.onerror = () => reject(new Error('Failed to retrieve surveys from the database'));
    });

    if (pendingSurveys.length === 0) {
      console.log('No pending surveys to synchronize.');
      return;
    }

    for (const survey of pendingSurveys) {
      const surveyId = survey.id;
      delete survey['id'];

      try {
        const response = await fetch('/forms/create', {
          method: 'POST',
          body: JSON.stringify(survey),
          headers: { 'Content-Type': 'application/json' }
        });

        if (!response.ok) {
          throw new Error(`Failed to synchronize survey ID ${surveyId}: ${response.statusText}`);
        }

        await new Promise((resolve, reject) => {
          const deleteTransaction = db.transaction(OBJECT_STORE_NAME, 'readwrite');
          const deleteStore = deleteTransaction.objectStore(OBJECT_STORE_NAME);
          const deleteRequest = deleteStore.delete(surveyId);
          deleteRequest.onsuccess = () => resolve();
          deleteRequest.onerror = () => reject(new Error(`Failed to delete survey ID ${surveyId} from the database`));
        });

        console.log(`✅ Survey ID ${surveyId} successfully synchronized.`);
      } catch (error) {
        console.error(`🚨 Error synchronizing survey ID ${surveyId}:`, error);
      }
    }

    alert('Local data successfully synchronized.');
  } catch (error) {
    console.error('❌ Error during survey synchronization:', error);
    alert('An error occurred while synchronizing surveys. Check the console for details.');
  } finally {
    if (db) {
      db.close();
    }
  }
}

async function loadUpdateForm(id) {
  const db = await openDatabase();
  const transaction = db.transaction(OBJECT_STORE_NAME, 'readonly');
  const store = transaction.objectStore(OBJECT_STORE_NAME);

  store.get(id).onsuccess = (event) => {
    const form = event.target.result;
    if (form) {
      document.getElementById('formId').value = form.id;
      document.getElementById('username').value = form.username;
      document.getElementById('fullname').value = form.fullname;
      document.getElementById('sector').value = form.sector;
      document.getElementById('education').value = form.education;

      // Oculta la lista y muestra el formulario
      document.getElementById('formListContainer').style.display = "none";
      document.getElementById('updateFormContainer').style.display = "block";
    }
  };
}

function cancelUpdate() {
  document.getElementById('updateFormContainer').style.display = "none";
  document.getElementById('formListContainer').style.display = "block";
}

async function updateFormData() {
  const formId = Number(document.getElementById('formId').value);
  const updatedFullname = document.getElementById('fullname').value;
  const updatedSector = document.getElementById('sector').value;
  const updatedEducation = document.getElementById('education').value;

  const db = await openDatabase();
  const transaction = db.transaction(OBJECT_STORE_NAME, 'readwrite');
  const store = transaction.objectStore(OBJECT_STORE_NAME);

  store.get(formId).onsuccess = (event) => {
    const form = event.target.result;
    if (form) {
      form.fullname = updatedFullname;
      form.sector = updatedSector;
      form.education = updatedEducation;
      form.isSynchronized = false; // Indicar que hay cambios sin sincronizar

      store.put(form).onsuccess = () => {
        alert('✅ Formulario actualizado correctamente.');

        // Ocultar formulario y volver a mostrar la lista
        document.getElementById('updateFormContainer').style.display = "none";
        document.getElementById('formListContainer').style.display = "block";

        // Recargar la lista con los nuevos datos
        loadForms();
      };
    }
  };
}

async function deleteForm(formId) {
  const db = await openDatabase();
  const transaction = db.transaction(OBJECT_STORE_NAME, 'readwrite');
  const store = transaction.objectStore(OBJECT_STORE_NAME);

  store.delete(formId).onsuccess = () => {
    alert("✅ Formulario eliminado correctamente.");
    loadForms();
  };
}
