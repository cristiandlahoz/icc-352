document.addEventListener("DOMContentLoaded", function () {
    const commentForm = document.getElementById("commentForm");
    const articleId = commentForm.dataset.articleId;
    const logged = commentForm.dataset.logged === "true";
    const user = commentForm.dataset.user;

    commentForm.addEventListener("submit", async function (event) {
        event.preventDefault();

        const commentBox = document.getElementById("commentBox");
        const commentText = commentBox.value.trim();

        if (!logged) {
            let loginModal = new bootstrap.Modal(document.getElementById('loginModal'));
            loginModal.show();
            return;
        }

        if (commentText === "") {
            alert("El comentario no puede estar vacío");
            return;
        }

        const response = await fetch("/comments", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: new URLSearchParams({ articleId: articleId, comment: commentText, author: user })
        });

        if (response.ok) {
            // Guardar la posición del scroll antes de recargar la página
            localStorage.setItem('scrollPosition', window.scrollY);

            // Recargar la página
            window.location.reload();
        } else {
            alert("Error al enviar el comentario");
        }
    });

    // Restaurar la posición del scroll al recargar la página
    window.onload = () => {
        const scrollPosition = localStorage.getItem('scrollPosition');
        if (scrollPosition) {
            window.scrollTo(0, parseInt(scrollPosition, 10));
            localStorage.removeItem('scrollPosition'); // Limpiar el almacenamiento una vez restaurado
        }
    };
});