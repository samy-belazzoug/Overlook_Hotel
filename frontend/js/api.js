const API_URL = "http://localhost:8080/api";

// GET helper
async function apiGet(path) {
    const response = await fetch(`${API_URL}${path}`);
    if (!response.ok) throw new Error(`Erreur GET ${path}`);
    return await response.json();
}

// POST helper
async function apiPost(path, data) {
    const response = await fetch(`${API_URL}${path}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
    });
    if (!response.ok) throw new Error(`Erreur POST ${path}`);
    return await response.json();
}


async function loadEvents() {
    try {
        const events = await apiGet(`/evenements`);
        renderEvents(events);
    } catch (error) {
        console.error(error);
        document.getElementById("events").innerHTML = "<p>Impossible de charger les événements.</p>";
    }
}

function renderEvents(events) {
    const container = document.getElementById("events");
    container.innerHTML = "";

    if (events.length === 0) {
        container.innerHTML = "<p>Aucun événement disponible.</p>";
        return;
    }

    events.forEach(ev => {
        const div = document.createElement("div");
        div.className = "event";

        div.innerHTML = `
            <h3>${ev.titre}</h3>
            <p>${ev.description}</p>
            <p><b>Date:</b> ${ev.date}</p>
            <p><b>Capacité:</b> ${ev.capacite}</p>
            <button onclick="reserveEvent(${ev.id})">Réserver</button>
        `;
        container.appendChild(div);
    });
}

async function reserveEvent(eventId) {
    const clientId = prompt("Votre ID client ?");
    if (!clientId) return;

    try {
        await apiPost(`/evenements/${eventId}/reserve?clientId=${clientId}`, {});
        alert("Réservation réussie !");
        loadEvents();
    } catch (error) {
        alert("Erreur: " + error.message);
    }
}

// Charger les événements au démarrage
loadEvents();
