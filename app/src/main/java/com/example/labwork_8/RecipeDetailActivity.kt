package com.example.labwork_8

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RecipeDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        val tvRecipeName: TextView = findViewById(R.id.tvRecipeName)
        val tvIngredients: TextView = findViewById(R.id.tvIngredients)
        val tvInstructions: TextView = findViewById(R.id.tvInstructions)
        val ivRecipeImage: ImageView = findViewById(R.id.ivRecipeImage)

        // Получаем ID рецепта из Intent
        val recipeId = intent.getStringExtra("recipeId")

        if (recipeId == null) {
            Toast.makeText(this, "Error: Recipe ID not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Получаем ссылку на рецепт в Firebase
        val database = FirebaseDatabase.getInstance()
        val recipeRef = database.getReference("recipes").child(recipeId)

        // Слушаем изменения данных
        recipeRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val recipe = snapshot.getValue(Recipe::class.java)

                if (recipe != null) {
                    tvRecipeName.text = recipe.name
                    tvIngredients.text = recipe.ingredients
                    tvInstructions.text = recipe.instructions

                    // Загружаем изображение
                    if (!recipe.imageUrl.isNullOrEmpty()) {
                        Log.d("RecipeDetailActivity", "Loading image URL: ${recipe.imageUrl}")
                        Picasso.get()
                            .load(recipe.imageUrl)
                            .placeholder(R.drawable.ic_launcher_foreground)
                            .error(R.drawable.ic_launcher_foreground)
                            .into(ivRecipeImage)
                    } else {
                        Log.d("RecipeDetailActivity", "No image URL found")
                        ivRecipeImage.setImageResource(R.drawable.ic_launcher_foreground)
                    }
                } else {
                    Toast.makeText(baseContext, "Recipe not found", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("RecipeDetailActivity", "Error loading recipe", error.toException())
                Toast.makeText(baseContext, "Error loading recipe", Toast.LENGTH_SHORT).show()
                finish()
            }
        })
    }

    fun returnBtn(v: View) {
        intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}