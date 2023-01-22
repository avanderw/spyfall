if ("WebSocket" in window) {
    console.log("Websocket is supported by your Browser!");
} else {
    console.log("Websocket is not supported by your Browser!");
}

let thisPlayerID;
let gameID = getQueryParameter("id");
document.getElementById("game").innerText = `#${gameID}`;

let ws = new WebSocket("ws://avanderw.tplinkdns.com:8080");
ws.onopen = () => {
    console.log("Connection is opened...");
    ws.send(JSON.stringify({ command: "open", gameID: gameID }));
};
ws.onmessage = (evt) => {
    console.log("Received: " + evt.data);

    let msg = JSON.parse(evt.data);
    switch (msg.request.command) {
        case "open": handleOpen(msg); break;
        case "close": handleClose(msg); break;
        case "join": handleJoin(msg); break;
        case "ready": handleReady(msg); break;
        case "start": handleStart(msg); break;
        default: console.log("Unknown command"); break;
    }

    game = msg.game;
};
ws.onclose = () => {
    console.log("Connection is closed...");
};

function getQueryParameter(variable) {
    let query = window.location.search.substring(1);
    let vars = query.split("&");
    for (const element of vars) {
        let pair = element.split("=");
        if (pair[0] == variable) {
            return pair[1];
        }
    }
    return "";
}

let game;
function handleOpen(msg) {
    console.log("Open");
    game = msg.game;
    if (game.Players && game.Players.length > 0) {
        for (const player of msg.game.Players) {
            const playerElement = document.createElement("li");
            playerElement.classList.add("list-group-item");
            playerElement.id = player.ID;
            playerElement.innerText = player.ID;
            document.getElementById("players").appendChild(playerElement);
            if (player.Ready) {
                playerElement.classList.add("list-group-item-success");
            }
        }
    }
}

function handleClose(msg) {
    let player = document.getElementById(msg.request.playerID);
    if (player) {
        document.getElementById("players").removeChild(player);
    }
}

const joinButton = document.getElementById("join");
joinButton.addEventListener("click", () => {
    thisPlayerID = document.getElementById("name").value;
    if (thisPlayerID == "") {
        return;
    }
    let request = { command: "join", gameID: gameID, playerID: thisPlayerID };
    ws.send(JSON.stringify(request));
    joinButton.disabled = true;
});
document.getElementById("name").addEventListener("keyup", (event) => {
    if (event.key === "Enter") {
        joinButton.click();
    }
});

function handleJoin(msg) {
    console.log("Join");
    const player = document.createElement("li");
    player.classList.add("list-group-item");
    player.id = msg.request.playerID;
    player.innerText = msg.request.playerID;
    if (msg.request.ready) {
        player.classList.add("list-group-item-success");
    }
    if (msg.request.playerID == thisPlayerID) {
        document.getElementById("name-input-group").classList.add("d-none");
        const nameDisplay = document.getElementById("name-display");
        nameDisplay.innerText = `${thisPlayerID}`;
        readyButton.classList.remove("d-none");
        readyButton.focus();
        nameDisplay.classList.remove("d-none");
        document.getElementById("greet").classList.remove("d-none");
    }
    document.getElementById("players").appendChild(player);
}

const readyButton = document.getElementById("ready");
readyButton.addEventListener("click", () => {
    let request = { command: "ready", gameID: gameID, playerID: thisPlayerID };
    readyButton.classList.add("d-none");
    startButton.classList.remove("d-none")
    startButton.focus();
    ws.send(JSON.stringify(request));
});

function handleReady(msg) {
    console.log("Ready");
    document.getElementById(msg.request.playerID).classList.add("list-group-item-success");
}

const startButton = document.getElementById("start");
startButton.addEventListener("click", () => {
    let warning = document.getElementById("warning");
    if (game.Players.length < 4) {
        warning.innerText = "You need at least four players to start the game."
        warning.classList.remove("d-none");
        return;
    }

    if (game.Players.some(player => !player.Ready)) {
        warning.innerText = "All players need to be ready to start the game."
        warning.classList.remove("d-none");
        return;
    }
    let request = { command: "start", gameID: gameID };
    ws.send(JSON.stringify(request));
});

function handleStart(msg) {
    console.log("Start");
    document.getElementById("setup").classList.add("d-none");
    document.getElementById("play").classList.remove("d-none");
    document.getElementById("location").innerText = msg.location;
    document.getElementById("role").innerText = msg.role;
}

document.getElementById('share').addEventListener('click', ()=>{
    if (navigator.share) {
        navigator.share({
            title: `Spyfall #${gameID}`,
            text: "Join my game of Spyfall!",
            url: window.location.href
        })
        .then(() => console.log('Successful share'))
        .catch((error) => console.log('Error sharing', error));
    } else {
        console.log('Web Share API not supported');
    }
});