if (workbox) {
  console.log(`Yay! Workbox is loaded!`);
} else {
  console.error(`Boo! Workbox didn't load!`);
}

workbox.core.setLogLevel(workbox.core.LOG_LEVELS.warn);

workbox.core.setCacheNameDetails({
  prefix: "ben-app",
  suffix: "v16",
  precache: "install-time"
});

workbox.core.skipWaiting();
workbox.core.clientsClaim();
workbox.precaching.precacheAndRoute(self.__precacheManifest || []);

workbox.routing.registerRoute(
  // Cache CSS files
  /.*\.css/,
  workbox.strategies.staleWhileRevalidate({
    cacheName: "css-cache"
  })
);
workbox.routing.registerRoute(
  /.*\.js/,
  workbox.strategies.staleWhileRevalidate({
    cacheName: "js-cache"
  })
);

workbox.routing.registerRoute(
  /\.(?:png|gif|jpg|jpeg|svg)$/,
  workbox.strategies.staleWhileRevalidate({
    cacheName: "images",
    plugins: [
      new workbox.expiration.Plugin({
        maxEntries: 60,
        maxAgeSeconds: 7 * 24 * 60 * 60
      })
    ]
  })
);
