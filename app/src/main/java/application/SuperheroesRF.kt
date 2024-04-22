package application

import android.app.Application
import data.SuperheroesRepository
import data.remote.RetrofitHelper
import retrofit2.Retrofit

class SuperheroesRF: Application() {

    private val retrofit by lazy {
        RetrofitHelper().getRetrofit()

    }

    val repository by lazy { SuperheroesRepository(retrofit) }
}
