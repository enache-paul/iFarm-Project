"use strict";

function loadGardenApiCalls() {
    loadAllPlants();
    loadOwnedPlantsFromGarden();
}

function loadAllPlants() {

    getAllPlants().then(plants => {
        const $target = document.querySelector("#library-vegetables");
        plants.forEach(plant => {
            const $html = `
                    <div class="plants" title="${plant.name}" data-plantid="${plant.id}">
                        <img src="${plant.imgLink}" alt="${plant.name}" title="${plant.name}" class="${plant.id}"> 
                        <p title="${plant.name}" class="${plant.id}">${plant.name}</p>
                    </div>
                                `;
            $target.insertAdjacentHTML("beforeend", $html);
        });
    });
}

function loadOwnedPlantsFromGarden() {
    getGardenById(localStorage.getItem("gardenId")).then(garden => {
       const arrayOfOwnedPlants = garden.plants;
       displayOwnedPlants(arrayOfOwnedPlants);
    });
}

function displayOwnedPlants(arrayOfOwnedPlants) {
    const $divOwnedVegetables = document.querySelector("#vegetables-in-garden ul");
    $divOwnedVegetables.innerHTML = "";

    arrayOfOwnedPlants.forEach(ownedPlant => {
       const $html = `<li class="${ownedPlant.name} ${ownedPlant.id}" data-ownedPlantId="${ownedPlant.id}" value="${ownedPlant.name}">${ownedPlant.name}</li>`
       $divOwnedVegetables.insertAdjacentHTML("beforeend", $html)
    });
    insertEmptyOwned(arrayOfOwnedPlants, $divOwnedVegetables);
    gardenListeners();
    displayStatisticsFirstOwnedPlant();
}

function insertEmptyOwned(arrayOfOwned, element) {
    if (arrayOfOwned.length < 4) {
        for (let i = 0; i < (4 - arrayOfOwned.length); i++) {
            element.insertAdjacentHTML("beforeend", `<li class="add-crop">Add Crop</li>`);
        }
    }
}

function addPlantToGardenOnPage(plantId) {
    addPlantToGarden(plantId)
        .then(loadOwnedPlantsFromGarden)
        .then(displayStatisticsFirstOwnedPlant)
        .then(giveABorderColor);
}

function giveABorderColor(e){
    //TODO: NOT YET IMPLEMENTED
}
