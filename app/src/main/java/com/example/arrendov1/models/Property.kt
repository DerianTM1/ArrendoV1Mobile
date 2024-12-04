package com.example.arrendov1.models

import java.io.Serializable

data class Property(
    val id: Int,
    val street: String,
    val number: String,
    val city: String,
    val state: String,
    val postal_code: String,
    val rental_rate: String?,
    val availability: String,
    val total_bathrooms: Int,
    val total_rooms: Int,
    val total_m2: Int,
    val have_parking: Boolean,
    val accept_mascots: Boolean,
    val property_price: String?,
    val property_details: String?,
    val property_photos_path: String?,
    val owner_user_id: Int,
    val zone_id: Int,
    val created_at: String,
    val updated_at: String,
    val colony: String,
    val half_bathrooms: Int,
    val surface_built: Int,
    val total_surface: Int,
    val antiquity: Int,
    val maintenance: String?,
    val state_conservation: String,
    val wineries: Int,
    val closets: Int,
    val levels: Int
) : Serializable