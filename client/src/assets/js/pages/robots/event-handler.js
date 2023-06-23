"use strict";

function addRobot() {
    buttonsEventListeners();
    document.querySelector("#create-robot").addEventListener("submit", processInfoNewRobot);
}

function buttonsEventListeners() {
    emptyRobotSlotEvents();
    filledRobotSlotEvents();
}

function filledRobotSlotEvents() {
    const $buttons = document.querySelectorAll(".owned-robot");
    $buttons.forEach($btn => {
        $btn.addEventListener("click", robotOwnedInfo);
    });
}

function robotOwnedInfo(e) {
    e.preventDefault();

    displayAndHide("#statistics", "#add-robot");
    robotData(e);
    handleMissingEventClassToggle(e);
}

function displayAndHide(selector1, selector2) {
    const $selector1 = document.querySelector(selector1);
    const $selector2 = document.querySelector(selector2);
    $selector1.classList.remove("hidden");
    $selector2.classList.add("hidden");
}

function robotData(e) {
    document.querySelectorAll(".robot-name").forEach(el => el.innerHTML = e.target.dataset.robotname);
    document.querySelector(".robot-battery").innerHTML = e.target.dataset.batterylevel;
    document.querySelector(".robot-description").innerHTML = e.target.dataset.description;
}

function emptyRobotSlotEvents() {
    const $buttons = document.querySelectorAll(".empty");
    $buttons.forEach($btn => {
        $btn.addEventListener("click", navigate);
    });
}

function navigate(e) {
    e.preventDefault();
    hideAsides("#add-robot");
    handleMissingEventClassToggle(e);
}

function hideAsides($target) {
    const $asides = document.querySelectorAll("aside")
    $asides.forEach(aside => {
        aside.classList.add("hidden");
        document.querySelector($target).classList.remove("hidden");
    });
}
