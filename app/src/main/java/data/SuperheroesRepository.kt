package data

import data.remote.SuperheroesApi
import data.remote.model.SuperheroesDetailDto
import data.remote.model.SuperheroesDto
import retrofit2.Call
import retrofit2.Retrofit


class SuperheroesRepository(private val retrofit: Retrofit) {

    private val superheroesApi: SuperheroesApi = retrofit.create(SuperheroesApi::class.java)

    fun getSuperheroes(url: String?): Call<List<SuperheroesDto>> = superheroesApi.getSuperheroes(url)

    fun getSuperheroesDetail(id: String?): Call<SuperheroesDetailDto> = superheroesApi.getSuperheroesDetail(id)

}