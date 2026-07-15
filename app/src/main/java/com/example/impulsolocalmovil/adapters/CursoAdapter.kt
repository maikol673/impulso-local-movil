package com.example.impulsolocalmovil.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.impulsolocalmovil.R
import com.example.impulsolocalmovil.models.InscripcionCurso

class CursoAdapter(
    private var items: List<InscripcionCurso>,
    private val onItemClick: (InscripcionCurso) -> Unit
) : RecyclerView.Adapter<CursoAdapter.ViewHolder>() {

    class ViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvDescripcion: TextView = itemView.findViewById(R.id.tvDescripcion)
        val tvInstructor: TextView = itemView.findViewById(R.id.tvInstructor)
        val tvDuracion: TextView = itemView.findViewById(R.id.tvDuracion)
        val tvNivel: TextView = itemView.findViewById(R.id.tvNivel)
        val btnVerDetalle: Button = itemView.findViewById(R.id.btnVerDetalle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_curso_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val curso = item.curso

        if (curso != null) {
            holder.tvNombre.text = curso.nombre
            holder.tvDescripcion.text = curso.descripcion
            holder.tvInstructor.text = "👨‍🏫 ${curso.instructor}"
            holder.tvDuracion.text = "⏱️ ${curso.duracion}"
            holder.tvNivel.text = curso.nivel

            holder.btnVerDetalle.setOnClickListener {
                onItemClick(item)
            }
            holder.itemView.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    override fun getItemCount() = items.size

    fun updateList(newItems: List<InscripcionCurso>) {
        items = newItems
        notifyDataSetChanged()
    }
}