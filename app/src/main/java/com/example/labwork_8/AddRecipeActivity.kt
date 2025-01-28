package com.example.labwork_8

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class AddRecipeActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        // Initialize Firebase
        database = Firebase.database.reference.child("recipes")

        val saveButton: Button = findViewById(R.id.saveBtn)
        saveButton.setOnClickListener {
            val name = findViewById<EditText>(R.id.recipeName).text.toString()
            val ingredients = findViewById<EditText>(R.id.ingredients).text.toString()
            val instructions = findViewById<EditText>(R.id.instruction).text.toString()

            if (name.isNotBlank() && ingredients.isNotBlank() && instructions.isNotBlank()) {
                addRecipe(name, ingredients, instructions)
            } else {
                Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addRecipe(name: String, ingredients: String, instructions: String) {
        val recipeId = database.push().key
        val recipe = Recipe(name = name, ingredients = ingredients, instructions = instructions)

        recipeId?.let {
            database.child(it).setValue(recipe)
                .addOnSuccessListener { finish() }
                .addOnFailureListener { Toast.makeText(this, "Failed to add recipe.", Toast.LENGTH_SHORT).show() }
        }
    }

    fun returnBtn(v: View) {
        intent = Intent(this, MainActivity::class.java);
        startActivity(intent);
    }
}
