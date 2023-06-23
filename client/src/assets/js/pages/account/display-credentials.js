"use strict";

function loadUserCredentials() {
    displayUserCredentials();
    eventListenerHandler();
}

function displayUserCredentials() {
    const $div = document.querySelector("#personal");

    getGardenById(getGardenId()).
        then(garden => {
            buildHtml(garden, $div);
            saveGardenToStorage(garden.id, garden.name, garden.tier);
            highLightCurrentTier();
    });
}

function buildHtml(garden, selector) {
    let html = "";
    selector.innerHTML = "";

    html += `<h2 class="garden-name">Your garden name: <span>${garden.name}</span></h2>
                          <p class="garden-id">Your garden ID: <span>${garden.id}</span></p>
                          <p class="garden-tier">Current tier: <span>${garden.tier}</span></p>
                          <p class="garden-plants">Owned plants: <span>${garden.plants.length}</span></p>
                          <p class="garden-robots">Robots: <span>${garden.robots.length}</span></p>`

    selector.insertAdjacentHTML("beforeend", html);
}

function saveGardenToStorage(id, name, tier) {
    localStorage.setItem("gardenId", id);
    localStorage.setItem("gardenName", name);
    localStorage.setItem("tier", tier);
}

function highLightCurrentTier() {
    const tier = localStorage.getItem("tier");
    const $li = document.querySelector(`.tiers #${tier}`);

    $li.classList.add("selected");
}

function eventListenerHandler() {
    const $lists = document.querySelectorAll(".tier");
    $lists.forEach(elementList => elementList.addEventListener("click", changeTierHandler));
}

function changeTierHandler(e) {
    e.preventDefault();
    const tier = e.target.id;

    updateTier(tier);
    removeClassListOnAllElements(".selected");
    displayUserCredentials();
}

function removeClassListOnAllElements(selector) {
    const $selectors = document.querySelectorAll(selector);
    $selectors.forEach(elementSelector => elementSelector.classList.remove("selected"));
}
