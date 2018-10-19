package org.lwr.edgarapp.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import org.lwr.edgarapp.CalendarioDetalle
import org.lwr.edgarapp.R
import org.lwr.edgarapp.models.calendarioM


class AdapterCalendario(private val mContext: Activity, private val listCalendario: List<calendarioM>) : RecyclerView.Adapter<AdapterCalendario.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txFecha: TextView? = null
        var txTitulo: TextView? = null
        var txDescripcion: TextView?=null
        var rlCiclo: LinearLayout?=null
        var txCategoria: TextView?=null
        var encabezado: RelativeLayout?=null
        var cont: RelativeLayout?=null

        init {
            txFecha = view.findViewById(R.id.txFecha) as TextView
            txTitulo = view.findViewById(R.id.txTitulo) as TextView
            txDescripcion = view.findViewById(R.id.txDescripcion) as TextView
            rlCiclo = view.findViewById(R.id.rlCiclo) as LinearLayout
            txCategoria = view.findViewById(R.id.txCategoria) as TextView
            encabezado = view.findViewById(R.id.encabezado) as RelativeLayout
            cont = view.findViewById(R.id.cont) as RelativeLayout
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_calendario, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val calendarioItem = listCalendario[position]

        holder.txFecha!!.setText(calendarioItem.fecha)
        holder.txTitulo!!.setText(calendarioItem.titulo)
        holder.txDescripcion!!.setText(calendarioItem.descripcion)
        holder.txCategoria!!.setText(calendarioItem.categoria)

        if (calendarioItem.cicloLunar == true) {
            holder.rlCiclo!!.visibility =  View.VISIBLE
        }else {
            holder.rlCiclo!!.visibility =  View.GONE
        }

        var sdk = android.os.Build.VERSION.SDK_INT

        if (calendarioItem.tipo == 1) {
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.encabezado!!.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.bcalendario_uno) );
            } else {
                holder. encabezado!!.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bcalendario_uno));
            }

            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.cont!!.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.calendario_uno) );
            } else {
                holder. cont!!.setBackground(ContextCompat.getDrawable(mContext, R.drawable.calendario_uno));
            }


        }else  if (calendarioItem.tipo == 2) {
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.encabezado!!.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.bcalendario_dos) );
            } else {
                holder. encabezado!!.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bcalendario_dos));
            }

            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.cont!!.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.calendario_dos) );
            } else {
                holder. cont!!.setBackground(ContextCompat.getDrawable(mContext, R.drawable.calendario_dos));
            }
        }else  if (calendarioItem.tipo == 3) {
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.encabezado!!.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.bcalendario_tres) );
            } else {
                holder. encabezado!!.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bcalendario_tres));
            }

            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.cont!!.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.calendario_tres) );
            } else {
                holder. cont!!.setBackground(ContextCompat.getDrawable(mContext, R.drawable.calendario_tres));
            }
        }else  if (calendarioItem.tipo == 4) {
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.encabezado!!.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.bcalendario_cuatro) );
            } else {
                holder. encabezado!!.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bcalendario_cuatro));
            }

            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.cont!!.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.calendario_cuatro) );
            } else {
                holder. cont!!.setBackground(ContextCompat.getDrawable(mContext, R.drawable.calendario_cuatro));
            }
        }


        holder.cont!!.setOnClickListener(View.OnClickListener {
            val DishDetailWindows = Intent(mContext, CalendarioDetalle::class.java)
            //DishDetailWindows.putExtra("idRestaurante", Dishes.id)
            //DetalleVentana.putExtra("idSucursal", listGaleria.get(position).getSucursal_id())
            mContext.startActivity(DishDetailWindows)
        })


    }

    override fun getItemCount(): Int {
        return listCalendario.size
    }
}
