package ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import application.SuperheroesRF
import com.avv.superheroesmarvel.databinding.FragmentSuperheroesDetailBinding
import com.bumptech.glide.Glide
import data.SuperheroesRepository
import data.remote.model.SuperheroesDetailDto
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import utils.Constants


private const val ID = "id"

class SuperheroesDetailFragment : Fragment() {
    private var _binding: FragmentSuperheroesDetailBinding? = null
    private val binding get() = _binding!!

    private var id: String? = null

    private lateinit var repository: SuperheroesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            id = args.getString(ID)

            Log.d(Constants.LOGTAG, "Id recibido: $id")

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSuperheroesDetailBinding.inflate(inflater, container, false)
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
            id?.let { ids ->
                val call: Call<SuperheroesDetailDto> = repository.getSuperheroesDetail(ids)

                call.enqueue(object : Callback<SuperheroesDetailDto> {
                    override fun onResponse(p0: Call<SuperheroesDetailDto>, response: Response<SuperheroesDetailDto>) {
                        binding.apply {
                            pbLoading.visibility = View.INVISIBLE
                            tvTitle.text = response.body()?.nombre
                            Glide.with(requireActivity())
                                .load(response.body()?.url_image)
                                .into(ivImage)
                            tvLongDesc.append(response.body()?.description)
                            tvPosition.append(response.body()?.published_at)
                            tvCountry.append(response.body()?.poder)
                            tvFoot.append(response.body()?.armas)


                        }
                    }

                    override fun onFailure(p0: Call<SuperheroesDetailDto>, p1: Throwable) {

                        binding.apply {
                            pbLoading.visibility = View.INVISIBLE

                            Log.d(Constants.LOGTAG, p1.message.toString())
                        }
                    }

                })
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(gameId: String) =
            SuperheroesDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ID, gameId)
                }
            }
    }
}
