package org.lwr.edgarapp.adapter

import android.app.Activity
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import org.lwr.edgarapp.R
import org.lwr.edgarapp.models.documentosM


class AdapterDocumentos(private val mContext: Activity, private val listCalendario: List<documentosM>) : RecyclerView.Adapter<AdapterDocumentos.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txTitulo: TextView? = null

        init {
            txTitulo = view.findViewById(R.id.txTitulo) as TextView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_documentos, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val calendarioItem = listCalendario[position]

        holder.txTitulo!!.setText(calendarioItem.titulo)
        
        /*holder.poFavorito.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, boolean checked) {

                Log.e("valor", "click " + checked);
            }
        });*/
        /*holder.nombre.setText(album.getNombre());
        holder.precio.setText("$"+album.getPrecio());
        holder.descripcion.setText(album.getDescripcion());*/

    }

    override fun getItemCount(): Int {
        return listCalendario.size
    }
}
