package id.kel3.sijahit.sijahit.admin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

class AdminHomeViewModel : ViewModel() {
    private val dbRef = FirebaseDatabase.getInstance().reference

    private val _saldo = MutableStateFlow(0)
    val saldo: StateFlow<Int> = _saldo

    private val _pesananAktif = MutableStateFlow(0)
    val pesananAktif: StateFlow<Int> = _pesananAktif

    private val _stokMenipis = MutableStateFlow(0)
    val stokMenipis: StateFlow<Int> = _stokMenipis

    private val _konfirmasi = MutableStateFlow(0)
    val konfirmasi: StateFlow<Int> = _konfirmasi

    private val _adminCount = MutableStateFlow(0)
    val adminCount: StateFlow<Int> = _adminCount

    private val _pesananSelesai = MutableStateFlow(0)
    val pesananSelesai: StateFlow<Int> = _pesananSelesai

    private val _totalProduk = MutableStateFlow(0)
    val totalProduk: StateFlow<Int> = _totalProduk

    private val _pelanggan = MutableStateFlow(0)
    val pelanggan: StateFlow<Int> = _pelanggan

    private val _penghasilanHariIni = MutableStateFlow(0)
    val penghasilanHariIni: StateFlow<Int> = _penghasilanHariIni

    private val _penghasilanMinggu = MutableStateFlow(0)
    val penghasilanMinggu: StateFlow<Int> = _penghasilanMinggu

    private val _penghasilanBulan = MutableStateFlow(0)
    val penghasilanBulan: StateFlow<Int> = _penghasilanBulan

    init {
        fetchData()
    }

    private fun fetchData() = viewModelScope.launch {
        // 1. Saldo
        dbRef.child("adminDashboard").child("saldo").get().addOnSuccessListener {
            _saldo.value = it.getValue(Int::class.java) ?: 0
        }

        // 2, 4, 6. Pesanan Aktif, Menunggu, dan Selesai + Hitung Penghasilan
        dbRef.child("pesanan").get().addOnSuccessListener { snapshot ->
            var aktif = 0
            var menunggu = 0
            var selesai = 0
            var totalsaldo = 0

            var hariIniTotal = 0
            var mingguTotal = 0
            var bulanTotal = 0

            val now = Calendar.getInstance()

            for (child in snapshot.children) {
                val status = child.child("status").getValue(String::class.java)?.lowercase(Locale.ROOT)
                val total = child.child("total").getValue(Int::class.java) ?: 0
                val timestamp = child.child("timestamp").getValue(Long::class.java) ?: 0L

                when (status) {
                    "aktif" -> aktif++
                    "menunggu" -> menunggu++
                    "selesai" -> {
                        selesai++
                        totalsaldo += total

                        // Cek waktu
                        val cal = Calendar.getInstance().apply { timeInMillis = timestamp }

                        if (isSameDay(now, cal)) hariIniTotal += total
                        if (isSameWeek(now, cal)) mingguTotal += total
                        if (isSameMonth(now, cal)) bulanTotal += total
                    }
                }
            }

            _pesananAktif.value = aktif
            _konfirmasi.value = menunggu
            _pesananSelesai.value = selesai
            _saldo.value = totalsaldo

            _penghasilanHariIni.value = hariIniTotal
            _penghasilanMinggu.value = mingguTotal
            _penghasilanBulan.value = bulanTotal
        }

        // 3, 7. Produk: Stok Menipis & Total Produk
        dbRef.child("produk").get().addOnSuccessListener { snapshot ->
            var menipis = 0
            var total = 0
            for (child in snapshot.children) {
                val stok = child.child("stok").getValue(Int::class.java) ?: 0
                if (stok <= 3) menipis++
                total++
            }
            _stokMenipis.value = menipis
            _totalProduk.value = total
        }

        // 5, 8. Roles: Admin & User
        dbRef.child("roles").get().addOnSuccessListener { snapshot ->
            var admin = 0
            var user = 0
            for (child in snapshot.children) {
                when (child.getValue(String::class.java)?.lowercase(Locale.ROOT)) {
                    "admin" -> admin++
                    "user" -> user++
                }
            }
            _adminCount.value = admin
            _pelanggan.value = user
        }
    }

    private fun isSameDay(a: Calendar, b: Calendar): Boolean =
        a.get(Calendar.YEAR) == b.get(Calendar.YEAR) &&
                a.get(Calendar.DAY_OF_YEAR) == b.get(Calendar.DAY_OF_YEAR)

    private fun isSameWeek(a: Calendar, b: Calendar): Boolean =
        a.get(Calendar.YEAR) == b.get(Calendar.YEAR) &&
                a.get(Calendar.WEEK_OF_YEAR) == b.get(Calendar.WEEK_OF_YEAR)

    private fun isSameMonth(a: Calendar, b: Calendar): Boolean =
        a.get(Calendar.YEAR) == b.get(Calendar.YEAR) &&
                a.get(Calendar.MONTH) == b.get(Calendar.MONTH)
}