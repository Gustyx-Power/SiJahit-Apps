package id.kel3.sijahit.sijahit.admin.viewmodel

data class Ukuran(
    val panjangBadan: Int = 0,
    val lebarBadan: Int = 0,
    val lebarPinggang: Int = 0,
    val lebarPinggul: Int = 0,
    val lebarLengan: Int = 0,
    val panjangLengan: Int = 0
)

data class Pesanan(
    val id: String = "",
    val namaUser: String = "",
    val jenisPesanan: String = "",
    val ukuran: Ukuran = Ukuran(),
    val jenisKain: String = "",
    val total: Int = 0,
    val status: String = "Menunggu",
    val timestamp: Long = 0L
)