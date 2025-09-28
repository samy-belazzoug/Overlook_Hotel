// Optionally set auth header if needed
const AUTH_HEADERS = {
    // "Authorization": "Basic " + btoa("gestionnaire@example.com:secret123")
};

async function loadDashboard() {
    try {
        const response = await fetch(`${API_URL}/dashboard`, { headers: AUTH_HEADERS });
        if (!response.ok) {
            const bodyText = await response.text().catch(() => "");
            throw new Error(`HTTP ${response.status} ${bodyText}`);
        }
        const stats = await response.json();
        renderStats(stats);
    } catch (error) {
        console.error("Dashboard load error:", error);
        document.getElementById("stats").innerHTML = `<p>Impossible de charger le tableau de bord. ${error?.message ?? ""}</p>`;
    }
}

function renderStats(stats) {
    const confirmed = stats.confirmedReservations ?? 0;
    const cancelled = stats.cancelledReservations ?? 0;
    const revenus = stats.revenus ?? 0;
    const taux = stats.tauxOccupation ?? 0; // 0..1
    const tauxPct = (taux * 100).toFixed(2);
    document.getElementById("stats").innerHTML = `
        <p><b>Confirmées:</b> ${confirmed}</p>
        <p><b>Annulées:</b> ${cancelled}</p>
        <p><b>Taux d'occupation:</b> ${tauxPct}%</p>
        <p><b>Revenus:</b> ${revenus} €</p>
    `;
}

// Charger au lancement
loadDashboard();
