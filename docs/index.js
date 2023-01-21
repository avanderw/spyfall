if ("WebSocket" in window) {
    console.log("Websocket is supported by your Browser!");
} else {
    console.log("Websocket is not supported by your Browser!");
}

let ws = new WebSocket("ws://avanderw.tplinkdns.com:8080");
ws.onopen = () => {
    console.log("Connection is opened...");
};
ws.onmessage = (evt) => {
    console.log("Received: " + evt.data);

    let msg = JSON.parse(evt.data);
    switch (msg.request.command) {
        case "create": location.href = `game.html?id=${msg.game.ID}`; break;
        default: console.log("Unknown command"); break;
    }

};
ws.onclose = () => {
    console.log("Connection is closed...");
};

document.getElementById("create").addEventListener("click", () => {
    let request = {command: "create"};
    ws.send(JSON.stringify(request));
});

document.getElementById("join").addEventListener("click", () => {
    location.href = `game.html?id=${document.getElementById("game").value}`;
});