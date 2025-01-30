package com.example.labwork_8

data class Recipe(
    val id: String = "",
    val name: String = "",
    val ingredients: String = "",
    val instructions: String = "",
    val imageUrl: String = ""
) {
    // Пустой конструктор нужен для Firebase
    constructor() : this("", "", "", "", "")
}
