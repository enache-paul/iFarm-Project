"use strict";

function removeClassFromAllElements(classNameWithoutDot) {
    document.querySelectorAll("." + classNameWithoutDot).forEach(el => {
        el.classList.remove(classNameWithoutDot);
    });
}

function addClassToElement(element, classNameWithoutDot) {
    element.classList.add(classNameWithoutDot);
}
