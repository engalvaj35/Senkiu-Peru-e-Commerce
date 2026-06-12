document.addEventListener(
    "DOMContentLoaded",
    cargarNotificaciones
);

function formatearFecha(fecha) {

    return new Date(fecha)
        .toLocaleString(
            "es-PE",
            {
                day: "2-digit",
                month: "2-digit",
                year: "numeric",
                hour: "2-digit",
                minute: "2-digit"
            }
        );
}

async function cargarNotificaciones() {

    const response =
        await fetch('/api/notificaciones');

    const lista =
        await response.json();

    const contenedor =
        document.getElementById(
            "contenedorNotificaciones"
        );

    if (lista.length === 0) {

        contenedor.innerHTML =
            `
            <div class="empty-state">

                <h2>
                    No tienes notificaciones
                </h2>

            </div>
            `;

        return;
    }

    contenedor.innerHTML = "";

    lista.forEach(n => {

        contenedor.innerHTML +=
        `
        <div class="notificacion-card ${!n.leido ? 'no-leida' : ''}">

            <div class="notificacion-header">

                <div>

                    <h3>${n.titulo}</h3>

                    <div class="fecha">
                        ${formatearFecha(n.createdAt)}
                    </div>

                </div>

            </div>

            <div class="notificacion-mensaje">

                ${n.mensaje}

            </div>

            ${
                !n.leido
                ?
                `
                <div class="notificacion-footer">
                    <button
                        class="btn-leer"
                        onclick="marcarLeida(${n.id})">

                        Marcar como leído

                    </button>
                </div>
                `
                :
                ''
            }

        </div>
        `;
    });

}

async function marcarLeida(id){

    const token =
        document.querySelector(
            'meta[name="_csrf"]'
        ).content;

    const header =
        document.querySelector(
            'meta[name="_csrf_header"]'
        ).content;

    await fetch(
        `/api/notificaciones/${id}/leer`,
        {
            method:'POST',
            headers:{
                [header]:token
            }
        }
    );

    cargarNotificaciones();
}