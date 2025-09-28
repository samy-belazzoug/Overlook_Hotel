async function loadNotifications() {
  const userId = 1; // ВРЕМЕННО жёстко — потом подставим из логина
  try {
    const resp = await fetch(`http://localhost:8080/api/notifications?userId=${userId}`);
    if (!resp.ok) throw new Error("Erreur HTTP " + resp.status);

    const data = await resp.json();
    renderNotifications(data);
  } catch (err) {
    document.getElementById("notifications").innerText =
      "Impossible de charger les notifications.";
    console.error(err);
  }
}

function renderNotifications(notifs) {
  const container = document.getElementById("notifications");
  container.innerHTML = "";

  if (notifs.length === 0) {
    container.innerHTML = "<p>Aucune notification.</p>";
    return;
  }

  notifs.forEach(n => {
    const div = document.createElement("div");
    div.className = "notification";
    div.innerHTML = `
      <p><strong>[${n.type}]</strong> ${n.contenu}</p>
      <small>${new Date(n.date).toLocaleString()}</small>
    `;
    container.appendChild(div);
  });
}

loadNotifications();
