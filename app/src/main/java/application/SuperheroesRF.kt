package application

import android.app.Application
import com.google.firebase.FirebaseApp
import data.SuperheroesRepository
import data.remote.RetrofitHelper

class SuperheroesRF : Application() {

    private val retrofit by lazy {
        RetrofitHelper().getRetrofit()
    }

    val repository by lazy { SuperheroesRepository(retrofit) }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
