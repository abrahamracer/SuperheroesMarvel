package ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.avv.superheroesmarvel.databinding.SuperheroesElementBinding
import com.bumptech.glide.Glide
import data.remote.model.SuperheroesDto


class SuperheroesAdapter (
    private val superheroes: List<SuperheroesDto>,
    private val onPlayerClicked: (SuperheroesDto) -> Unit
): RecyclerView.Adapter<SuperheroesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuperheroesViewHolder {
        val binding = SuperheroesElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SuperheroesViewHolder(binding)
    }

    override fun getItemCount(): Int = superheroes.size

    override fun onBindViewHolder(holder: SuperheroesViewHolder, position: Int) {
        val superheroe = superheroes[position]
        holder.bind(superheroe)

        //Cargamos con glide la imagen al imageView
        Glide.with(holder.itemView.context)
        Glide.with(holder.itemView.context)
            .load(superheroe.url_image)
            .into(holder.ivThumbnail)

        //Con Picasso


        holder.itemView.setOnClickListener {
            onPlayerClicked(superheroe)
        }
    }

}