package ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.avv.superheroesmarvel.databinding.SuperheroesElementBinding
import data.remote.model.SuperheroesDto

class SuperheroesViewHolder (private var binding: SuperheroesElementBinding):
    RecyclerView.ViewHolder(binding.root) {
    val ivThumbnail = binding.ivThumbnail

    fun bind(superheroe: SuperheroesDto){
        binding.tvTitle.text = superheroe.nombre
    }
}