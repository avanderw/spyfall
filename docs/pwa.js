const pwaInstall = document.getElementById("pwa-install");
window.addEventListener("beforeinstallprompt", (event) => {
    console.log("beforeinstallprompt");
    event.preventDefault(); // Prevent the mini-infobar from appearing on mobile.
    window.deferredPrompt = event; // Stash the event so it can be triggered later.
    pwaInstall.style.display = "block";
});

pwaInstall.addEventListener("click", () => {
    console.log("pwa-install");
    pwaInstall.style.display = "none";
    const promptEvent = window.deferredPrompt;
    if (!promptEvent) {
        console.log("No prompt event")
        return;
    }
    promptEvent.prompt();
    promptEvent.userChoice.then((result) => {
        if (result.outcome === "accepted") {
            console.log("User accepted the A2HS prompt");
        } else {
            console.log("User dismissed the A2HS prompt");
        }
        window.deferredPrompt = null; // Clear the saved prompt since it can't be used again.
    });
});

window.addEventListener("appinstalled", (event) => {
    console.log("appinstalled");
    window.deferredPrompt = null;
});

if ('serviceWorker' in navigator) {
    navigator.serviceWorker.register('service-worker.js')
        .then((registration) => {
            console.log('Service worker registered.');
        })
        .catch((err) => {
            console.log('Service worker registration failed: ', err);
        });
}

if (window.location.protocol === "http:") {
    console.log("Cannot install PWA if not on HTTPS");
}
