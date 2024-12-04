package com.example.arrendov1.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.arrendov1.MaintenanceDetailsActivity
import com.example.arrendov1.R
import com.example.arrendov1.models.MaintenanceRequest
import java.io.Serializable

class MaintenanceRequestAdapter(
    private val maintenanceRequests: List<MaintenanceRequest>,
) : RecyclerView.Adapter<MaintenanceRequestAdapter.MaintenanceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaintenanceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_maintenance_request, parent, false)
        return MaintenanceViewHolder(view)
    }

    override fun onBindViewHolder(holder: MaintenanceViewHolder, position: Int) {
        val maintenanceRequest = maintenanceRequests[position] // Corregí el nombre de la variable

        holder.tvType.text = "Type: ${maintenanceRequest.type}"
        holder.tvDescription.text = maintenanceRequest.description
        holder.tvReportedDate.text = "Reported Date: ${maintenanceRequest.report_date}"
        holder.tvPriority.text = "Priority: ${maintenanceRequest.priority}"

        // Cambiar color del texto según prioridad
        val priorityColor = when (maintenanceRequest.priority) {
            "High" -> R.color.priority_high
            "Low" -> R.color.priority_low
            else -> R.color.priority_medium
        }
        holder.tvPriority.setTextColor(holder.itemView.context.getColor(priorityColor))

        // Cambiar color del texto según el estado
        val statusColor = when (maintenanceRequest.status) {
            "Pending" -> R.color.status_pending
            "In Progress" -> R.color.status_in_progress
            "Completed" -> R.color.status_completed
            else -> R.color.status_pending
        }
        holder.tvStatus.setTextColor(holder.itemView.context.getColor(statusColor))
        holder.tvStatus.text = "Status: ${maintenanceRequest.status}"

        // Configurar el botón "View Details"
        holder.viewDetailsButton.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, MaintenanceDetailsActivity::class.java)

            // Pasar los datos de la solicitud seleccionada
            intent.putExtra("maintenance_request", maintenanceRequest) // Cambié el objeto a `maintenanceRequest`

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = maintenanceRequests.size

    inner class MaintenanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvType: TextView = itemView.findViewById(R.id.tvType)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val tvReportedDate: TextView = itemView.findViewById(R.id.tvReportedDate)
        val tvPriority: TextView = itemView.findViewById(R.id.tvPriority)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val viewDetailsButton: Button = itemView.findViewById(R.id.btnViewDetails) // Nuevo botón
    }
}
