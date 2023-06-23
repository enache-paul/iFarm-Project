"use strict";

let URL_CATEGORIES = "https://www.themealdb.com/api/json/v1/1/categories.php";
let URL_RECIPES = "https://www.themealdb.com/api/json/v1/1/filter.php?c=";
let recipes = [];
const INGREDIENTS_ARR = ["Chicken Breast", "Broccoli", "Oregano", "Zucchini", "Iceberg Salad", "Avocado", "Eggplant", "Green Beans", "Salt", "Jicama", "Mushroom", "Onion", "Pepper", "Tomato", "Pepper", "Cucumber", "Apricot", "Apple", "Pear", "Paprika"];

function saveArrayOfRecipes(mealsArrObjects) {
    recipes = [];

    mealsArrObjects.forEach(meal => {
        recipes.push(meal.strMeal);
    });
    return recipes;
}

function filterList(list, query) {
    return list.filter(recipe => {
        return (recipe.toLowerCase().includes(query));
    });
}

function insertArrOfFilteredMeals(filteredMeals) {
    const $ul = document.querySelector("ul#meals");
    $ul.innerHTML = "";

    filteredMeals.forEach(filteredMeal => {
        $ul.insertAdjacentHTML("beforeend", `<li>${filteredMeal}</li>`);
    });
}

function buildRandomIngredients(e) {
    const $li = document.querySelectorAll(".ingredients-list li span");

    $li.forEach(li => li.innerHTML = shuffle(INGREDIENTS_ARR)[0]);
    handleMissingEventClassToggle(e)
}

function shuffle(array) {
    let currentIndex = array.length,  randomIndex;

    while (currentIndex !== 0) {

        randomIndex = Math.floor(Math.random() * currentIndex);
        currentIndex--;

        [array[currentIndex], array[randomIndex]] = [array[randomIndex], array[currentIndex]];
    }

    return array;
}
