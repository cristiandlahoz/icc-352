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

// Convertir imagen a Base64
async function toBase64(url) {
    const response = await fetch(url);
    const blob = await response.blob();
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.onloadend = () => resolve(reader.result);
        reader.onerror = reject;
        reader.readAsDataURL(blob);
    });
}

// Guardar las URLs actuales con imagen
async function saveCurrentUrls() {
    const urls = [];

    for (const link of document.querySelectorAll('.link')) {
        const originalUrl = link.getAttribute('data-url');
        const shortenedUrl = link.previousElementSibling?.textContent || '';

        const imgElement = link.closest('article')?.querySelector('img');

        let previewImage = '';
        if (imgElement && imgElement.src) {
            try {
                previewImage = await toBase64(imgElement.src);
            } catch (error) {
                console.error('Error convirtiendo imagen a Base64:', error);
            }
        }

        urls.push({
            shortenedUrl,
            originalUrl,
            clickCount: 0,
            previewImage,
        });
    }

    const db = await openDatabase();
    const transaction = db.transaction(OBJECT_STORE_NAME, 'readwrite');
    const store = transaction.objectStore(OBJECT_STORE_NAME);

    for (const url of urls) {
        store.put(url);
    }

    return transaction.complete;
}

// Cargar las URLs desde IndexedDB
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

            const card = `
              <article class="flex items-center justify-center w-[90%] lg:w-[70%] p-3 py-8 rounded-lg shadow-xl bg-btn-gray">
                  <div class="items-center justify-center hidden lg:flex lg:basis-2/12">
                      <img src="${url.previewImage || '/images/placeholder.png'}" alt="Link preview" />
                  </div>
                  <div class="flex flex-col items-start justify-center ml-4 basis-4/5 lg:basis-3/4 gap-y-1">
                      <a class="font-normal hover:underline text-orange-300 text-btn-blue" target="_blank" href="/s/${url.shortenedUrl}">${url.shortenedUrl}</a>
                      <a class="text-white font-extralight hover:underline overflow-hidden" href="${url.originalUrl}" target="_blank">${url.originalUrl}</a>
                      <div class="flex items-center gap-x-5 mt-2">
                          <p class="text-white">Clicks: ${url.clickCount || 0}</p>
                      </div>
                  </div>
              </article>
          `;

            linksContainer.insertAdjacentHTML('beforeend', card);
            cursor.continue();
        }
    };
}
