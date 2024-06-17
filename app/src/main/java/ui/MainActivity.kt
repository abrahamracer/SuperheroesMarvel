package ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.avv.superheroesmarvel.R
import com.avv.superheroesmarvel.databinding.ActivityMainBinding
import ui.fragments.SuperheroesListFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SuperheroesListFragment())
                .commit()
        }
    }
}
