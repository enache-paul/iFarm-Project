"use strict";

function searchBarEventListener() {
    document.querySelector("#searchBar").addEventListener("keyup", handleSearchQuery);
}

function handleSearchQuery(e) {
    e.preventDefault();
    const searchQuery = e.target.value.toLowerCase();

    insertArrOfFilteredMeals(filterList(recipes, searchQuery));
    mealsEventListeners();
}
