package com.example.labwork_8

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RecipeDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        val tvRecipeName: TextView = findViewById(R.id.tvRecipeName)
        val tvIngredients: TextView = findViewById(R.id.tvIngredients)
        val tvInstructions: TextView = findViewById(R.id.tvInstructions)

        val recipeName = intent.getStringExtra("recipeName") ?: "No Name"
        val ingredients = intent.getStringExtra("ingredients") ?: "No Ingredients"
        val instructions = intent.getStringExtra("instructions") ?: "No Instructions"

        tvRecipeName.text = recipeName
        tvIngredients.text = ingredients
        tvInstructions.text = instructions
    }
}