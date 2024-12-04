package com.example.arrendov1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.arrendov1.adapters.PropertyAdapter
import com.example.arrendov1.models.Property
import com.example.arrendov1.network.RetrofitClient
import com.example.arrendov1.network.UidRequest
import com.google.firebase.auth.FirebaseAuth
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.channel.Channel
import com.pusher.client.channel.SubscriptionEventListener
import com.pusher.client.channel.PusherEvent
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OwnerActivity : AppCompatActivity() {

    private lateinit var logoutButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PropertyAdapter
    private lateinit var pusher: Pusher
    private lateinit var notificationView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner)

        // Inicializar RecyclerView
        recyclerView = findViewById(R.id.rvProperties)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PropertyAdapter(emptyList())
        recyclerView.adapter = adapter

        // Inicializar el botón de logout
        logoutButton = findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // Obtener el UID del usuario autenticado y cargar las propiedades
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser

        if (currentUser != null) {
            val uid = currentUser.uid
            fetchProperties(uid)
        } else {
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show()
        }

        // Inicializar TextView para notificaciones


        // Configurar Pusher
        setupPusher()
    }
    private fun showCustomToast(title: String, message: String, duration: Int = 5000) {
        val inflater = layoutInflater
        val layout = inflater.inflate(R.layout.custom_toast, null)

        // Configura los textos del Toast
        val toastTitle = layout.findViewById<TextView>(R.id.tvToastTitle)
        val toastMessage = layout.findViewById<TextView>(R.id.tvToastMessage)

        toastTitle.text = title
        toastMessage.text = message

        // Crea el Toast
        val toast = Toast(applicationContext)
        toast.duration = Toast.LENGTH_LONG
        toast.view = layout

        // Mostrar el Toast repetidamente durante el tiempo especificado
        val handler = android.os.Handler()
        val startTime = System.currentTimeMillis()
        handler.post(object : Runnable {
            override fun run() {
                if (System.currentTimeMillis() - startTime < duration) {
                    toast.show()
                    handler.postDelayed(this, 1000) // Repite cada 1 segundo
                }
            }
        })
    }


    private fun fetchProperties(firebaseUid: String) {
        val uidRequest = UidRequest(firebaseUid)

        RetrofitClient.instance.getPropertiesByOwner(uidRequest)
            .enqueue(object : Callback<List<Property>> {
                override fun onResponse(
                    call: Call<List<Property>>,
                    response: Response<List<Property>>
                ) {
                    if (response.isSuccessful) {
                        val properties = response.body()
                        if (properties != null) {
                            adapter = PropertyAdapter(properties)
                            recyclerView.adapter = adapter
                            Toast.makeText(this@OwnerActivity, "Properties loaded successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@OwnerActivity, "No properties found", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@OwnerActivity, "Error: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<List<Property>>, t: Throwable) {
                    Log.e("OwnerActivity", "API call failed", t)
                    Toast.makeText(this@OwnerActivity, "Failed to load properties: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun setupPusher() {
        val options = PusherOptions().setCluster("us3")
        pusher = Pusher("b39c741a3e96a1a5b07e", options)

        // Suscribirse al canal
        val channel: Channel = pusher.subscribe("ArrendoMobile")
        Log.d("OwnerActivity", "Suscrito al canal ArrendoMobile")

        // Escuchar el evento "new-request"
        channel.bind("new-request", object : SubscriptionEventListener {
            override fun onEvent(event: PusherEvent) {
                runOnUiThread {
                    try {
                        // Parsear el JSON recibido
                        val jsonData = JSONObject(event.data)

                        // Obtén los campos enviados desde Laravel
                        val propertyAddress = jsonData.getString("propertyAddress") // Dirección de la propiedad
                        val tenantName = jsonData.getString("tenantName")           // Nombre del tenant
                        val message = jsonData.getString("message")                // Mensaje dinámico

                        // Muestra el mensaje en un Toast personalizado con duración extendida
                        showCustomToast(
                            "New Maintenace Request",
                            "Property: $propertyAddress\nTenant: $tenantName\n$message",
                            duration = 7000 // Duración en milisegundos (7 segundos)
                        )
                    } catch (e: Exception) {
                        Log.e("OwnerActivity", "Error al procesar el evento", e)
                    }
                }
            }
        })

        // Conectar a Pusher
        pusher.connect()
        Log.d("OwnerActivity", "Pusher conectado correctamente")
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            // Desconectar Pusher al destruir la actividad
            pusher.disconnect()
            Log.d("OwnerActivity", "Pusher desconectado correctamente")
        } catch (e: Exception) {
            Log.e("OwnerActivity", "Error al desconectar Pusher", e)
        }
    }
}
