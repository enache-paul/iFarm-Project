"use strict"

document.addEventListener("DOMContentLoaded", init);

function init() {
    loadGardenApiCalls();
    gardenListeners();
    selectZone();
    initScan();
}

