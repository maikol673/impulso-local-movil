package com.example.impulsolocalmovil.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.impulsolocalmovil.*
import com.example.impulsolocalmovil.api.RetrofitClient
import com.example.impulsolocalmovil.models.Testimonio
import com.example.impulsolocalmovil.utils.TokenManager
import kotlinx.coroutines.*

class HomeFragment : Fragment() {

    private lateinit var btnVerEmprendimientos: TextView
    private lateinit var btnPublicar: TextView
    private lateinit var btnAgregarTestimonio: TextView
    private lateinit var cardNetworking: View
    private lateinit var cardFormacion: View
    private lateinit var layoutTestimonios: LinearLayout
    private lateinit var layoutNoTestimonios: LinearLayout

    private lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tokenManager = TokenManager.getInstance(requireContext())

        btnVerEmprendimientos = view.findViewById(R.id.btnVerEmprendimientos)
        btnPublicar = view.findViewById(R.id.btnPublicar)
        btnAgregarTestimonio = view.findViewById(R.id.btnAgregarTestimonio)
        cardNetworking = view.findViewById(R.id.cardNetworking)
        cardFormacion = view.findViewById(R.id.cardFormacion)
        layoutTestimonios = view.findViewById(R.id.layoutTestimonios)
        layoutNoTestimonios = view.findViewById(R.id.layoutNoTestimonios)

        setupClickListeners()
        cargarTestimonios()
    }

    private fun setupClickListeners() {
        btnVerEmprendimientos.setOnClickListener {
            startActivity(Intent(requireContext(), ListadoEmprendimientosActivity::class.java))
        }

        btnPublicar.setOnClickListener {
            startActivity(Intent(requireContext(), PublicarEmprendimientoActivity::class.java))
        }

        btnAgregarTestimonio.setOnClickListener {
            startActivity(Intent(requireContext(), CrearTestimonioActivity::class.java))
        }

        cardNetworking.setOnClickListener {
            startActivity(Intent(requireContext(), NetworkingActivity::class.java))
        }

        cardFormacion.setOnClickListener {
            startActivity(Intent(requireContext(), FormacionActivity::class.java))
        }
    }

    private fun cargarTestimonios() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.getTestimonials()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val testimonios = response.body() ?: emptyList()
                        mostrarTestimonios(testimonios)
                    } else {
                        layoutNoTestimonios.visibility = View.VISIBLE
                        layoutTestimonios.visibility = View.GONE
                    }
                }
            } catch (e: Exception) {
                layoutNoTestimonios.visibility = View.VISIBLE
                layoutTestimonios.visibility = View.GONE
            }
        }
    }

    private fun mostrarTestimonios(testimonios: List<Testimonio>) {
        android.util.Log.d("HomeFragment", "Mostrando ${testimonios.size} testimonios")

        if (testimonios.isEmpty()) {
            layoutNoTestimonios.visibility = View.VISIBLE
            layoutTestimonios.visibility = View.GONE
            return
        }

        layoutNoTestimonios.visibility = View.GONE
        layoutTestimonios.visibility = View.VISIBLE
        layoutTestimonios.removeAllViews()

        val testimoniosMostrar = if (testimonios.size > 2) testimonios.take(2) else testimonios

        testimoniosMostrar.forEach { testimonio ->
            val testimonioView = layoutInflater.inflate(R.layout.item_testimonio, null)
            val tvContenido = testimonioView.findViewById<TextView>(R.id.tvTestimonioContenido)
            val tvAutor = testimonioView.findViewById<TextView>(R.id.tvTestimonioAutor)
            val btnEliminar = testimonioView.findViewById<Button>(R.id.btnEliminarTestimonio)

            tvContenido.text = "\"${testimonio.contenido}\""
            val empresa = testimonio.empresa?.let { ", $it" } ?: ""
            tvAutor.text = "- ${testimonio.nombre}$empresa"

            val usuarioId = tokenManager.getUser()?.id ?: 0
            android.util.Log.d("HomeFragment", "Usuario ID: $usuarioId, Testimonio usuarioId: ${testimonio.usuarioId}")

            if (usuarioId == testimonio.usuarioId) {
                android.util.Log.d("HomeFragment", "Mostrando botón eliminar para testimonio ${testimonio.id}")
                btnEliminar.visibility = View.VISIBLE
                btnEliminar.setOnClickListener {
                    android.util.Log.d("HomeFragment", "Click en eliminar testimonio ${testimonio.id}")
                    eliminarTestimonio(testimonio.id)
                }
            } else {
                android.util.Log.d("HomeFragment", "Ocultando botón eliminar para testimonio ${testimonio.id}")
                btnEliminar.visibility = View.GONE
            }

            layoutTestimonios.addView(testimonioView)
        }
    }

    private fun eliminarTestimonio(testimonioId: Int) {
        val usuarioId = tokenManager.getUser()?.id ?: 0

        if (usuarioId == 0) {
            Toast.makeText(requireContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.deleteTestimonial(testimonioId, usuarioId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                            "✅ Testimonio eliminado",
                            Toast.LENGTH_SHORT
                        ).show()
                        cargarTestimonios()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Toast.makeText(
                            requireContext(),
                            "❌ Error: ${response.code()} - $errorBody",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        "❌ Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}