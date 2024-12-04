package com.example.arrendov1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.arrendov1.adapters.MaintenanceRequestAdapter
import com.example.arrendov1.models.MaintenanceRequest
import com.example.arrendov1.network.RetrofitClient
import com.example.arrendov1.network.UidRequest
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TenantActivity : AppCompatActivity() {

    private lateinit var logoutButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MaintenanceRequestAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tenant)

        // Inicializar el RecyclerView
        recyclerView = findViewById(R.id.rvMaintenanceRequests)
        recyclerView.layoutManager = LinearLayoutManager(this) // Cambiar a LinearLayoutManager

        // Configurar el adaptador inicial con una lista vacía
        adapter = MaintenanceRequestAdapter(emptyList())
        recyclerView.adapter = adapter

        // Inicializar el botón de logout
        logoutButton = findViewById(R.id.logoutButton)

        // Configurar acción del botón de logout
        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish() // Cierra esta actividad
        }

        // Obtener el UID del usuario autenticado y cargar las solicitudes
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser

        if (currentUser != null) {
            val uid = currentUser.uid
            fetchMaintenanceRequests(uid)
        } else {
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchMaintenanceRequests(firebaseUid: String) {
        // Crear solicitud
        val uidRequest = UidRequest(firebaseUid)

        // Llamar a la API
        RetrofitClient.instance.getMaintenanceRequests(uidRequest)
            .enqueue(object : Callback<List<MaintenanceRequest>> {
                override fun onResponse(
                    call: Call<List<MaintenanceRequest>>,
                    response: Response<List<MaintenanceRequest>>
                ) {
                    if (response.isSuccessful) {
                        val maintenanceRequests = response.body()
                        if (maintenanceRequests != null) {
                            // Actualizar el adaptador con los datos recibidos
                            adapter = MaintenanceRequestAdapter(maintenanceRequests)
                            recyclerView.adapter = adapter
                            Toast.makeText(this@TenantActivity, "Data loaded successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@TenantActivity, "No data found", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@TenantActivity, "Error: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<List<MaintenanceRequest>>, t: Throwable) {
                    Log.e("TenantActivity", "API call failed", t)
                    Toast.makeText(this@TenantActivity, "Failed to connect: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
    }
}
