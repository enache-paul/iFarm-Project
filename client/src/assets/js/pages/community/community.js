"use strict"

document.addEventListener("DOMContentLoaded", init);

function init() {
    document.querySelector("label").innerText = localStorage.getItem("gardenName");
    document.querySelector("#addPost input[type=submit]").addEventListener("click", newPost);
    getRecentForumPosts().then(posts => {
        fillPosts(posts);
    });
    Notification.requestPermission();

    openSocket();
}

function newPost(e) {
    e.preventDefault();
    const text = e.target.closest("form").querySelector("input[type]").value;
    if (text === "") {
        window.alert("Please add text to your post.");
        return;
    }

    addForumPost(text, "https://mymodernmet.com/wp/wp-content/uploads/2022/10/devil-fruit-carve-2.jpg")
        .then(post => {
            const postContainer = document.getElementById("posts");
            const clone = createPostClone(post);
            postContainer.insertBefore(clone, postContainer.firstChild);
            postContainer.firstElementChild.dataset.postId = post.id;
        }).catch(err => window.alert("creating post failed \n" + err.message));
}

function fillPosts(posts) {
    const postContainer = document.getElementById("posts");

    posts.forEach(post => {
        const clone = createPostClone(post);
        postContainer.appendChild(clone);
        postContainer.lastElementChild.dataset.postId = post.id;
    })
}

function createPostClone(post) {
    const unixTime = post.uploadTime;
    const date = new Date(unixTime).toLocaleDateString("be-NL");
    const time = new Date(unixTime).toLocaleTimeString("be-NL");

    const clone = document.getElementById("postTemplate").content.cloneNode(true);
    clone.querySelector("h2").innerText = post.garden.name;
    clone.querySelector("p").innerText = post.text;
    clone.querySelector("h3").innerText = date + " " + time;
    if (post.imgLink !== null) clone.querySelector("img").src = post.imgLink;
    else clone.querySelector("img").remove();
    clone.querySelector(".like").addEventListener("click", likePost);
    clone.querySelector(".commentButton").addEventListener("click", showComments)
    clone.querySelector(".like span").innerText = post.likes;

    return clone;
}

function likePost(e) {
    e.preventDefault();
    const likeEvent = {};
    likeEvent.type = "POST_LIKE";
    likeEvent.postId = parseInt(e.target.closest("div.forumPost").dataset.postId);

    sendToServer(likeEvent);
    getForumPostById(likeEvent.postId).then(forumPost => {
        e.target.closest("div.forumPost").querySelector(".like span").innerText = forumPost.likes;
    })
}

function postLiked(data) {
    let notification = new Notification(data.gardenName + " liked your post");
    notification.onclick = () => {
        notification.close();
        window.parent.focus();
    }
}

function commentLiked(data) {
    let notification = new Notification(data.gardenName + " liked your comment");
    notification.onclick = () => {
        notification.close();
        window.parent.focus();
    }
}

function showComments(e) {
    e.preventDefault();
    const commentContainer = e.target.closest("div.forumPost").querySelector(".comments");
    if (!commentContainer.classList.contains("hidden")) return clearCommentSection(commentContainer);


    getForumPostById(e.target.closest("div.forumPost").dataset.postId).then(post => {
        addCommentInput(commentContainer);
        createComments(commentContainer, post);
    });
}

function clearCommentSection(commentContainer) {
    commentContainer.classList.add("hidden");
    commentContainer.querySelectorAll("div").forEach(child => {
        child.remove();
    })
}

function addCommentInput(commentContainer) {
    const clone = document.getElementById("addCommentTemplate").content.cloneNode(true);
    clone.querySelector("input[type=submit]").addEventListener("click", newComment);
    commentContainer.appendChild(clone);

}

function newComment(e) {
    e.preventDefault();
    const text = e.target.closest("div").querySelector("input[type]").value;
    const postId = parseInt(e.target.closest("div.forumPost").dataset.postId);

    if (text === "") {
        window.alert("Please write valid comment.");
        return;
    }

    addComment(postId, text).then(comment => {
        const clone = document.getElementById("commentTemplate").content.cloneNode(true);
        clone.querySelector("h3").innerText = comment.garden.name;
        clone.querySelector("p").innerText = comment.text;
        clone.querySelector(".like span").innerText = comment.likes;
        clone.querySelector(".like").addEventListener("click", likeComment);


        const commentContainer = e.target.closest("div.forumPost").querySelector(".comments");
        commentContainer.appendChild(clone);
        commentContainer.querySelector("div.comment:last-child").dataset.commentId = comment.id;
        e.target.closest("div").querySelector("input[type]").value = null;
    });
}

function createComments(commentContainer, post) {
    const template = document.getElementById("commentTemplate");

    post.comments.forEach(comment => {
        const clone = template.content.cloneNode(true);
        clone.querySelector("h3").innerText = comment.garden.name;
        clone.querySelector("p").innerText = comment.text;
        clone.querySelector(".like span").innerText = comment.likes;
        clone.querySelector(".like").addEventListener("click", likeComment);

        commentContainer.appendChild(clone);
        commentContainer.querySelector("div.comment:last-child").dataset.commentId = comment.id;
    })

    commentContainer.classList.remove("hidden");
}

function likeComment(e) {
    e.preventDefault();
    const likeEvent = {};
    likeEvent.type = "COMMENT_LIKE";
    likeEvent.commentId = parseInt(e.target.closest("div.comment").dataset.commentId);

    sendToServer(likeEvent);
    const postId = parseInt(e.target.closest("div.forumPost").dataset.postId);
    getForumPostById(postId).then(forumPost => {
        e.target.closest("div.comment").querySelector(".like span")
            .innerText = forumPost.comments.find(comment => comment.id === likeEvent.commentId).likes;
    })
}
