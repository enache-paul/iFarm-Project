const CHANNEL_TO_SERVER = "events.to.server";
const CHANNEL_TO_GROUP = "events.to."
const CHANNEL_TO_MARTIANS = "events.to.martians";
let CHANNEL_TO_USER = "events.to.users.";

const BASE_URL = "https://project-ii.ti.howest.be/mars-23";
const EVENTBUS_PATH = "/events";
const GARDEN_ID = 'gardenId';

let eventBus;
let sendToServer;

function openSocket() {
    eventBus = new EventBus(BASE_URL + EVENTBUS_PATH);

    sendToServer = function (message) {
        message.gardenId = getGardenId();
        console.log(message);
        eventBus.send(CHANNEL_TO_SERVER, message);
    }

    eventBus.onopen = () => {
        eventBus.registerHandler(CHANNEL_TO_MARTIANS, broadcast);
        eventBus.registerHandler(CHANNEL_TO_USER + getGardenId(), unicast);
    }

    return sendToServer;
}

function openMulticastRegister(group, func){
    eventBus.registerHandler(CHANNEL_TO_GROUP + group, func);
}

function broadcast(error, message){
    console.log("hello broadcast");
    console.log(message);
    console.log(error);
}

function unicast(error, message){
    switch (message.body.type) {
        case 'POST_LIKE':
            postLiked(message.body);
            break;
        case 'COMMENT_LIKE':
            commentLiked(message.body);
            break;
        default:
            console.log("default type ig");
            break;
    }
}
