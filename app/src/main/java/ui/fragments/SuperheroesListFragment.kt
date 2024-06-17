package ui.fragments

import android.content.Intent
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import data.SuperheroesRepository
import data.remote.model.SuperheroesDto
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ui.Login
import ui.adapters.SuperheroesAdapter
import utils.Constants

class SuperheroesListFragment : Fragment() {

    private var _binding: FragmentSuperheroesListBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: SuperheroesRepository

    private lateinit var firebaseAuth: FirebaseAuth
    private var user: FirebaseUser? = null
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // No se necesita inicializar Firebase aquí si ya se inicializó en Application
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSuperheroesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

        user = firebaseAuth.currentUser
        userId = user?.uid

        binding.tvUsuario.text = user?.email

        // Revisamos si el email no está verificado
        if (user?.isEmailVerified != true) {
            binding.tvCorreoNoVerificado.visibility = View.VISIBLE
            binding.btnReenviarVerificacion.visibility = View.VISIBLE

            binding.btnReenviarVerificacion.setOnClickListener {
                user?.sendEmailVerification()?.addOnSuccessListener {
                    Toast.makeText(requireContext(), "El correo de verificación ha sido enviado", Toast.LENGTH_SHORT).show()
                }?.addOnFailureListener {
                    Toast.makeText(requireContext(), "Error: El correo de verificación no se ha podido enviar", Toast.LENGTH_SHORT).show()
                    Log.d("LOGS", "onFailure: ${it.message}")
                }
            }
        }

        // Para cerrar sesión
        binding.btnCerrarSesion.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(requireActivity(), Login::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        repository = (requireActivity().application as SuperheroesRF).repository
        lifecycleScope.launch {
            val call: Call<List<SuperheroesDto>> = repository.getSuperheroes("superheroes")

            call.enqueue(object : Callback<List<SuperheroesDto>> {
                override fun onResponse(p0: Call<List<SuperheroesDto>>, response: Response<List<SuperheroesDto>>) {
                    binding.pbLoading.visibility = View.GONE
                    Log.d(Constants.LOGTAG, "Respuesta recibida: ${response.body()}")

                    response.body()?.let { superheroes ->
                        binding.rvPlayers.apply {
                            layoutManager = LinearLayoutManager(requireContext())
                            adapter = SuperheroesAdapter(superheroes) { superheroe ->
                                // Aquí va la operación para el click de cada elemento
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
