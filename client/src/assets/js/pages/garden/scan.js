"use strict"

function initScan() {
    document.getElementById("addScan").addEventListener("click", openScanner);
}

function openScanner(e) {
    e.preventDefault();

    if (!localStorage.getItem("selectedOwnedPlantId")) return window.alert("no plant selected");

    const clone = document.getElementById("scannerTemplate").content.cloneNode(true);
    document.querySelector("main").appendChild(clone);
    const scanner = document.getElementById("scanner");

    scanner.querySelector("button").addEventListener("click", scanClick);
    document.querySelectorAll("li").forEach($plant => {
        if ($plant.dataset.ownedplantid === localStorage.getItem("selectedOwnedPlantId")) {
            document.querySelector("#scanner h2").innerText = "Scan " + $plant.innerText;
        }
    })


    scanner.requestFullscreen();
    document.addEventListener("fullscreenchange", closeScanner);

    navigator.mediaDevices.getUserMedia({ video: true })
        .then((mediaStream) => {
            const videoElement = document.querySelector("video");
            videoElement.srcObject = mediaStream;
            videoElement.autoplay = true;
        }).catch(err => console.log(err.message))
}

function closeScanner(e) {
    if (!document.fullscreen) {
        document.getElementById("scanner").remove();
    }
}

function scanClick(e) {
    e.preventDefault();

    const scanner = e.target.closest("div").querySelector("video");
    scanner.pause();
    setTimeout(sendScan, 300);
}

async function sendScan() {
    const scansArr = await scansByPlantId(parseInt(localStorage.getItem("selectedOwnedPlantId")));

    if (checkTier(scansArr)) {
        addScanToPlant(parseInt(localStorage.getItem("selectedOwnedPlantId")))
            .then(data => {
                document.exitFullscreen();

                setStatsMyLastScan();
                lineChartInGarden();
            })
            .catch(err => window.alert("failed to scan plant"))
    } else {
        window.alert("Upgrade to Premium or PremiumXL to scan more than twice a day this plant!");
        window.location.href = "index.html";
    }
}

function checkTier(scansArr) {
    const TIER = localStorage.getItem("tier");

    if (TIER === "PREMIUM" || TIER === "PREMIUMXL")
        return true;

    return scansArr.length < 2;
}