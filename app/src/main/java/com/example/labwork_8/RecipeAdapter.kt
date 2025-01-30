import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.labwork_8.R
import com.example.labwork_8.Recipe
import com.example.labwork_8.RecipeDetailActivity

class RecipeAdapter(
    private val recipes: List<Recipe>,
    private val onDelete: (String) -> Unit,
    private val onItemClick: (Recipe) -> Unit,
    private val onImageClick: (Recipe) -> Unit // Принимаем onImageClick
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    inner class RecipeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val nameText: TextView = view.findViewById(R.id.recipeName)
        private val ingredientsText: TextView = view.findViewById(R.id.recipeIngredients)
        private val deleteButton: ImageButton = view.findViewById(R.id.deleteButton)
        private val recipeImage: ImageView? = view.findViewById(R.id.recipeImage)

        fun bind(recipe: Recipe) {
            nameText.text = recipe.name
            ingredientsText.text = recipe.ingredients

            deleteButton.setOnClickListener { recipe.id.let { onDelete(it) } }

            // Изменяем открытие деталей рецепта
            itemView.setOnClickListener {
                // Создаем Intent прямо здесь
                val intent = Intent(itemView.context, RecipeDetailActivity::class.java).apply {
                    putExtra("recipeId", recipe.id)  // Передаем ID рецепта
                }
                itemView.context.startActivity(intent)
            }

            if (recipe.imageUrl.isNotEmpty()) {
                recipeImage?.let {
                    Glide.with(itemView.context)
                        .load(recipe.imageUrl)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .into(it)
                }
            } else {
                recipeImage?.setImageResource(R.drawable.ic_launcher_foreground)
            }

            recipeImage?.setOnClickListener { onImageClick(recipe) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(recipes[position])
    }

    override fun getItemCount(): Int = recipes.size
}
