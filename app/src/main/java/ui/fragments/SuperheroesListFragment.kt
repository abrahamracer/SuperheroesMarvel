package ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import application.SuperheroesRF
import com.avv.superheroesmarvel.R
import com.avv.superheroesmarvel.databinding.FragmentSuperheroesListBinding
import data.SuperheroesRepository
import data.remote.model.SuperheroesDto
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ui.adapters.SuperheroesAdapter
import utils.Constants


class SuperheroesListFragment : Fragment() {

    private var _binding: FragmentSuperheroesListBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: SuperheroesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSuperheroesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireActivity().application as SuperheroesRF).repository
        lifecycleScope.launch {
            //val call: Call<List<GameDto>> = repository.getGames("cm/games/games_list.php")
            val call: Call<List<SuperheroesDto>> = repository.getSuperheroes("superheroes")

            call.enqueue(object : Callback<List<SuperheroesDto>> {
                override fun onResponse(p0: Call<List<SuperheroesDto>>, response: Response<List<SuperheroesDto>>) {


                    binding.pbLoading.visibility = View.GONE

                    Log.d(Constants.LOGTAG, "Respuesta recibida: ${response.body()}")

                    response.body()?.let {superheroes ->
                        binding.rvGames.apply {
                            layoutManager = LinearLayoutManager(requireContext())
                            adapter = SuperheroesAdapter(superheroes){ superheroe ->
                                //Aquí va la operación para el click de cada elemento
                                requireActivity().supportFragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container, SuperheroesDetailFragment.newInstance(superheroe.id.toString()))
                                    .addToBackStack(null)
                                    .commit()

                            }

                        }
                    }
                }

                override fun onFailure(p0: Call<List<SuperheroesDto>>, error: Throwable) {

                    binding.pbLoading.visibility = View.GONE

                    Toast.makeText(
                        requireContext(),
                        "Error en la conexión: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
        }
    }
}