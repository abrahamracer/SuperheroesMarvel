package data.remote.model

import com.google.gson.annotations.SerializedName

data class SuperheroesDetailDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("nombre")
    val nombre: String? = null,
    @SerializedName("published_at")
    val published_at: String? = null,
    @SerializedName("poder")
    val poder: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("url_image")
    val url_image: String? = null, // Comma was missing here
    @SerializedName("armas")
    val armas: String? = null,
    @SerializedName("yt_video")
    val ytVideo: String? = null
)
