const CACHE_NAME = 'my-app-cache-v1';
const STATIC_ASSETS = [
  '/',
  '/forms/create',
  '/js/offline.js',
  '/js/indexDB.js',
  '/sw.js',
  '/css/output.css',
  '/favicon.ico',
  '/forms/forms',
];

// Install event: Cache static assets
self.addEventListener('install', (event) => {
  event.waitUntil(
    caches.open(CACHE_NAME)
      .then((cache) => {
        console.log('Caching static assets');
        return cache.addAll(STATIC_ASSETS);
      })
  );
});

// Fetch event: Serve from cache if available
self.addEventListener('fetch', (event) => {
  event.respondWith(
    caches.match(event.request)
      .then((response) => {
        if (event.request.method === 'POST') {
          return fetch(event.request);
        } else {
          console.log(`method: ${event.request.method}`);
          response ? console.log(`Serving from cache: ${event.request.url}`) : console.log(`Fetching: ${event.request.url}`);
          return response || fetch(event.request);
        }
      }).catch((error) => {
        console.error('Error in fetch handler:', error);
      }))
});

// Activate event: Clean up old caches
self.addEventListener('activate', (event) => {
  event.waitUntil(
    caches.keys().then((cacheNames) => {
      return Promise.all(
        cacheNames
          .filter((name) => name !== CACHE_NAME)
          .map((name) => caches.delete(name))
      );
    })
  );
});
