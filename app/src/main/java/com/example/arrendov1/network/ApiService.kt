package com.example.arrendov1.network

import com.example.arrendov1.models.MaintenanceRequest
import com.example.arrendov1.models.Property
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("/api/android/get-user-role")
    @Headers("Content-Type: application/json")
    fun getUserRole(@Body uidRequest: UidRequest): Call<UserRoleResponse>

    @POST("/api/android/maintenance-requests")
    @Headers("Content-Type: application/json")
    fun getMaintenanceRequests(@Body uidRequest: UidRequest): Call<List<MaintenanceRequest>>

    @POST("/api/android/get-owner-properties")
    @Headers("Content-Type: application/json")
    fun getPropertiesByOwner(@Body uidRequest: UidRequest): Call<List<Property>>

    @GET("/api/android/get-pending-requests")
    fun getPendingRequestsByProperty(
        @Query("property_id") propertyId: Int
    ): Call<List<MaintenanceRequest>>
}
