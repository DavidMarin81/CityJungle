package com.example.cityjungle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.cityjungle.databinding.ActivityLoginBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db : FirebaseFirestore = FirebaseFirestore.getInstance()

        binding.btnAccesoEntrar.setOnClickListener {
            var encontrado = false
            var pagado = false
            db.collection("usuarios")
                .get()
                .addOnSuccessListener { resultado ->
                    for (documento in resultado) {
                        if (binding.etAccesoEmail.getText().toString() == documento.get("email").toString()
                            && binding.etAccesoContrasena.getText().toString() == documento.get("contrasena").toString()) {
                            encontrado = true
                            if (documento.get("pagado").toString() == "pagado") {
                                pagado = true
                            }
                        }
                    }
                    if (encontrado && pagado) {
                        Toast.makeText(this, "Bienvenido a nuestra app", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "No tiene acceso a la app", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "ERROR DE CONEXION", Toast.LENGTH_SHORT).show()
                }
        }
    }
}