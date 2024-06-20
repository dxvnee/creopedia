package org.d3if3121.creopedia.ui.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.d3if3121.creopedia.model.Creo
import org.d3if3121.creopedia.network.ApiStatus
import org.d3if3121.creopedia.network.CreoApi
import java.io.ByteArrayOutputStream

class MainViewModel: ViewModel(){
    private val _creoData = MutableStateFlow<List<Creo>>(emptyList())
    val creoData: StateFlow<List<Creo>> = _creoData


    private val _currentCreo = MutableStateFlow<Creo?>(null)
    val currentCreo: StateFlow<Creo?> = _currentCreo

    private val _newCreo = MutableStateFlow<Creo?>(null)
    val newCreo: StateFlow<Creo?> = _newCreo

    var status = MutableStateFlow(ApiStatus.LOADING)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set


    fun getAllCreo(userId: String){
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                _creoData.value = CreoApi.service.getCreos(userId)
                status.value = ApiStatus.SUCCESS
            }catch (e: Exception){
                Log.d("MainViewModelGetAllCreo", "Failure: ${e.message}")
                status.value = ApiStatus.FAILED
            }
        }
    }


    fun createCreoWithImage(userId: String, creo: Creo, bitmap: Bitmap){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = CreoApi.service.createCreoWithImage(
                    userId,
                    creo.name.toRequestBody("text/plain".toMediaTypeOrNull()),
                    creo.element.toRequestBody("text/plain".toMediaTypeOrNull()),
                    creo.size.toRequestBody("text/plain".toMediaTypeOrNull()),
                    bitmap.toMultipartBody()
                )

                if (result.status == "success")
                    getAllCreo(userId)
                else
                    throw Exception(result.message)

            }catch (e: Exception){
                Log.d("MainViewModel", "Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun updateCreo(userId: String, creoId: Int, creo: Creo, bitmap: Bitmap){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var result = CreoApi.service.updateCreo(
                    userId,
                    creoId,
                    creo.name.toRequestBody("text/plain".toMediaTypeOrNull()),
                    creo.element.toRequestBody("text/plain".toMediaTypeOrNull()),
                    creo.size.toRequestBody("text/plain".toMediaTypeOrNull()),
                    bitmap.toMultipartBody()
                )

                if (result.status == "success")
                    getAllCreo(userId)
                else
                    throw Exception(result.message)
            }catch (e: Exception){
                Log.d("MainViewModel_Update", "Failure: ${e.message}")
            }
        }
    }
    fun deleteCreo(userId: String, id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var result = CreoApi.service.deleteCreo(userId, id)

                if (result.status == "success")
                    getAllCreo(userId)
                else
                    throw Exception(result.message)
            }catch (e: Exception){
                Log.d("MainViewModel", "Failure: ${e.message}")
            }
        }
    }

//    fun getCreo(creoId: Int){
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                _currentCreo.value = CreoApi.service.getCreo(creoId)
//            }catch (e: Exception){
//                Log.d("MainViewModel", "Failure: ${e.message}")
//            }
//        }
//    }
//    fun createCreo(newCreo: Creo){
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                _newCreo.value = CreoApi.service.createCreo(newCreo)
//            }catch (e: Exception){
//                Log.d("MainViewModel", "Failure: ${e.message}")
//            }
//        }
//    }

    private fun Bitmap.toMultipartBody(): MultipartBody.Part {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()
        val requestBody = byteArray.toRequestBody(
            "image/jpg".toMediaTypeOrNull(), 0 , byteArray.size
        )
        return MultipartBody.Part.createFormData(
            "image", "image.jpg", requestBody
        )
    }

    fun clearMessage(){
        errorMessage.value = null
    }
}