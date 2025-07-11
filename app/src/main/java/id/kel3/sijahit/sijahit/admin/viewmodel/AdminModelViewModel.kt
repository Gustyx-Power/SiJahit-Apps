package id.kel3.sijahit.sijahit.admin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

data class ModelJahitan(
    val id: String = "",
    val nama: String = "",
    val deskripsi: String = "",
    val gambar: String = ""
)

class AdminModelViewModel : ViewModel() {

    private val dbRef = FirebaseDatabase.getInstance().getReference("modelJahitan")

    private val _modelList = MutableStateFlow<List<ModelJahitan>>(emptyList())
    val modelList: StateFlow<List<ModelJahitan>> = _modelList

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isDialogVisible = MutableStateFlow(false)
    val isDialogVisible: StateFlow<Boolean> = _isDialogVisible

    private val _selectedModelId = MutableStateFlow<String?>(null)
    val selectedModelId: StateFlow<String?> = _selectedModelId

    private val _isDeleteDialogVisible = MutableStateFlow(false)
    val isDeleteDialogVisible: StateFlow<Boolean> = _isDeleteDialogVisible

    init {
        fetchModelList()
    }

    private fun fetchModelList() {
        _isLoading.value = true
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<ModelJahitan>()
                for (modelSnapshot in snapshot.children) {
                    val model = modelSnapshot.getValue(ModelJahitan::class.java)
                    model?.let { list.add(it) }
                }
                _modelList.value = list
                _isLoading.value = false
            }

            override fun onCancelled(error: DatabaseError) {
                _isLoading.value = false
            }
        })
    }

    fun showAddModelDialog() {
        _isDialogVisible.value = true
    }

    fun hideAddModelDialog() {
        _isDialogVisible.value = false
    }

    fun tambahModel(nama: String, deskripsi: String, gambar: String) {
        val id = UUID.randomUUID().toString()
        val model = ModelJahitan(id, nama, deskripsi, gambar)
        dbRef.child(id).setValue(model)
        hideAddModelDialog()
    }

    fun confirmDelete(id: String) {
        _selectedModelId.value = id
        _isDeleteDialogVisible.value = true
    }

    fun dismissDeleteDialog() {
        _isDeleteDialogVisible.value = false
        _selectedModelId.value = null
    }

    fun hapusModel(id: String) {
        dbRef.child(id).removeValue()
    }
}