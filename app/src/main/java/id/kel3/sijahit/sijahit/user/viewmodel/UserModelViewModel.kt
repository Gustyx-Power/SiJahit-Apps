package id.kel3.sijahit.sijahit.user.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import id.kel3.sijahit.sijahit.admin.viewmodel.ModelJahitan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserModelViewModel : ViewModel() {
    private val dbRef = FirebaseDatabase.getInstance().getReference("modelJahitan")

    private val _modelList = MutableStateFlow<List<ModelJahitan>>(emptyList())
    val modelList: StateFlow<List<ModelJahitan>> get() = _modelList

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    init {
        fetchModels()
    }

    private fun fetchModels() {
        _isLoading.value = true
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<ModelJahitan>()
                for (item in snapshot.children) {
                    val model = item.getValue(ModelJahitan::class.java)
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
}