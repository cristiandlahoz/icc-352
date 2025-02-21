document.addEventListener("DOMContentLoaded", function () {
    const deleteButton = document.getElementById("deleteButton");
    const articleId = deleteButton.dataset.articleId;

    deleteButton.addEventListener("click", function () {
        deleteArticle(articleId);
    });
});

function deleteArticle(articleId) {
    if (confirm("Are you sure you want to delete this article?")) {
        fetch(`/articles/${articleId}`, {
            method: 'DELETE',
        }).then(response => {
            if (response.ok) {
                window.location.href = "/";
            } else {
                alert("Error deleting article");
            }
        });
    }
}
