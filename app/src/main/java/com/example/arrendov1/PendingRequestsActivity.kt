package com.example.arrendov1

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.arrendov1.adapters.MaintenanceRequestAdapter
import com.example.arrendov1.adapters.OwnerMaintenanceRequestAdapter
import com.example.arrendov1.models.MaintenanceRequest
import com.example.arrendov1.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PendingRequestsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OwnerMaintenanceRequestAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pending_requests)

        // Inicializar RecyclerView
        recyclerView = findViewById(R.id.rvPendingRequests)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inicializar adaptador vacío
        adapter = OwnerMaintenanceRequestAdapter(emptyList())
        recyclerView.adapter = adapter

        // Obtener property_id del intent
        val propertyId = intent.getIntExtra("property_id", -1)

        if (propertyId != -1) {
            Log.d("PendingRequestsActivity", "Property ID received: $propertyId")
            fetchPendingRequestsWithRetry(propertyId)
        } else {
            Toast.makeText(this, "Invalid property ID", Toast.LENGTH_SHORT).show()
            Log.e("PendingRequestsActivity", "No valid property ID received")
        }
    }

    private fun fetchPendingRequestsWithRetry(propertyId: Int, retryCount: Int = 3) {
        RetrofitClient.instance.getPendingRequestsByProperty(propertyId)
            .enqueue(object : Callback<List<MaintenanceRequest>> {
                override fun onResponse(
                    call: Call<List<MaintenanceRequest>>,
                    response: Response<List<MaintenanceRequest>>
                ) {
                    if (response.isSuccessful) {
                        val requests = response.body()
                        if (requests != null) {
                            adapter = OwnerMaintenanceRequestAdapter(requests)
                            recyclerView.adapter = adapter
                        } else {
                            Toast.makeText(this@PendingRequestsActivity, "No pending requests found", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(
                            this@PendingRequestsActivity,
                            "Error: ${response.code()} - ${response.message()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<MaintenanceRequest>>, t: Throwable) {
                    if (retryCount > 0) {
                        fetchPendingRequestsWithRetry(propertyId, retryCount - 1)
                    } else {
                        Log.e("PendingRequestsActivity", "Failed to fetch data", t)
                        Toast.makeText(this@PendingRequestsActivity, "Failed to load data: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }
}
