package com.example.arrendov1.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.arrendov1.MaintenanceOwnerDetailsActivity
import com.example.arrendov1.R
import com.example.arrendov1.models.MaintenanceRequest
import java.io.Serializable

class OwnerMaintenanceRequestAdapter(
    private val maintenanceRequests: List<MaintenanceRequest>
) : RecyclerView.Adapter<OwnerMaintenanceRequestAdapter.MaintenanceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaintenanceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_owner_maintenance_request, parent, false)
        return MaintenanceViewHolder(view)
    }

    override fun onBindViewHolder(holder: MaintenanceViewHolder, position: Int) {
        val maintenanceRequest = maintenanceRequests[position]

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

        // Configurar el botón "View Details"
        holder.viewDetailsButton.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, MaintenanceOwnerDetailsActivity::class.java)

            // Pasar los datos de la solicitud seleccionada
            intent.putExtra("maintenance_request", maintenanceRequest)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = maintenanceRequests.size

    inner class MaintenanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvType: TextView = itemView.findViewById(R.id.tvType)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val tvReportedDate: TextView = itemView.findViewById(R.id.tvReportedDate)
        val tvPriority: TextView = itemView.findViewById(R.id.tvPriority)
        val viewDetailsButton: Button = itemView.findViewById(R.id.btnViewDetails)
    }
}
