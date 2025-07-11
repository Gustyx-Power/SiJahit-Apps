package id.kel3.sijahit.sijahit.admin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminPesananViewModel : ViewModel() {
    private val dbRef = FirebaseDatabase.getInstance().reference

    private val _pesananList = MutableStateFlow<List<Pesanan>>(emptyList())
    val pesananList: StateFlow<List<Pesanan>> = _pesananList

    init {
        fetchPesanan()
    }

    private fun fetchPesanan() = viewModelScope.launch {
        dbRef.child("pesanan").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Pesanan>()
                snapshot.children.forEach { item ->
                    val id = item.child("id").getValue(String::class.java) ?: ""
                    val namaUser = item.child("namaUser").getValue(String::class.java) ?: ""
                    val jenisPesanan = item.child("jenisPesanan").getValue(String::class.java) ?: ""
                    val jenisKain = item.child("jenisKain").getValue(String::class.java) ?: ""
                    val total = item.child("total").getValue(Int::class.java) ?: 0
                    val status = item.child("status").getValue(String::class.java) ?: ""
                    val timestamp = item.child("timestamp").getValue(Long::class.java) ?: 0L

                    val ukuran = Ukuran(
                        panjangBadan = item.child("ukuran/panjangBadan").getValue(Int::class.java) ?: 0,
                        lebarBadan = item.child("ukuran/lebarBadan").getValue(Int::class.java) ?: 0,
                        lebarPinggang = item.child("ukuran/lebarPinggang").getValue(Int::class.java) ?: 0,
                        lebarPinggul = item.child("ukuran/lebarPinggul").getValue(Int::class.java) ?: 0,
                        lebarLengan = item.child("ukuran/lebarLengan").getValue(Int::class.java) ?: 0,
                        panjangLengan = item.child("ukuran/panjangLengan").getValue(Int::class.java) ?: 0
                    )

                    val pesanan = Pesanan(
                        id = id,
                        namaUser = namaUser,
                        jenisPesanan = jenisPesanan,
                        jenisKain = jenisKain,
                        total = total,
                        status = status,
                        timestamp = timestamp,
                        ukuran = ukuran
                    )

                    list.add(pesanan)
                }
                _pesananList.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                // Bisa log error di sini jika mau
            }
        })
    }

    fun ubahStatus(id: String, newStatus: String) {
        dbRef.child("pesanan").child(id).child("status").setValue(newStatus)
    }
}