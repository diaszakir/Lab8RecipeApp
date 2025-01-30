package com.example.labwork_8

import RecipeAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class MainActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var recipeAdapter: RecipeAdapter
    private val recipeList = mutableListOf<Recipe>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase
        database = Firebase.database.reference.child("recipes")

        // Setup RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.listItem)
        recipeAdapter = RecipeAdapter(recipeList, { recipeId ->
            deleteRecipe(recipeId)
        }, { recipe ->
            // Открыть экран с деталями рецепта
            val intent = Intent(this, RecipeDetailActivity::class.java).apply {
                putExtra("recipeName", recipe.name)
                putExtra("ingredients", recipe.ingredients)
                putExtra("instructions", recipe.instructions)
                putExtra("recipeID", recipe.id)
            }
            startActivity(intent)
        }, { recipe ->
            // Обработчик нажатия на изображение (например, открытие в полном экране)
            val intent = Intent(this, RecipeDetailActivity::class.java).apply {
                putExtra("imageUrl", recipe.imageUrl)
            }
            startActivity(intent)
        });
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = recipeAdapter

        // Fetch recipes from Firebase
        fetchRecipes()

        // Add new recipe
        val addButton: FloatingActionButton = findViewById(R.id.addBtn)
        addButton.setOnClickListener {
            startActivity(Intent(this, AddRecipeActivity::class.java))
        }
    }

    private fun fetchRecipes() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                recipeList.clear()
                for (recipeSnapshot in snapshot.children) {
                    val recipe = recipeSnapshot.getValue(Recipe::class.java)
                    recipe?.let { recipeSnapshot.key?.let { it1 -> it.copy(id = it1) }
                        ?.let { it2 -> recipeList.add(it2) } }
                }
                recipeAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Failed to load recipes.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteRecipe(recipeId: String) {
        database.child(recipeId).removeValue()
            .addOnSuccessListener { Toast.makeText(this, "Recipe deleted.", Toast.LENGTH_SHORT).show() }
            .addOnFailureListener { Toast.makeText(this, "Failed to delete recipe.", Toast.LENGTH_SHORT).show() }
    }
}

