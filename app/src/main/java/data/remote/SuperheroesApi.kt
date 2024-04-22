package data.remote

import data.remote.model.SuperheroesDetailDto
import data.remote.model.SuperheroesDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url


interface SuperheroesApi {

    @GET
    fun getSuperheroes(
        @Url url: String?
    ): Call<List<SuperheroesDto>>

    @GET("superheroes/{id}")
    fun getSuperheroesDetail(
        @Path("id") id: String?,
    ): Call<SuperheroesDetailDto>
}