const routeId = document.getElementById('routeId').value
const commentCtnr = document.getElementById('commentCtnr');
commentCtnr.addEventListener('click', onClickComment);

const csrfHeaderName = document.querySelector('[name="_csrf_header"]').content
const csrfHeaderValue = document.querySelector('[name="_csrf"]').content

const commentForm = document.getElementById('commentForm');
commentForm.addEventListener('submit', commentSubmitHandler);

const allCommentsUrl = `http://localhost:8080/api/${routeId}/comments`;

displayComments(allCommentsUrl);

async function onClickComment(event) {
    if (event.target.name === 'editBtn') {
        let commentId = event.target.parentNode.id;
        commentId = commentId.substring(commentId.length - 1);
        toggleComment(commentId);
    } else if (event.target.id === 'confirmBtn') {
        event.preventDefault();

        const originalComment = event.target.parentNode.parentNode.parentNode.querySelector('p').textContent;

        const newComment = event.target.parentNode.parentNode.querySelector('textarea').value;

        let commentId = event.target.parentNode.parentNode.id;
        commentId = commentId.substring(commentId.length - 1);

        if (originalComment !== newComment) {

            try {
                await commentEditHandler(commentId);
                document.getElementById('comment-' + commentId).classList.remove('is-invalid');
                await displayComments(allCommentsUrl);
            } catch (error) {
                let elementWithError = document.getElementById('comment-' + commentId);

                if (elementWithError) {
                    elementWithError.classList.add("is-invalid")
                }
            }
        } else {
            document.getElementById('comment-' + commentId).classList.remove('is-invalid');
            await displayComments(allCommentsUrl);
        }
    } else if (event.target.id === 'deleteBtn') {
        event.preventDefault();
        let commentId = event.target.parentNode.parentNode.id;
        commentId = commentId.substring(commentId.length - 1);
        await commentDeleteHandler(commentId);
    }
}

async function commentEditHandler(commentId) {
    const form = event.target.parentNode.parentNode;
    const formData = new FormData(form);

    const url = `/api/${routeId}/comments/${commentId}/edit`;

    const fetchOptions = {
        method: 'PATCH',
        headers: {
            [csrfHeaderName] : [csrfHeaderValue],
            "Content-Type" : "application/json",
            "Accept" : "application/json"
        }
    };

    await postFormDataAsJson({url, formData, fetchOptions});
}

async function commentDeleteHandler(commentId) {
    const fetchOptions = {
        method: 'DELETE',
        headers: {
            [csrfHeaderName] : [csrfHeaderValue],
            "Content-Type" : "application/json",
            "Accept" : "application/json"
        }
    };

    await fetch(`/api/${routeId}/comments/${commentId}/delete`, fetchOptions);
    await displayComments(allCommentsUrl);
}

async function commentSubmitHandler(event) {
    event.preventDefault();

    const form = event.currentTarget;
    const formData = new FormData(form);
    const url = form.action;

    const fetchOptions = {
        method: 'POST',
        headers: {
            [csrfHeaderName] : [csrfHeaderValue],
            "Content-Type" : "application/json",
            "Accept" : "application/json"
        }
    };

    try {
        await postFormDataAsJson({url, formData, fetchOptions});
        document.getElementById('message').classList.remove('is-invalid');
        form.reset();
        await displayComments(allCommentsUrl);
    } catch (error) {
        let errorObj = JSON.parse(error.message);
        if(errorObj.fieldWithErrors) {
            errorObj.fieldWithErrors.forEach(e => {
                let elementWithError = document.getElementById(e);

                if (elementWithError) {
                    elementWithError.classList.add("is-invalid")
                }
            })
        }
    }
}

async function postFormDataAsJson({url, formData, fetchOptions}) {
    const plainFormData = Object.fromEntries(formData.entries())
    fetchOptions.body = JSON.stringify(plainFormData);

    const response = await fetch(url, fetchOptions);

    if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(errorMessage);
    }

    return response.json();
}

async function displayComments(url) {
    commentCtnr.innerHTML = "";

    fetch(url)
        .then(response => response.json())
        .then(data => {
            for (let comment of data) {
                commentCtnr.prepend(asComment(comment));
            }
        })
}

function toggleComment(id) {
    const form = document.getElementById('commentEditForm-' + id);
    const cardContent = form.parentNode.querySelector('p');
    let editBtn = document.getElementById('editBtn-' + id);

    if (form.style.display === "none") {
        cardContent.style.display = 'none';
        form.style.display = "block";
        editBtn.style.display = 'none';
        form.querySelector('textarea').value = cardContent.textContent;
        commentCtnr.querySelectorAll('button').forEach(btn => btn.disabled = true);
        form.querySelector('div').querySelectorAll('button').forEach(btn => btn.disabled = false);
    } else {
        cardContent.style.display = 'block';
        form.style.display = "none";
        editBtn.style.display = 'block';
    }
}

function asComment(c) {
    const commentCard = document.createElement('div');
    commentCard.id = 'commentCard-' + c.commentId;

    let created = c.created.substring(0, 16).replace('T', ' ');

    let cardInfo = document.createElement('h4');
    cardInfo.textContent = `Created by: (${c.user}) Date: (${created})`;

    let cardContent = document.createElement('p');
    cardContent.id = 'content';
    cardContent.textContent = c.message;

    commentCard.appendChild(cardInfo);
    commentCard.appendChild(cardContent);

    if (c.canEdit) {
        let formTextArea = document.createElement('textarea');
        formTextArea.name = "message";
        formTextArea.id = 'comment-' + c.commentId;
        formTextArea.cols = 30;
        formTextArea.rows = 2;
        formTextArea.classList.add('form-control');
        formTextArea.style.backgroundColor = "white";

        const confirmBtn = document.createElement('button');
        confirmBtn.id = "confirmBtn";
        confirmBtn.classList.add('btn');
        confirmBtn.style.backgroundColor = "DodgerBlue";
        confirmBtn.textContent = "Confirm";

        const deleteBtn = document.createElement('button');
        deleteBtn.id = "deleteBtn";
        deleteBtn.classList.add('btn');
        deleteBtn.style.backgroundColor = "FireBrick";
        deleteBtn.textContent = "Delete";

        const errMessage = document.createElement('small');
        errMessage.id = "messageError";
        errMessage.textContent = 'Message should be at least 5 characters.';
        errMessage.classList.add("invalid-feedback");
        errMessage.classList.add("alert");
        errMessage.classList.add("alert-danger");

        const btnCtnr = document.createElement('div');
        btnCtnr.id = "btnCtnr";
        btnCtnr.classList.add("form-group");
        btnCtnr.appendChild(confirmBtn);
        btnCtnr.appendChild(deleteBtn);

        let form = document.createElement('form');
        form.id = 'commentEditForm-' + c.commentId;
        form.classList.add("commentEdit");
        form.style.display = "none";
        form.appendChild(formTextArea);
        form.appendChild(errMessage);
        form.appendChild(btnCtnr);

        const editBtn = document.createElement('button');
        editBtn.id = 'editBtn-' + c.commentId;
        editBtn.name = "editBtn";
        editBtn.classList.add("btn");
        editBtn.textContent = 'Edit';

        commentCard.appendChild(form);
        commentCard.appendChild(editBtn);
    }

    const br = document.createElement('br');
    commentCard.appendChild(br);

    return commentCard;
}