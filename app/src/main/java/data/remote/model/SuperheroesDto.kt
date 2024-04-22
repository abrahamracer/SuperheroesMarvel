package data.remote.model

import com.google.gson.annotations.SerializedName


data class SuperheroesDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("nombre")
    val nombre: String? = null,
    @SerializedName("url_image")
    val url_image: String? = null
)