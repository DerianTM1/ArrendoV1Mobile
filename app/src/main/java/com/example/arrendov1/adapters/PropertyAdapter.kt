package com.example.arrendov1.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.arrendov1.R
import com.example.arrendov1.models.Property
import com.example.arrendov1.OwnerActivity
import com.example.arrendov1.PendingRequestsActivity

class PropertyAdapter(
    private val properties: List<Property>
) : RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_property, parent, false)
        return PropertyViewHolder(view)
    }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        val property = properties[position]

        // Configurar los datos de la propiedad
        holder.tvAddress.text = "${property.street}, ${property.number}, ${property.colony}, ${property.postal_code}"
        holder.tvState.text = "${property.city}, ${property.state}"
        holder.tvAvailability.text = "${property.availability}"

        // Botón para ver solicitudes de mantenimiento
        holder.btnViewRequests.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, PendingRequestsActivity::class.java)
            intent.putExtra("property_id", property.id)
            context.startActivity(intent)
            Log.d("PropertyAdapter", "Clicked on property ID: ${property.id}")


        }
    }

    override fun getItemCount(): Int = properties.size

    inner class PropertyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAddress: TextView = itemView.findViewById(R.id.tvAddress)
        val tvState: TextView = itemView.findViewById(R.id.tvState)
        val tvAvailability: TextView = itemView.findViewById(R.id.tvAvailability)
        val btnViewRequests: Button = itemView.findViewById(R.id.btnViewRequests)
    }
}
