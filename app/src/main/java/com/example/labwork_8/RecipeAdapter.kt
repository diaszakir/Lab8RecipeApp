import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.labwork_8.R
import com.example.labwork_8.Recipe

class RecipeAdapter(
    private val recipes: List<Recipe>,
    private val onDelete: (String) -> Unit
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    inner class RecipeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val nameText: TextView = view.findViewById(R.id.recipeName)
        private val ingredientsText: TextView = view.findViewById(R.id.recipeIngredients)
        private val deleteButton: ImageButton = view.findViewById(R.id.deleteButton)

        fun bind(recipe: Recipe) {
            nameText.text = recipe.name
            ingredientsText.text = recipe.ingredients
            deleteButton.setOnClickListener { recipe.id?.let { onDelete(it) } }
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
