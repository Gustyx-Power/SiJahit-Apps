package id.kel3.sijahit.sijahit.admin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

data class StockItem(
    val id: String = "",
    val nama: String = "",
    val jumlah: Int = 0,
    val warna: String = "",
    val gambar: String = "" // URL gambar (opsional, bisa dikosongkan)
)

class AdminStockViewModel : ViewModel() {
    private val dbRef = FirebaseDatabase.getInstance().getReference("stock")

    private val _stockList = MutableStateFlow<List<StockItem>>(emptyList())
    val stockList: StateFlow<List<StockItem>> get() = _stockList

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> get() = _showDialog

    init {
        fetchStockList()
    }

    private fun fetchStockList() {
        _isLoading.value = true
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<StockItem>()
                for (item in snapshot.children) {
                    val stock = item.getValue(StockItem::class.java)
                    stock?.let { list.add(it) }
                }
                _stockList.value = list
                _isLoading.value = false
            }

            override fun onCancelled(error: DatabaseError) {
                _isLoading.value = false
            }
        })
    }

    fun showAddStockDialog() {
        _showDialog.value = true
    }

    fun hideAddStockDialog() {
        _showDialog.value = false
    }

    fun tambahStock(nama: String, jumlah: Int, warna: String, gambar: String) {
        val id = UUID.randomUUID().toString()
        val stock = StockItem(id, nama, jumlah, warna, gambar)
        dbRef.child(id).setValue(stock)
        _showDialog.value = false
    }

    fun hapusStock(id: String) {
        dbRef.child(id).removeValue()
    }
}