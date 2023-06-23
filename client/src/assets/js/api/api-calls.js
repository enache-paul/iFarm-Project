"use strict";

const API_URL = "https://project-ii.ti.howest.be/mars-23/api/";
const GET = "GET";
const POST = "POST";
const PUT = "PUT";

document.addEventListener("DOMContentLoaded", test);

function test() {
    testApiCall(getAllPlants());                    // this is an example
    console.log("Here are your api calls from testApiCall");
}

function testApiCall(apiCallFunction) {
    apiCallFunction.then(fulfilledResponse => console.log(fulfilledResponse));
    // use testApiCall to see how objects/array-of-objects are returned
    // you can also execute this is the browser console
}

// ownedPlants
function addPlantToGarden(plantId) {
    const BODY = {
        gardenId: getGardenId(),
        plantId: plantId
    }
    return fetchFromServer(`gardens/${getGardenId()}/ownedPlants`, POST, BODY);
}
function addScanToPlant(plantId) {
    const BODY = {
        ownedPlantId: plantId
    }
    return fetchFromServer(`gardens/${getGardenId()}/scans`, POST, BODY);
}
function harvestPlant(plantId) {
    const BODY = {
        ownedPlantId: parseInt(plantId)
    }
    return fetchFromServer(`gardens/${getGardenId()}/ownedPlants`, PUT, BODY);
}

//forumPosts
function addForumPost(text, imageLink) {
    const BODY = {
        gardenId: getGardenId(),
        text: text,
        imgLink: imageLink
    }
    return fetchFromServer("forumposts", POST, BODY);
}
function getRecentForumPosts() {
    return fetchFromServer("forumposts", GET);
}
function getForumPostById(id) {
    return fetchFromServer( `forumposts/${id}`, GET);
}
function addComment(postId, text) {
    const BODY = {
        postId: postId,
        gardenId: getGardenId(),
        text: text,
    }
    return fetchFromServer("forumposts/comments", POST, BODY);
}

//gardens
function addGarden(name, tier) {
    const BODY = {
        name: name,
        tier: tier
    }
    return fetchFromServer("gardens", POST, BODY);
}
function getGardenById(id) {
    return fetchFromServer( `gardens/${id}`, GET);
}
function updateTier(tier) {
    const BODY = {
        tier: tier
    }
    return fetchFromServer(`gardens/${getGardenId()}`, PUT, BODY);
}

//plants
function getPlantById(id) {
    return fetchFromServer( `plants/${id}`, GET);
}
function getAllPlants() {
    return fetchFromServer("plants", GET);
}
function addPlant(name, imgLink){
    const BODY = {
        name: name,
        imgLink : imgLink
    };
    return fetchFromServer("plants", POST, BODY);
}

//robots
function addOwnedRobot(robotName, description) {
    const BODY = {
        robotName: robotName,
        description: description
    }

    return fetchFromServer(`/robots/${getGardenId()}`, POST, BODY);
}

