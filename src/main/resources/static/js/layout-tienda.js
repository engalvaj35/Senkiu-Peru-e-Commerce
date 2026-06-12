async function cargarContador() {
    const response = await fetch('/api/notificaciones/contador');
    const cantidad = await response.text();

    const el = document.getElementById('contadorNotificaciones');
    if (el) el.textContent = cantidad;
}

async function cargarNotificaciones() {
    const response = await fetch('/api/notificaciones');
    const notificaciones = await response.json();

    const lista = document.getElementById('listaNotificaciones');
    if (!lista) return;

    lista.innerHTML = '';

    if (notificaciones.length === 0) {
        lista.innerHTML = '<li><span class="dropdown-item-text">Sin notificaciones</span></li>';
        return;
    }

    notificaciones.forEach(n => {
        lista.innerHTML += `
            <li>
                <a class="dropdown-item" href="#" onclick="marcarLeida(${n.id}, event)">
                    <strong>${n.titulo}</strong><br>
                    <small>${n.mensaje}</small>
                </a>
            </li>
        `;
    });
}

document.addEventListener('DOMContentLoaded', () => {
    setTimeout(() => {
        cargarContador();
        cargarNotificaciones();
    }, 50);
});

async function marcarLeida(id) {

    const token = document.querySelector('meta[name="_csrf"]').getAttribute("content");
    const header = document.querySelector('meta[name="_csrf_header"]').getAttribute("content");

    await fetch(`/api/notificaciones/${id}/leer`, {
        method: 'POST',
        headers: {
            [header]: token
        }
    });

    await cargarContador();
    await cargarNotificaciones();
}