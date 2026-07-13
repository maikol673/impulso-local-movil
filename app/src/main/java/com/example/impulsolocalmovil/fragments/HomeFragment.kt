package com.example.impulsolocalmovil.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.impulsolocalmovil.*

class HomeFragment : Fragment() {

    private lateinit var btnVerEmprendimientos: TextView
    private lateinit var btnPublicar: TextView
    private lateinit var btnAgregarTestimonio: TextView

    // Beneficios - Tarjetas clickeables
    private lateinit var cardCrecimiento: View
    private lateinit var cardNetworking: View
    private lateinit var cardFormacion: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Botones principales
        btnVerEmprendimientos = view.findViewById(R.id.btnVerEmprendimientos)
        btnPublicar = view.findViewById(R.id.btnPublicar)
        btnAgregarTestimonio = view.findViewById(R.id.btnAgregarTestimonio)

        // Tarjetas de beneficios
        cardCrecimiento = view.findViewById(R.id.cardCrecimiento)
        cardNetworking = view.findViewById(R.id.cardNetworking)
        cardFormacion = view.findViewById(R.id.cardFormacion)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        // Botón Ver Emprendimientos
        btnVerEmprendimientos.setOnClickListener {
            val intent = Intent(requireContext(), ListadoEmprendimientosActivity::class.java)
            startActivity(intent)
        }

        // Botón Publicar Emprendimiento
        btnPublicar.setOnClickListener {
            val intent = Intent(requireContext(), PublicarEmprendimientoActivity::class.java)
            startActivity(intent)
        }

        // Botón Compartir Testimonio
        btnAgregarTestimonio.setOnClickListener {
            val intent = Intent(requireContext(), CrearTestimonioActivity::class.java)
            startActivity(intent)
        }

        // Tarjeta: Crecimiento (📞)
        cardCrecimiento.setOnClickListener {
            // TODO: Ir a Beneficio Crecimiento
        }

        // Tarjeta: Networking (💡)
        cardNetworking.setOnClickListener {
            val intent = Intent(requireContext(), NetworkingActivity::class.java)
            startActivity(intent)
        }

        // Tarjeta: Formación (🔍)
        cardFormacion.setOnClickListener {
            val intent = Intent(requireContext(), FormacionActivity::class.java)
            startActivity(intent)
        }
    }
}