package com.example.labwork_8

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class AddRecipeActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private var selectedImageUri: Uri? = null
    private lateinit var recipeImage: ImageView // Инициализация переменной для изображения

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        // Инициализация Firebase
        database = FirebaseDatabase.getInstance().getReference("recipes")
        storage = FirebaseStorage.getInstance()

        // Инициализация переменной для ImageView
        recipeImage = findViewById(R.id.recipeImage)

        val saveButton: Button = findViewById(R.id.saveBtn)
        val imageButton: Button = findViewById(R.id.uploadImageBtn)

        // Кнопка выбора изображения
        imageButton.setOnClickListener {
            pickImageFromGallery()
        }

        // Кнопка сохранения рецепта
        saveButton.setOnClickListener {
            val name = findViewById<EditText>(R.id.recipeName).text.toString()
            val ingredients = findViewById<EditText>(R.id.ingredients).text.toString()
            val instructions = findViewById<EditText>(R.id.instruction).text.toString()

            if (name.isNotBlank() && ingredients.isNotBlank() && instructions.isNotBlank()) {
                if (selectedImageUri != null) {
                    uploadImageToFirebase(name, ingredients, instructions)
                } else {
                    addRecipe(name, ingredients, instructions, "")
                }
            } else {
                Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Метод для выбора изображения
    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_IMAGE_PICK)
    }

    // Обработка результата выбора изображения
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            recipeImage.setImageURI(selectedImageUri) // Используем переменную recipeImage для установки изображения
        }
    }

    // Загрузка изображения в Firebase Storage
    // Загрузка изображения в Firebase Storage
    private fun uploadImageToFirebase(name: String, ingredients: String, instructions: String) {
        selectedImageUri?.let { uri ->
            val imageRef = storage.reference.child("recipe_images/${UUID.randomUUID()}.jpg")

            imageRef.putFile(uri)
                .addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        val imageUrl = downloadUri.toString()
                        Log.d("AddRecipeActivity", "Image URL: $imageUrl")

                        // Сначала сохраняем рецепт
                        val recipeId = database.push().key
                        val recipe = Recipe(id = recipeId ?: "", name = name, ingredients = ingredients, instructions = instructions, imageUrl = imageUrl)

                        recipeId?.let {
                            database.child(it).setValue(recipe)
                                .addOnSuccessListener {
                                    // После успешного сохранения открываем DetailActivity
                                    val intent = Intent(this, RecipeDetailActivity::class.java).apply {
                                        putExtra("recipeId", recipeId)  // Передаем только ID
                                    }
                                    startActivity(intent)
                                    Toast.makeText(this, "Recipe added successfully", Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Failed to add recipe.", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("AddRecipeActivity", "Upload failed", exception)
                    Toast.makeText(this, "Failed to upload image: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

// Убираем отдельный метод addRecipe, так как теперь сохранение происходит в uploadImageToFirebase

    // Сохранение рецепта в Firebase Database
    private fun addRecipe(name: String, ingredients: String, instructions: String, imageUrl: String) {
        val recipeId = database.push().key
        val recipe = Recipe(id = recipeId ?: "", name = name, ingredients = ingredients, instructions = instructions, imageUrl = imageUrl)

        recipeId?.let {
            database.child(it).setValue(recipe)
                .addOnSuccessListener {
                    Toast.makeText(this, "Recipe added successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to add recipe.", Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun returnBtn(v: View) {
        startActivity(Intent(this, MainActivity::class.java))
    }

    companion object {
        private const val REQUEST_CODE_IMAGE_PICK = 100
    }
}
