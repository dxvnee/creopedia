package org.d3if3121.creopedia.network

import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.d3if3121.creopedia.model.Creo
import org.d3if3121.creopedia.model.OpStatus
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

private const val BASE_URL = "https://creopedia-a3bb75061510.herokuapp.com"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()
//

interface CreoApiService {

    @GET("/api/creo/{id}")
    suspend fun getCreo(
        @Header("Authorization") userId: String,
        @Path("id") id: Int
    ): Creo

    @GET("/api/creo/")
    suspend fun getCreos(
        @Header("Authorization") userId: String
    ): List<Creo>

    @Multipart
    @POST("/api/creo/createCreoWithImage")
    suspend fun createCreoWithImage(
        @Header("Authorization") userId: String,
        @Part("name") name: RequestBody,
        @Part("element") element: RequestBody,
        @Part("size") size: RequestBody,
        @Part image: MultipartBody.Part
    ): OpStatus

    @Multipart
    @PUT("/api/creo/{id}")
    suspend fun updateCreo(
        @Header("Authorization") userId: String,
        @Path("id") id: Int,
        @Part("name") name: RequestBody,
        @Part("element") element: RequestBody,
        @Part("size") size: RequestBody,
        @Part image: MultipartBody.Part
    ): OpStatus

    @DELETE("/api/creo/{id}")
    suspend fun deleteCreo(
        @Header("Authorization") userId: String,
        @Path("id") id: Int,
    ): OpStatus

//    @POST("/api/creo/")
//    suspend fun createCreo(
//        @Body creo:Creo
//    ): Creo
//
//    @GET("/api/creo/{id}/image")
//    suspend fun getCreoImage(
//        @Header("Authorization") userId: String,
//        @Path("id") id: Int
//    ): ResponseBody

}

object CreoApi {
    val service: CreoApiService by lazy {
        retrofit.create(CreoApiService::class.java)
    }

    fun getCreoUrl(id: String): String {
        val link = "$BASE_URL/api/creo/$id/image"
        return link
        Log.d("hehe",link)
//        "https://9736-2404-8000-1024-18ef-a510-14e6-a81b-7458.ngrok-free.app/api/creo/8/image"
    }
}

enum class ApiStatus { LOADING, SUCCESS, FAILED }