"use strict"

document.addEventListener("DOMContentLoaded", init);

function init() {
    console.log("page loaded");
    if (!getGardenId()) {
        if (location.href.split("/").slice(-1)[0] === "new-garden.html") {
            document.querySelector("input[type=submit]").addEventListener("click", createNewGarden);
        } else {
            promptNewAccount();
        }
    } else {
        getGardenById(getGardenId())
            .then(saveOwnedRobotsToStorage)
            .catch(err => {
                console.log(err);
                localStorage.clear();
                promptNewAccount();
            })
    }
}

function saveOwnedRobotsToStorage(garden) {
    localStorage.setItem("totalOwnedRobots", `${parseInt(garden.robots.length)}`)
}

function promptNewAccount() {
    window.location.href = 'new-garden.html';
}

function createNewGarden(e) {
    e.preventDefault();

    const name = e.target.closest("form").querySelector("input").value;
    if (name === "" || name.length > 100) window.alert("invalid name");

    addGarden(name, "FREE").then(garden => {
            localStorage.setItem("gardenId", garden.id);
            localStorage.setItem("gardenName", garden.name);
            window.location.href = 'index.html';
        })
        .catch(err => {
            console.log(err.message);
            window.alert("failed to create new garden");
        })
}

function getGardenId() {
    return parseInt(localStorage.getItem("gardenId"));
}
