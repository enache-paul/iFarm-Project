"use strict"

document.addEventListener("DOMContentLoaded", init);

function init() {
    fetchMealCategories();
    searchBarEventListener();
}

function fetchMealCategories() {
    fetch(URL_CATEGORIES)
        .then(res => res.json())
        .then(json => {
            const categories = json.categories;
            categories.forEach(category => {        
                displayCategory(category);
            });
            addListeners();
            loadFirstCategory();
        });
}

function displayCategory(category) {
    const $target = document.querySelector("#categories");
    const $html = `<li id=${category.strCategory}>${category.strCategory}</li>`;

    $target.insertAdjacentHTML("beforeend", $html);   
}

function addListeners() {
    const $lis = document.querySelectorAll("#categories li");
    $lis.forEach(li => {
        li.addEventListener("click", displayCategoryMeals);
    });
}

function displayCategoryMeals(e){
    e.preventDefault();

    const $target = document.querySelector("ul#meals");
    $target.innerHTML = "";
    
    const category = e.target.innerHTML;
    const URL = `${URL_RECIPES}${category}`

    handleMissingEventClassToggle(e);
    renameSearchAsideByCategory(category);
    fetchRecipes(URL);
}

function fetchRecipes(URL) {
    fetch(URL)
        .then(res => res.json())
        .then(json => {
            const meals = json.meals;
            saveArrayOfRecipes(meals);
            insertMealsAsHtml(meals)
        });
}

function insertMealsAsHtml(meals) {
    const $target = document.querySelector("ul#meals");

    meals.forEach(meal => {
        $target.insertAdjacentHTML("beforeend", `<li>${meal.strMeal}</li>`);
    });
    mealsEventListeners();
}

function mealsEventListeners() {
    const $target = document.querySelectorAll("ul#meals li");

    $target.forEach(li => {
        li.addEventListener("click", buildRandomIngredients);
    });
}

function renameSearchAsideByCategory(category) {
    const allElements = document.querySelectorAll(".category");
    allElements.forEach(el => el.innerHTML = category);
}

function loadFirstCategory() {
    const $liFirst = document.querySelector("#categories").firstElementChild;
    const category = $liFirst.innerHTML;
    const URL = `${URL_RECIPES}${category}`;

    renameSearchAsideByCategory(category);
    fetchRecipes(URL);
}
