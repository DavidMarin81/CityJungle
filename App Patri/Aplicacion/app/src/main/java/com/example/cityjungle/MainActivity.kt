package com.example.cityjungle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.cityjungle.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Patterns
import java.util.regex.Pattern


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val db : FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCrearCuenta.setOnClickListener {
            var nombre = binding.etCrearNombre.getText().toString()
            var email = binding.etCrearEmail.getText().toString()
            var contrasena = binding.etCrearContrasena.getText().toString()

            if (nombre.isNotBlank() && email.isNotBlank() && contrasena.isNotBlank()) {
                if (comprobarEmail(email) && comprobarContrasena(contrasena)) {
                    val datos = hashMapOf(
                        "contrasena" to contrasena,
                        "email" to email,
                        "pagado" to "pagado"
                    )
                        db.collection("usuarios")
                            .document(nombre)
                            .set(datos)
                            .addOnSuccessListener { resultado ->
                                Toast.makeText(this, "Cuenta creada con éxito", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { exception ->
                                Toast.makeText(this, "ERROR DE CONEXION", Toast.LENGTH_SHORT).show()
                            }

                } else {
                    Toast.makeText(this, "Datos mal introducidos", Toast.LENGTH_SHORT).show()
                    resetearDatos()
                }
            } else {
                Toast.makeText(this, "No se permiten campos vacios", Toast.LENGTH_SHORT).show()
                resetearDatos()
            }
        }

        binding.tvTengoCuenta.setOnClickListener {
            startActivity(Intent(this, LoginActivity :: class.java))
            resetearDatos()
        }
    }

    private fun comprobarNombre(nombre : String) : Boolean {
        val patronNombre = Regex("[a-z] + ", RegexOption.IGNORE_CASE)
        return patronNombre.matches(nombre)
    }

    private fun comprobarEmail(email: String): Boolean {
        val pattern : Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    private fun comprobarContrasena(contrasena : String) : Boolean {
        var correcto = true
        if (contrasena.length < 4) {
            correcto = false
        }
        return correcto
    }

    private fun resetearDatos() {
        binding.etCrearNombre.setText("")
        binding.etCrearEmail.setText("")
        binding.etCrearContrasena.setText("")
    }

}