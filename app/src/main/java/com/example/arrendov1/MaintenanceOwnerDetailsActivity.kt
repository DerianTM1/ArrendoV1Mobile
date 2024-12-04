package com.example.arrendov1

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.arrendov1.models.MaintenanceRequest

class MaintenanceOwnerDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maintenance_owner_details)

        // Recuperar los datos pasados desde el adaptador
        val maintenanceRequest = intent.getSerializableExtra("maintenance_request") as? MaintenanceRequest

        // Referenciar las vistas
        val tvProperty: TextView = findViewById(R.id.tvProperty)
        val tvTenantName: TextView = findViewById(R.id.tvTenantName)
        val tvTenantPhone: TextView = findViewById(R.id.tvTenantPhone)
        val tvType: TextView = findViewById(R.id.tvType)
        val tvDescription: TextView = findViewById(R.id.tvDescription)
        val tvPriority: TextView = findViewById(R.id.tvPriority)
        val tvStatus: TextView = findViewById(R.id.tvStatus)

        val tvReportDate: TextView = findViewById(R.id.tvReportDate)

        val tvMaintenanceCost: TextView = findViewById(R.id.tvMaintenanceCost)

        // Mostrar los datos si están disponibles
        maintenanceRequest?.let {
            tvProperty.text = "${it.property.street}, ${it.property.city}, ${it.property.state}"
            tvTenantName.text = "${it.tenant_user?.first_name ?: "N/A"} ${it.tenant_user?.last_name ?: "N/A"}"
            tvTenantPhone.text = it.tenant_user?.phone ?: "Phone not available"
            tvType.text = it.type
            tvDescription.text = it.description
            tvPriority.text = it.priority
            tvStatus.text = it.status
            tvReportDate.text = it.report_date
            tvMaintenanceCost.text = "${it.maintenance_cost ?: "N/A"} Pesos"
        }
    }
}
