package com.example.arrendov1.models
import java.io.Serializable

data class MaintenanceRequest(
    val id: Int,
    val property_id: Int,
    val tenant_user_id: Int,
    val type: String,
    val description: String,
    val report_date: String,
    val priority: String,
    val status: String,
    val evidence: String?,
    val owner_note: String?,
    val maintenance_cost: String?,
    val date_review: String?,
    val created_at: String,
    val updated_at: String,
    val property: Property,
    val tenant_user: tenant_user?
) : Serializable

data class tenant_user(
    val first_name: String,
    val last_name: String,
    val phone: String?
) : Serializable