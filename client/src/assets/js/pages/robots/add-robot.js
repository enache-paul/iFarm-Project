"use strict";

function processInfoNewRobot(e) {
    e.preventDefault();

    const robotName = document.querySelector("#robot-name").value;
    const description = document.querySelector("#robot-description").value;

    if (checkTier())
        addOwnedRobot(robotName, description).then(loadRobots);
    else {
        window.alert("Upgrade to Premium XL to access the robots feature!");
        window.location.href = "index.html";
    }
}
