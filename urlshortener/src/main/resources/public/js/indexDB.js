const DB_NAME = 'WornuxDashboardDB';
const DB_VERSION = 1;
const OBJECT_STORE_NAME = 'urls';

// Abrir base de datos
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

// Función para cargar las URLs desde IndexedDB (no forms)
async function loadUrls() {
    const db = await openDatabase();
    const transaction = db.transaction(OBJECT_STORE_NAME, 'readonly');
    const store = transaction.objectStore(OBJECT_STORE_NAME);
    const linksContainer = document.getElementById('linksContainer');

    linksContainer.innerHTML = '';

    store.openCursor().onsuccess = (event) => {
        const cursor = event.target.result;
        if (cursor) {
            const url = cursor.value;

            // Aquí creas dinámicamente la tarjeta/link para cada URL
            const card = `
                <article class="flex items-center justify-center w-[90%] lg:w-[70%] p-3 py-8 rounded-lg shadow-xl bg-btn-gray">
                    <div class="flex flex-col items-start justify-center ml-4 basis-4/5 lg:basis-3/4 gap-y-1">
                        <a class="font-normal hover:underline text-orange-300 text-btn-blue" target="_blank"
                           href="/s/${url.shortenedUrl}">${url.shortenedUrl}</a>
                        <a class="text-white font-extralight hover:underline overflow-hidden"
                           href="${url.originalUrl}" target="_blank">${url.originalUrl}</a>
                        <div class="flex items-center gap-x-5 mt-2">
                            <p class="text-white">Clicks: ${url.clickCount || 0}</p>
                        </div>
                    </div>
                </article>`;

            linksContainer.insertAdjacentHTML('beforeend', card);
            cursor.continue();
        }
    };
}

// (Borramos todo lo de syncSurveys, loadUpdateForm, cancelUpdate, updateFormData y deleteForm, ya no es necesario)
