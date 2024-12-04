package com.example.arrendov1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.arrendov1.network.ApiService
import com.example.arrendov1.network.UidRequest
import com.example.arrendov1.network.UserRoleResponse
import com.google.firebase.auth.FirebaseAuth
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.channel.Channel
import com.pusher.client.channel.PusherEvent
import com.pusher.client.channel.SubscriptionEventListener
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var pusher: Pusher // Instancia de Pusher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Configurar elementos ui
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)

        // Configurar acción del botón de inicio de sesión
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Configurar Pusher
        setupPusher()
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        fetchUserRole(user.uid) // Obtener el uid del usuario autenticado
                    }
                } else {
                    Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun fetchUserRole(firebaseUid: String) {
        // Configurar Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        // Crear la solicitud
        val uidRequest = UidRequest(firebaseUid)
        val call = apiService.getUserRole(uidRequest)

        // Enviar la solicitud al backend
        call.enqueue(object : Callback<UserRoleResponse> {
            override fun onResponse(call: Call<UserRoleResponse>, response: Response<UserRoleResponse>) {
                if (response.isSuccessful) {
                    val userRoleResponse = response.body()
                    if (userRoleResponse != null) {
                        when (userRoleResponse.role) {
                            "Owner" -> {
                                // Redirigir a OwnerActivity
                                startActivity(Intent(this@MainActivity, OwnerActivity::class.java))
                                finish() // Cierra MainActivity
                            }
                            "Tenant" -> {
                                // Redirigir a TenantActivity
                                startActivity(Intent(this@MainActivity, TenantActivity::class.java))
                                finish() // Cierra MainActivity
                            }
                            else -> {
                                Toast.makeText(this@MainActivity, "Unknown role", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Error: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<UserRoleResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Failed to connect: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setupPusher() {
        // Configurar Pusher con las opciones correctas
        val options = PusherOptions().setCluster("us3") // Cluster configurado en tu instancia de Pusher
        pusher = Pusher("b39c741a3e96a1a5b07e", options) // Usa tu API Key de Pusher

        // Suscribirse a un canal (en este caso, 'test-channel')
        val channel: Channel = pusher.subscribe("ArrendoMobile")

        // Escuchar eventos en el canal
        channel.bind("App\\Events\\TestEvent", object : SubscriptionEventListener {
            override fun onEvent(event: PusherEvent) {
                Log.d("Pusher", "Evento recibido: ${event.data}")
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Evento recibido: ${event.data}", Toast.LENGTH_LONG).show()
                }
            }
        })

        // Conectar a Pusher
        pusher.connect()
    }

    override fun onDestroy() {
        super.onDestroy()
        pusher.disconnect() // Desconectar Pusher al destruir la actividad
    }
}
