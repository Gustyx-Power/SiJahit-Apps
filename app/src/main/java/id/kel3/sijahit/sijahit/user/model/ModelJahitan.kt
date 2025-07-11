package id.kel3.sijahit.sijahit.user.model

import java.io.Serializable

data class ModelJahitan(
    val nama: String = "",
    val deskripsi: String = "",
    val gambar1: Int,
    val gambar: String = ""
) : Serializable