const CACHE_NAME = 'cache';
const VERSION = "v2.0.0";
const OFFLINE_URL = 'offline.html';
const URLS_TO_CACHE = [];

self.addEventListener('install', function (event) {
  console.log('[ServiceWorker] Install');

  event.waitUntil((async () => {
    await caches.open(CACHE_NAME + VERSION)
      .then(cache => {
        // Setting {cache: 'reload'} in the new request will ensure that the response
        // isn't fulfilled from the HTTP cache; i.e., it will be from the network.
        cache.add(new Request(OFFLINE_URL, { cache: 'reload' }));
        return cache.addAll(URLS_TO_CACHE);

      });
  })());

  self.skipWaiting();
});

self.addEventListener('activate', (event) => {
  console.log('[ServiceWorker] Activate');
  event.waitUntil((async () => {
    // Enable navigation preload if it's supported.
    // See https://developers.google.com/web/updates/2017/02/navigation-preload
    if ('navigationPreload' in self.registration) {
      await self.registration.navigationPreload.enable();
    }
  })());

  // Tell the active service worker to take control of the page immediately.
  self.clients.claim();
});

self.addEventListener('fetch', function (event) {
  console.log('[Service Worker] Fetch', event.request.url);
  if (event.request.mode === 'navigate') {
    event.respondWith((async () => {
      try {
        const preloadResponse = await event.preloadResponse;
        if (preloadResponse) {
          console.log('[Service Worker] Using navigation preload')
          return preloadResponse;
        }

        const networkResponse = await fetch(event.request);
        console.log('[Service Worker] Fetch OK');
        return networkResponse;
      } catch (error) {
        console.log('[Service Worker] Fetch failed; returning offline page instead.', error);

        const cache = await caches.open(CACHE_NAME);
        const cachedResponse = await cache.match(OFFLINE_URL);
        return cachedResponse;
      }
    })());
  }
});