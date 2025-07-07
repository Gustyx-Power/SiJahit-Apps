package id.kel3.sijahit.sijahit.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import id.kel3.sijahit.sijahit.model.Pesanan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PesananViewModel : ViewModel() {
    private val _pesananList = MutableStateFlow<List<Pesanan>>(emptyList())
    val pesananList: StateFlow<List<Pesanan>> = _pesananList

    private val database = FirebaseDatabase.getInstance().getReference("pesanan")

    init {
        fetchPesanan()
    }

    private fun fetchPesanan() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Pesanan>()
                for (child in snapshot.children) {
                    val data = child.getValue(Pesanan::class.java)
                    if (data != null) {
                        list.add(data.copy(id = child.key ?: ""))
                    }
                }
                _pesananList.value = list
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun updateStatus(id: String, status: String) {
        database.child(id).child("status").setValue(status)
    }
}