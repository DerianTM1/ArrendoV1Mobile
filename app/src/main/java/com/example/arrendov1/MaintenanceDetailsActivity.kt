package com.example.arrendov1

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.arrendov1.models.MaintenanceRequest

class MaintenanceDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maintenance_details)

        // Recuperar los datos pasados desde el adaptador
        val maintenanceRequest = intent.getSerializableExtra("maintenance_request") as? MaintenanceRequest


        // Referenciar las vistas
        val tvProperty: TextView = findViewById(R.id.tvProperty)
        val tvType: TextView = findViewById(R.id.tvType)
        val tvDescription: TextView = findViewById(R.id.tvDescription)
        val tvPriority: TextView = findViewById(R.id.tvPriority)
        val tvStatus: TextView = findViewById(R.id.tvStatus)
        val tvOwnerNote: TextView = findViewById(R.id.tvOwnerNote)
        val tvReportDate: TextView = findViewById(R.id.tvReportDate)
        val tvDispatchDate: TextView = findViewById(R.id.tvDispatchDate)
        val tvMaintenanceCost: TextView = findViewById(R.id.tvMaintenanceCost)

        // Nuevas vistas para datos del Tenant
        val tvTenantName: TextView = findViewById(R.id.tvTenantName)
        val tvTenantPhone: TextView = findViewById(R.id.tvTenantPhone)
        val tvTenantNameLabel: TextView = findViewById(R.id.tvTenantNameLabel)
        val tvTenantPhoneLabel: TextView = findViewById(R.id.tvTenantPhoneLabel)

        // Mostrar los datos si están disponibles
        maintenanceRequest?.let {
            tvProperty.text = "${it.property.street}, ${it.property.city}, ${it.property.state}"
            tvType.text = it.type
            tvDescription.text = it.description
            tvPriority.text = it.priority

            // Cambiar el color según la prioridad
            val priorityColor = when (it.priority) {
                "High" -> R.color.priority_high
                "Low" -> R.color.priority_low
                else -> R.color.priority_medium
            }
            tvPriority.setTextColor(getColor(priorityColor))

            tvStatus.text = it.status

            // Cambiar el color según el estado
            val statusColor = when (it.status) {
                "Pending" -> R.color.status_pending
                "In Progress" -> R.color.status_in_progress
                "Completed" -> R.color.status_completed
                else -> R.color.status_pending
            }
            tvStatus.setTextColor(getColor(statusColor))

            tvOwnerNote.text = it.owner_note ?: "No notes available"
            tvReportDate.text = it.report_date
            tvDispatchDate.text = it.date_review ?: "Not reviewed yet"

            tvMaintenanceCost.text = "${it.maintenance_cost ?: "N/A"} Pesos"
            tvMaintenanceCost.setTextColor(getColor(R.color.green_cost)) // Color verde para costos



        }
    }
}
