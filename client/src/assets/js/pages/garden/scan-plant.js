"use strict";

async function setStatsMyLastScan() {
    const plantId = parseInt(localStorage.getItem("selectedOwnedPlantId"));
    const scansArr = await scansByPlantId(plantId);

    if (scansArr.length > 0) {
        const lastScan = scansArr.slice(-1);
        const unixTine = lastScan[0].scanTime;
        const date = new Date(unixTine).toLocaleDateString("be-NL");
        const time = new Date(unixTine).toLocaleTimeString("be-NL");

        document.querySelector("#yield").innerHTML = lastScan[0].expectedYield;
        document.querySelector("#growth").innerHTML = lastScan[0].growth;
        document.querySelector("#health").innerHTML = lastScan[0].health;
        document.querySelector("#time").innerHTML = date + " " + time;

        if (lastScan[0].growth >= 100) showHarvestButton();
    } else {
        document.querySelector("#yield").innerHTML = "0";
        document.querySelector("#growth").innerHTML = "0";
        document.querySelector("#health").innerHTML = "0";
        document.querySelector("#time").innerHTML = "No scans found";
    }
}

function removeIfExistsHarvestButton() {
    if (document.querySelector("#chart-placeholder ul li button") !== undefined && document.querySelector("#chart-placeholder ul li button") !== null) {
        document.querySelector("#chart-placeholder ul li button").closest('li').remove();
    }
}

function scansByPlantId(plantId) {
    return getGardenById(getGardenId())
        .then(data => data.plants)
        .then(plantsArr => {
            return findPlantInArrayById(plantsArr, plantId).scans;
        });
}

function findPlantInArrayById(plantsArr, plantId) {
    let foundPlant = null;
    plantsArr.forEach(plant => {
        if (plant.id === plantId) {
            foundPlant = plant;
        }
    });

    return foundPlant;
}

function showHarvestButton() {
    if (document.querySelector("#chart-placeholder ul li button") !== undefined && document.querySelector("#chart-placeholder ul li button") !== null) {
        document.querySelector("#chart-placeholder ul li button").closest('li').remove();
    }

    const $li = document.createElement("li");
    const $button = document.createElement("button");
    $li.appendChild($button);
    $button.innerText = "Harvest";
    $button.addEventListener("click", harvest);
    document.querySelector("#chart-placeholder ul").appendChild($li);
}

function harvest() {
    harvestPlant(localStorage.getItem("selectedOwnedPlantId"))
        .then(() => loadOwnedPlantsFromGarden())
        .catch(() => loadOwnedPlantsFromGarden());
    window.alert("Plant has been removed from your plants list");
    removeIfExistsHarvestButton();
}
