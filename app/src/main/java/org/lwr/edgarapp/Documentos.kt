package org.lwr.edgarapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.app.Activity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import org.lwr.edgarapp.adapter.AdapterDocumentos
import org.lwr.edgarapp.models.documentosM
import java.util.ArrayList
import android.support.v7.widget.DividerItemDecoration




class Documentos : AppCompatActivity() {
    lateinit var toolbar: Toolbar

    lateinit var recyclerCalendario: RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    internal var listDocumentos: MutableList<documentosM> = ArrayList<documentosM>()
    private var adapter: AdapterDocumentos? = null
    lateinit var context: Activity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.documentos)
        context = this;

        inits()


    }

    private fun inits() {
        toolbar = findViewById(R.id.my_awesome_toolbar) as Toolbar
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true);
        //toolbar.inflateMenu(R.menu.drawer);



        listDocumentos.clear()
        adapter = AdapterDocumentos(context, listDocumentos)
        layoutManager = LinearLayoutManager(context)
        recyclerCalendario = findViewById(R.id.recyclerDocumento) as RecyclerView

        recyclerCalendario.layoutManager = layoutManager
        recyclerCalendario.itemAnimator = DefaultItemAnimator()
        recyclerCalendario.adapter = adapter
        recyclerCalendario.addItemDecoration(DividerItemDecoration(recyclerCalendario.getContext(), DividerItemDecoration.VERTICAL))


        listDocumentos.add(documentosM("1", "Catálogo de Cultivares de Cacao de Perú"))
        listDocumentos.add(documentosM("1", "Nutrición Integral y Poda Oportuna"))
        listDocumentos.add(documentosM("1", "El Potasio en los Suelos y su Rol en la Producción Agrícola"))
        listDocumentos.add(documentosM("1", "El Encalado de Suelo ácidos"))
        listDocumentos.add(documentosM("1", "Glosario del Cacao"))
        listDocumentos.add(documentosM("1", "Mejora en la caena de valor del cacao orgánico"))
        listDocumentos.add(documentosM("1", "Abono Orgánico. Manejo y uso en el cultivo de cacao"))
        listDocumentos.add(documentosM("1", "Catálogo de Cultivares de Cacao de Perú"))
        listDocumentos.add(documentosM("1", "Nutrición Integral y Poda Oportuna"))
        listDocumentos.add(documentosM("1", "El Potasio en los Suelos y su Rol en la Producción Agrícola"))
        listDocumentos.add(documentosM("1", "El Encalado de Suelo ácidos"))
        listDocumentos.add(documentosM("1", "Glosario del Cacao"))
        listDocumentos.add(documentosM("1", "Mejora en la caena de valor del cacao orgánico"))
        listDocumentos.add(documentosM("1", "Abono Orgánico. Manejo y uso en el cultivo de cacao"))


        adapter!!.notifyDataSetChanged()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish() // close this activity as oppose to navigating up

        return false
    }

}
