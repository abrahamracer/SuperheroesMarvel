package ui.fragments

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import application.SuperheroesRF
import com.avv.superheroesmarvel.R
import com.avv.superheroesmarvel.databinding.FragmentSuperheroesDetailBinding
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.FirebaseApp
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
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
    private lateinit var map: GoogleMap
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            id = args.getString(ID)
            Log.d(Constants.LOGTAG, "Id recibido: $id")
        }

        // Inicializa Firebase si aún no lo está
        if (!::repository.isInitialized) {
            FirebaseApp.initializeApp(requireContext())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSuperheroesDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize and start media player
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.vengadores)
        mediaPlayer?.start()

        repository = (requireActivity().application as SuperheroesRF).repository

        binding.pbLoading.visibility = View.VISIBLE // Show loading indicator

        lifecycleScope.launch {
            id?.let { ids ->
                val call: Call<SuperheroesDetailDto> = repository.getSuperheroesDetail(ids)

                call.enqueue(object : Callback<SuperheroesDetailDto> {
                    override fun onResponse(call: Call<SuperheroesDetailDto>, response: Response<SuperheroesDetailDto>) {
                        binding.apply {
                            pbLoading.visibility = View.INVISIBLE // Hide loading indicator
                            if (response.isSuccessful) {
                                tvTitle.text = response.body()?.nombre ?: ""
                                Glide.with(requireActivity())
                                    .load(response.body()?.url_image)
                                    .into(ivImage)
                                tvLongDesc.append(response.body()?.description ?: "")
                                tvPosition.append(response.body()?.published_at ?: "")
                                tvCountry.append(response.body()?.poder ?: "")
                                tvFoot.append(response.body()?.armas ?: "")

                                // Load video into YouTubePlayerView
                                youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                                    override fun onReady(youTubePlayer: YouTubePlayer) {
                                        response.body()?.ytVideo?.let { videoId ->
                                            youTubePlayer.loadVideo(videoId, 0f)
                                        } ?: run {
                                            // Load default video if no video ID is provided
                                            youTubePlayer.loadVideo("0AwxHCI_BnA", 0f)
                                        }
                                        // Inicializar el mapa después de que el reproductor de YouTube esté listo
                                        response.body()?.let { playerDetail ->
                                            initializeMap(playerDetail.lat ?: 0.0, playerDetail.long ?: 0.0)
                                        }
                                    }
                                })

                                // Add the YouTubePlayerView to the lifecycle observer
                                lifecycle.addObserver(youtubePlayerView)
                            } else {
                                // Handle the case when the response is not successful
                                tvTitle.text = getString(R.string.error)
                            }
                        }
                    }

                    override fun onFailure(call: Call<SuperheroesDetailDto>, t: Throwable) {
                        binding.apply {
                            pbLoading.visibility = View.INVISIBLE // Hide loading indicator
                            Log.d(Constants.LOGTAG, t.message.toString())
                            tvTitle.text = getString(R.string.error)
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

    private fun initializeMap(lat: Double, long: Double) {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            // Aquí puedes realizar operaciones con el mapa una vez que esté listo
            map = googleMap
            createMarker(lat, long)
            // Por ejemplo, puedes establecer la configuración del mapa aquí
            // map.uiSettings.isZoomControlsEnabled = true
        }
    }

    private fun createMarker(lat: Double, long: Double){
        val coordinates = LatLng(lat, long)

        val marker = MarkerOptions()
            .position(coordinates)
            .title("Estadio jugador")
            .snippet("FIFA")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marvel))

        map.addMarker(marker)

        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordinates, 16f),
            4000,
            null
        )
    }
}
