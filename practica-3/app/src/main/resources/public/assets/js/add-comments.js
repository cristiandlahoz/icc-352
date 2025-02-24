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
            const newComment = await response.json();
            appendComment(newComment);
            commentBox.value = "";
        } else {
            alert("Error al enviar el comentario");
        }
    });

    function appendComment(comment) {
        let commentsList = document.querySelector(".comments-list");

        // Si el contenedor de comentarios no existe, créalo
        if (!commentsList) {
            commentsList = document.createElement("div");
            commentsList.classList.add("comments-list");
            document.querySelector(".comments-section").appendChild(commentsList);
        }

        const newCommentDiv = document.createElement("div");
        newCommentDiv.classList.add("comment-box");

        newCommentDiv.innerHTML = `
            <div class="d-flex gap-3">
                <img src="https://randomuser.me/api/portraits/men/34.jpg" alt="User Avatar" class="user-avatar">
                <div class="flex-grow-1">
                    <div class="d-flex justify-content-between align-items-center mb-2">
                        <h6 class="mb-0">${comment.username}</h6>
                        <span class="comment-time">2 hours ago</span>
                    </div>
                    <p class="mb-2">${comment.comment}</p>
                    <div class="comment-actions"></div>
                </div>
            </div>
        `;

        commentsList.prepend(newCommentDiv);
    }
});