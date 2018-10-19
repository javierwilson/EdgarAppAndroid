package org.lwr.edgarapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.app.Activity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import org.lwr.edgarapp.adapter.AdapterCalendario
import org.lwr.edgarapp.models.calendarioM
import java.util.ArrayList


class Calendario : AppCompatActivity() {
    lateinit var toolbar: Toolbar

    lateinit var recyclerCalendario: RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    internal var listCalendario: MutableList<calendarioM> = ArrayList<calendarioM>()
    private var adapter: AdapterCalendario? = null
    lateinit var context: Activity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calendario)
        context = this;

        inits()


    }

    private fun inits() {
        toolbar = findViewById(R.id.my_awesome_toolbar) as Toolbar
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true);
        //toolbar.inflateMenu(R.menu.drawer);



        listCalendario.clear()
        adapter = AdapterCalendario(context, listCalendario)
        layoutManager = LinearLayoutManager(context)
        recyclerCalendario = findViewById(R.id.recyclerCalendario) as RecyclerView

        recyclerCalendario.layoutManager = layoutManager
        recyclerCalendario.itemAnimator = DefaultItemAnimator()
        recyclerCalendario.adapter = adapter


        listCalendario.add(calendarioM("1", "Protección para el desarrollo y llenando de frutos jóvenes","Aplicar fungicidas la zona productiva y productores foliares con contenido de potasio para ganar tamaño de fruto y peso de semilla.",true,1,"","29 ene. - 28 mar."))
        listCalendario.add(calendarioM("1", "Tiempo de cosecha","Aplicar fungicidas la zona productiva y productores foliares con contenido de potasio para ganar tamaño de fruto y peso de semilla.",false,2,"","31 mar. - 29 sep."))
        listCalendario.add(calendarioM("1", "3er abonamiento. Aplicación de abonos.","Realizar la apertura del área de aplicación, aplicar el abono y tapar el área abonada.",true,3,"Productiva","30 abr. -19 jun."))
        listCalendario.add(calendarioM("1", "Apertura de calles - 3era poda","Realizar la apertura del área de aplicación, aplicar el abono y tapar el área abonada.",true,4,"","29 abr. -19 jun."))
        listCalendario.add(calendarioM("1", "Protección para el desarrollo y llenando de frutos jóvenes","Aplicar fungicidas la zona productiva y productores foliares con contenido de potasio para ganar tamaño de fruto y peso de semilla.",true,1,"","29 ene. - 28 mar."))
        listCalendario.add(calendarioM("1", "Tiempo de cosecha","Aplicar fungicidas la zona productiva y productores foliares con contenido de potasio para ganar tamaño de fruto y peso de semilla.",false,2,"","31 mar. - 29 sep."))
        listCalendario.add(calendarioM("1", "3er abonamiento. Aplicación de abonos.","Realizar la apertura del área de aplicación, aplicar el abono y tapar el área abonada.",true,3,"Productiva","30 abr. -19 jun."))
        listCalendario.add(calendarioM("1", "Apertura de calles - 3era poda","Realizar la apertura del área de aplicación, aplicar el abono y tapar el área abonada.",true,4,"","29 abr. -19 jun."))


        adapter!!.notifyDataSetChanged()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish() // close this activity as oppose to navigating up

        return false
    }

}
