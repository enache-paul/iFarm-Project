"use strict";

function gardenListeners() {
    cropsInGarden();
    document.querySelector("#plants-overview #back1").addEventListener("click", goBack1);
    document.querySelector("#plant-summary #back2").addEventListener("click", goBack2);
    document.querySelector("#select-zone #back3").addEventListener("click", goBack3);
    document.querySelector("#plant-summary #next1").addEventListener("click", goNext1);
    document.querySelector("#next2").addEventListener("click", goNext2);
}

function selectZone(){

    const $areas = document.querySelectorAll(".zone-container div");
    $areas.forEach($area => {
        $area.addEventListener("click", zoneSelected);
    });

}

const $selectedArea = [];
function zoneSelected(e){
    e.preventDefault();

    const $area = e.target;
    $selectedArea.push($area);

    $area.style.backgroundColor = "red";
    calculateFertilizer();
    
    $selectedArea.forEach($selection => {
        $selection.addEventListener("click", deSelectZone);
    });

    document.querySelector("#fertilizer-container button").addEventListener("click", purchaseConfirmation);
}

function deSelectZone(e){
    e.preventDefault();

    const $area = e.target;
    $selectedArea.pop($area);

    $area.style.backgroundColor = "black";
    calculateFertilizer();
}

function calculateFertilizer(){
    const $surface = document.querySelector("span#surface");
    const $quantity = document.querySelector("span#quantity");

    const $price = document.querySelector("span#price");
    $surface.innerHTML = $selectedArea.length;
    $quantity.innerHTML = Math.round($surface.innerHTML * 1.2,4);
    $price.innerHTML = Math.round($surface.innerHTML * 15, 4)
}

function cropsInGarden(){
    const $crops = document.querySelectorAll("#vegetables-in-garden li");
    
    $crops.forEach($crop => {
        if ($crop.className !== "add-crop"){
            $crop.addEventListener("click", displayStatistics);
        } 
        else{
            $crop.addEventListener("click", displayLibrary);
        }       
    });
}

function displayStatisticsFirstOwnedPlant() {
    const $crop = document.querySelector("#vegetables-in-garden ul").firstElementChild;

    if ($crop.className !== "add-crop") {
        displayStatisticsOnLoad($crop);
    }
    else
        displayLibrary();

    highlightFirstElement($crop);
}

function highlightFirstElement(element) {
    element.classList.add("selected");
}

function displayStatisticsOnLoad(plant) {
    hideAsides("#statistics");

    const name = plant.innerText + "s";
    const $h4 = document.querySelector("#statistics h4");
    const $li = document.querySelector("#vegetables-in-garden ul").firstElementChild.closest("li").dataset.ownedplantid;

    localStorage.setItem("selectedOwnedPlantId", $li);
    $h4.innerHTML = "";
    $h4.innerHTML = name;

    lineChartInGarden();
    setStatsMyLastScan();
    randomizeWarnings(name);
}

function displayStatistics(e) {
    e.preventDefault();

    localStorage.setItem("selectedOwnedPlantId", e.target.closest("li").dataset.ownedplantid);

    hideAsides("#statistics");
    const plantName = e.target.innerText + "s";

    const $h4 = document.querySelector("#statistics h4");
    $h4.innerHTML = "";
    $h4.innerHTML = plantName;

    handleMissingEventClassToggle(e);
    lineChartInGarden();
    setStatsMyLastScan();
    randomizeWarnings(plantName); 
}

function addPlantIdCLassToScanButton() {
    document.querySelector(".scan-button-container").classList.add(e.target.classList.item(1));
    document.querySelector(".scan-button").classList.add(e.target.classList.item(1));
}

function randomizeStatistics() {
    const $statistics = document.querySelectorAll("#statistics ul li span");
    $statistics.forEach(stat => {
        stat.textContent = Math.floor(Math.random() * (94 - 50 + 1)) + 50;
    });
}

function randomizeWarnings(plantName){
    const $waterWarning = document.querySelector("#warnings li.water");
    $waterWarning.innerHTML = `${plantName} need ${Math.floor(Math.random() * 5.00) + 1}l more water!`;

    const $fertilizerWarning = document.querySelector("#warnings li.fertilizer");
    $fertilizerWarning.innerHTML = `${plantName} need ${Math.floor(Math.random() * 999)}g more fertilizer`;
}

function displayLibrary(e){
    hideAsides("aside#plants-overview");
    handleMissingEventClassToggle(e);

    const $plants = document.querySelectorAll(".plants");
    $plants.forEach(plant => {
        plant.addEventListener("click", displayOverview);
    });
}

function hideAsides($target) {
    const $asides = document.querySelectorAll("aside")
    $asides.forEach(aside => {
        aside.classList.add("hidden");
        document.querySelector($target).classList.remove("hidden");
    });
}

function handleMissingEventClassToggle(e) {
    if (e !== undefined) {
        removeClassFromAllElements("selected");
        addClassToElement(e.target, "selected");
    }
}

function displayOverview(e){
    goNext("aside#plants-overview", "aside#plant-summary");

    saveSelectedPlantToStorage(e)

    const $h4 = document.querySelector("#summary h4");
    let plantName = e.target.title

    $h4.innerHTML = plantName;

    const $html =   `
        <li>Planting the ${plantName}s in the garden</li>
        <li>Fertilizing ${plantName}s</li>
        <li>Managing ${plantName}s pests and diseases</li>
        <li>harvesting ${plantName}s</li>
                    `;
    
                    const $ol = document.querySelector("#summary ol");
    $ol.innerHTML = "";
    $ol.insertAdjacentHTML("beforeend", $html);
}

function saveSelectedPlantToStorage(e) {
    localStorage.setItem("selectedPlant", e.target.closest("div").dataset.plantid);
}

function goNext($currentPage, $nextPage){
    document.querySelector($currentPage).classList.add("hidden")
    document.querySelector($nextPage).classList.remove("hidden");
}

function goBack1(){
    hideAsides("#statistics");
}

function goBack2(){
    hideAsides("#plants-overview");
}

function goBack3(){
    hideAsides("#plant-summary");
}

function goNext1(){
    hideAsides("#select-zone");
}

function goNext2() {
    addPlantToGardenOnPage(parseInt(localStorage.getItem("selectedPlant")));
}

function purchaseConfirmation(){
    const $target = document.querySelector("#fertilizer-container");
    $target.innerHTML = "";

    const $html = `<p>Purchase Success</p>`;
    $target.insertAdjacentHTML("beforeend", $html);
}
