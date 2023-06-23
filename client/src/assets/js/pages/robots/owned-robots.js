"use strict";

function loadRobots() {
    getGardenById(getGardenId())
        .then(garden => garden.robots)
        .then(robots => displayOwnedRobots(robots))
        .then(addRobot)
        .then(handleNoRobotInGarden);
}

function displayOwnedRobots(robots) {
    const $ul = document.querySelector("#robots-in-garden ul");
    $ul.innerHTML = "";

    robots.forEach(robot => {
        const html = `<li class="owned-robot add-robot" data-robotName="${robot.name}" data-description="${robot.description}" data-batteryLevel="${robot.batteryLevel}">${robot.name}</li>`;
        $ul.insertAdjacentHTML("beforeend", html);
    });

    updateOwnedRobots(robots);
    insertEmptyOwnedRobots(robots, $ul);
}

function insertEmptyOwnedRobots(arrayOfOwned, element) {
    if (arrayOfOwned.length < 4) {
        for (let i = 0; i < (4 - arrayOfOwned.length); i++) {
            element.insertAdjacentHTML("beforeend", `<li class="add-robot empty">Add Robot</li>`);
        }
    }
}

function loadFirstRobot() {
    const $li = document.querySelector("#robots-in-garden ul").firstElementChild;

    document.querySelectorAll(".robot-name").forEach(el => el.innerHTML = $li.dataset.robotname);
    document.querySelector(".robot-battery").innerHTML = $li.dataset.batterylevel;
    document.querySelector(".robot-description").innerHTML = $li.dataset.description;

    removeClassFromAllElements("selected");
    addClassToElement($li, "selected");
}

function robotsInGarden() {
    const $firstChildLi = document.querySelector("#robots-in-garden ul").firstElementChild;
    return $firstChildLi.classList.contains("owned-robot");
}

function handleNoRobotInGarden() {
    if (robotsInGarden()) {
        loadFirstRobot();
        displayAndHide("#statistics", "#add-robot");
    }
    else {
        handleAddRobotOnLoadHighlight();
        displayAndHide("#add-robot", "#statistics");
    }
}

function handleAddRobotOnLoadHighlight() {
    const $li = document.querySelector("#robots-in-garden ul").firstElementChild;

    removeClassFromAllElements("selected");
    addClassToElement($li, "selected");
}

function updateOwnedRobots(robots) {
    const $ownedRobotElement = document.querySelector("#robot span");
    const $workLoadElement = document.querySelector("#workload span");

    $ownedRobotElement.innerHTML = robots.length;
    $workLoadElement.innerHTML = calculateWorkLoad(robots.length);
}

function calculateWorkLoad(robots) {
    if (robots.length === 0)
        return 0;
    else
        return  robots * 100 / 4;
}

