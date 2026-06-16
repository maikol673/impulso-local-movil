package com.example.impulsolocalmovil.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.impulsolocalmovil.R

class AdminDashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvTotalEmprendimientos = view.findViewById<TextView>(R.id.tvTotalEmprendimientos)
        val tvUsuariosActivos = view.findViewById<TextView>(R.id.tvUsuariosActivos)
        val tvTotalCategorias = view.findViewById<TextView>(R.id.tvTotalCategorias)
        val tvTotalTestimonios = view.findViewById<TextView>(R.id.tvTotalTestimonios)

        // Datos de prueba
        tvTotalEmprendimientos.text = "24"
        tvUsuariosActivos.text = "156"
        tvTotalCategorias.text = "8"
        tvTotalTestimonios.text = "42"
    }
}