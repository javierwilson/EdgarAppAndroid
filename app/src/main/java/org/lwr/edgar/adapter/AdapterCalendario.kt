package org.lwr.edgar.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.lwr.edgar.CalendarioDetalle
import org.lwr.edgar.R
import org.lwr.edgar.models.calendarioM
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.*




class AdapterCalendario(private val mContext: Activity, private val listCalendario: List<calendarioM>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_ITEM = 1
    private val VIEW_PROG = 0

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txFecha: TextView? = null
        var txTitulo: TextView? = null
        var txDescripcion: TextView?=null
        var rlCiclo: LinearLayout?=null
        var txCategoria: TextView?=null
        var encabezado: RelativeLayout?=null
        var cont: RelativeLayout?=null

        var moon_uno: ImageView? = null
        var moon_dos: ImageView? = null
        var moon_tres: ImageView? = null
        var moon_cuatro: ImageView? = null

        init {
            txFecha = view.findViewById(R.id.txFecha) as TextView
            txTitulo = view.findViewById(R.id.txTitulo) as TextView
            txDescripcion = view.findViewById(R.id.txDescripcion) as TextView
            rlCiclo = view.findViewById(R.id.rlCiclo) as LinearLayout
            txCategoria = view.findViewById(R.id.txCategoria) as TextView
            encabezado = view.findViewById(R.id.encabezado) as RelativeLayout
            cont = view.findViewById(R.id.cont) as RelativeLayout

            moon_uno = view.findViewById(R.id.moon_uno) as ImageView
            moon_dos = view.findViewById(R.id.moon_dos) as ImageView
            moon_tres = view.findViewById(R.id.moon_tres) as ImageView
            moon_cuatro = view.findViewById(R.id.moon_cuatro) as ImageView

        }
    }

    inner class ProgressViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        var progressBar: ProgressBar? = null

        init {
            progressBar = view.findViewById(R.id.progressBar) as ProgressBar
        }
    }

    override fun getItemViewType(position: Int): Int {
        System.out.println("prueba "+listCalendario.get(position).id)
        return (if (listCalendario.get(position).id != "") VIEW_ITEM else VIEW_PROG).toInt()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder

        if (viewType === VIEW_ITEM) {
            val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_calendario, parent, false)

            vh = MyViewHolder(itemView)
        } else {
            val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.progress_item, parent, false)

            vh = ProgressViewHolder(itemView)
        }

        return vh

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


        if (holder is MyViewHolder) {

            val calendarioItem = listCalendario[position]

            holder.txFecha!!.setText(calendarioItem.start_date+" - "+calendarioItem.end_date)
            holder.txTitulo!!.setText(calendarioItem.title)
            holder.txDescripcion!!.setText(calendarioItem.descripcion)
            holder.txCategoria!!.setText(calendarioItem.name)

            /*if (calendarioItem.cicloLunar == true) {
                holder.rlCiclo!!.visibility =  View.VISIBLE
            }else {
                holder.rlCiclo!!.visibility =  View.GONE
            }*/

            var sdk = Build.VERSION.SDK_INT


            setDrawableFilterColor(mContext, Color.parseColor(calendarioItem.color), holder.encabezado!!.getBackground())

            if(sdk < Build.VERSION_CODES.JELLY_BEAN) {
                holder.cont!!.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.calendario_uno) );
            } else {
                holder. cont!!.setBackground(ContextCompat.getDrawable(mContext, R.drawable.calendario_uno));
            }


            if(calendarioItem.first_moon_cycle!= 0){
                holder.moon_uno!!.visibility = View.VISIBLE
            }else{
                holder.moon_uno!!.visibility = View.GONE
            }

            if(calendarioItem.second_moon_cycle!= 0){
                holder.moon_dos!!.visibility = View.VISIBLE

            }else{
                holder.moon_dos!!.visibility = View.GONE
            }

            if(calendarioItem.third_moon_cycle!= 0){
                holder.moon_tres!!.visibility = View.VISIBLE

            }else{
                holder.moon_tres!!.visibility = View.GONE
            }

            if(calendarioItem.four_moon_cycle!= 0){
                holder.moon_cuatro!!.visibility = View.VISIBLE

            }else{
                holder.moon_cuatro!!.visibility = View.GONE
            }

            holder.cont!!.setOnClickListener(View.OnClickListener {
                val DishDetailWindows = Intent(mContext, CalendarioDetalle::class.java)
                DishDetailWindows.putExtra("id", calendarioItem.id)
                DishDetailWindows.putExtra("start_date", calendarioItem.start_date)
                DishDetailWindows.putExtra("end_date", calendarioItem.end_date)
                DishDetailWindows.putExtra("name", calendarioItem.name)
                DishDetailWindows.putExtra("article", calendarioItem.article)
                DishDetailWindows.putExtra("first_moon_cycle", calendarioItem.first_moon_cycle)
                DishDetailWindows.putExtra("second_moon_cycle", calendarioItem.second_moon_cycle)
                DishDetailWindows.putExtra("third_moon_cycle", calendarioItem.third_moon_cycle)
                DishDetailWindows.putExtra("four_moon_cycle", calendarioItem.four_moon_cycle)
                DishDetailWindows.putExtra("color", calendarioItem.color)
                DishDetailWindows.putExtra("title", calendarioItem.title)
                mContext.startActivity(DishDetailWindows)
            })

        } else {
            (holder as ProgressViewHolder).progressBar!!.setIndeterminate(true)

            //progressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));

        }




    }

    override fun getItemCount(): Int {
        return listCalendario.size
    }

    fun setDrawableFilterColor(context: Context, colorResource: Int, drawable: Drawable) {

        //val filterColor = Color.parseColor(context.resources.getString(colorResource))
        drawable.colorFilter = PorterDuffColorFilter(colorResource, PorterDuff.Mode.MULTIPLY)

    }
}
