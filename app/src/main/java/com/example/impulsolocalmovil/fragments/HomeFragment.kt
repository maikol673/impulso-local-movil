package com.example.impulsolocalmovil.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.impulsolocalmovil.CrearTestimonioActivity
import com.example.impulsolocalmovil.ListadoEmprendimientosActivity
import com.example.impulsolocalmovil.PublicarEmprendimientoActivity
import com.example.impulsolocalmovil.R

class HomeFragment : Fragment() {

    private lateinit var btnVerEmprendimientos: TextView
    private lateinit var btnPublicar: TextView
    private lateinit var btnAgregarTestimonio: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnVerEmprendimientos = view.findViewById(R.id.btnVerEmprendimientos)
        btnPublicar = view.findViewById(R.id.btnPublicar)
        btnAgregarTestimonio = view.findViewById(R.id.btnAgregarTestimonio)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        btnVerEmprendimientos.setOnClickListener {
            val intent = Intent(requireContext(), ListadoEmprendimientosActivity::class.java)
            startActivity(intent)
        }

        btnPublicar.setOnClickListener {
            val intent = Intent(requireContext(), PublicarEmprendimientoActivity::class.java)
            startActivity(intent)
        }

        btnAgregarTestimonio.setOnClickListener {
            val intent = Intent(requireContext(), CrearTestimonioActivity::class.java)
            startActivity(intent)
        }
    }
}