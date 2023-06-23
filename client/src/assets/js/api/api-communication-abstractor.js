"use strict";

function fetchFromServer(URI, httpVerb, requestBody){
    const options = constructOptions(httpVerb, requestBody);

    return fetch(`${API_URL}${URI}`, options)
        .then((response) => {
            if (!response.ok) {
                throw response;
            }
            return response.json();
        })
        .then((jsonResponseToParse) => {
            return jsonResponseToParse;
        });
}

function constructOptions(httpVerb, requestBody){
    const options = {
        method: httpVerb,
        headers: {
            "Content-Type": "application/json"
        }
    };

    if (requestBody) {
        options.body = JSON.stringify(requestBody);
    }

    return options;
}