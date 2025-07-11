package id.kel3.sijahit.sijahit.user.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import id.kel3.sijahit.sijahit.user.model.UkuranData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class UserPesanan(
    val id: String = "",
    val namaUser: String = "",
    val jenisPesanan: String = "",
    val jenisKain: String = "",
    val status: String = "",
    val ukuran: UkuranData = UkuranData(),
)

class UserPesananViewModel : ViewModel() {
    private val _pesananList = MutableStateFlow<List<UserPesanan>>(emptyList())
    val pesananList = _pesananList.asStateFlow()

    init {
        loadPesanan()
    }

    private fun loadPesanan() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val dbRef = FirebaseDatabase.getInstance().getReference("users").child(uid).child("pesanan")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<UserPesanan>()
                for (child in snapshot.children) {
                    val id = child.child("id").getValue(String::class.java) ?: continue
                    val namaUser = child.child("namaUser").getValue(String::class.java) ?: ""
                    val jenisPesanan = child.child("jenisPesanan").getValue(String::class.java) ?: ""
                    val jenisKain = child.child("jenisKain").getValue(String::class.java) ?: ""
                    val status = child.child("status").getValue(String::class.java) ?: ""
                    val ukuranSnap = child.child("ukuran")

                    val ukuran = UkuranData(
                        panjangBadan = ukuranSnap.child("panjangBadan").getValue(Int::class.java) ?: 0,
                        panjangLengan = ukuranSnap.child("panjangLengan").getValue(Int::class.java) ?: 0,
                        lebarBadan = ukuranSnap.child("lebarBadan").getValue(Int::class.java) ?: 0,
                        lebarPinggang = ukuranSnap.child("lebarPinggang").getValue(Int::class.java) ?: 0,
                        lebarPinggul = ukuranSnap.child("lebarPinggul").getValue(Int::class.java) ?: 0,
                        lebarLengan = ukuranSnap.child("lebarLengan").getValue(Int::class.java) ?: 0,
                    )

                    list.add(UserPesanan(id, namaUser, jenisPesanan, jenisKain, status, ukuran))
                }
                _pesananList.value = list
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}