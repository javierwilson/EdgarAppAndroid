package org.lwr.edgar

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.os.StrictMode
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import org.lwr.edgar.adapter.AdapterDocumentos
import org.lwr.edgar.models.documentosM
import java.util.ArrayList
import android.support.v7.widget.DividerItemDecoration
import android.widget.Toast
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import org.lwr.edgar.database.ArchivosDB
import java.io.File


class Documentos : AppCompatActivity() {
    lateinit var toolbar: Toolbar

    lateinit var recyclerCalendario: RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    lateinit var dialogDocumentos: ACProgressFlower
    companion object {
        lateinit var ArchivosData: ArchivosDB
        lateinit var context: Activity
        internal var listDocumentos: MutableList<documentosM> = ArrayList<documentosM>()
        private var adapter: AdapterDocumentos? = null

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.documentos)
        context = this;

        inits()


    }

    private fun inits() {
        ArchivosData = ArchivosDB(context!!)


        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

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


        loadDocumentos(true)

        //adapter!!.notifyDataSetChanged()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish() // close this activity as oppose to navigating up

        return false
    }


    object recargar {
        fun recargaFila(posicion: Int,file_type:String) {
            listDocumentos[posicion].descargado = true
            listDocumentos[posicion].formato = file_type
            adapter!!.notifyDataSetChanged()


            val builder: AlertDialog.Builder
            builder = AlertDialog.Builder(context)
            builder.setTitle("Edgar App")
                    .setMessage("¿Desea abrir el archivo?")
                    .setPositiveButton("Si", DialogInterface.OnClickListener { dialog, which ->

                        var urlArchivo = ArchivosData.getArchivoByArchivo(listDocumentos[posicion].id)
                        var nombreArchivo = ""
                        if (urlArchivo.moveToFirst())
                            do {
                                nombreArchivo = urlArchivo.getString(urlArchivo.getColumnIndex(ArchivosDB.name))
                            } while (urlArchivo.moveToNext())



                        val separated = nombreArchivo.split(".")
                        var formato = ""
                        if (separated.size > 0) {
                            formato = separated[separated.size-1];
                        }

                        listDocumentos[posicion].formato = formato

                        var nuevaCarpeta = File(Environment.getExternalStorageDirectory(), Constants.nameFolder);
                        nuevaCarpeta.mkdirs();

                        var file = File(nuevaCarpeta, nombreArchivo)
                        if (file.exists()) {
                            var path = Uri.fromFile(file)
                            try {
                                var intent = Intent(Intent.ACTION_VIEW)
                                if(formato.toLowerCase().toString().equals("pdf")){
                                    intent.setDataAndType(path, "application/pdf")
                                }else if(formato.toLowerCase().toString().equals("")){
                                    intent.setDataAndType(path, "application/epub+zip")
                                }
                                //intent.setDataAndType(path, "application/pdf");
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context!!.startActivity(intent);
                                //finish();
                            } catch (e: ActivityNotFoundException) {

                                if(formato.toLowerCase().toString().equals("pdf")){
                                    var toast1 = Toast.makeText(Documentos.context,"La aplicación PDF Reader no está instalada en su dispositivo", Toast.LENGTH_SHORT);
                                    toast1.show()
                                }else if(formato.toLowerCase().toString().equals("")){
                                    var toast1 = Toast.makeText(Documentos.context,"Necesitas instalar una aplicacion para leer archivo Epub", Toast.LENGTH_SHORT);
                                    toast1.show()
                                }


                            }
                        } else {
                            var toast1 = Toast.makeText(context,"Lo sentimos, el archivo no existe. Inténtelo descargándolo de nuevo", Toast.LENGTH_SHORT);
                            toast1.show();
                        }


                    })
                    .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->
                        // do nothing
                    })
                    .setIcon(R.mipmap.ic_launcher)
                    .show()
        }
    }

    fun loadDocumentos(load: Boolean){
        if(load==true){
            dialogDocumentos = ACProgressFlower.Builder(context)
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .themeColor(Color.WHITE)
                    .text("Cargando")
                    .fadeColor(Color.DKGRAY).build()

            dialogDocumentos.setCancelable(false)
            dialogDocumentos.show()
        }


        var requestQueue = Volley.newRequestQueue(context)

        val jsonObjRequestHome = object : StringRequest(
                Request.Method.GET,
                Constants.URLDocumentos,
                Response.Listener { response ->

                    dialogDocumentos.dismiss()
                    val res = JSONObject(response)
                    var documents = res.getJSONArray("documents")

                    for(i in 0..documents.length()-1) {
                        val itemsDocumentos = documents.getJSONObject(i)
                        val obtenerRegistro = ArchivosData.getArchivoByArchivo(itemsDocumentos.getString("id"))

                        var descargado = false
                        var nombreArchivo = ""


                        if(obtenerRegistro.count>0){
                            descargado = true
                            if (obtenerRegistro.moveToFirst())
                                do {
                                    nombreArchivo = obtenerRegistro.getString(obtenerRegistro.getColumnIndex(ArchivosDB.name))
                                } while (obtenerRegistro.moveToNext())
                        }

                        val separated = nombreArchivo.split(".")
                        var formato = ""
                        if (separated.size > 0) {
                            formato = separated[separated.size-1];
                        }



                        listDocumentos.add(documentosM(itemsDocumentos.getString("id"), itemsDocumentos.getString("name"),itemsDocumentos.getString("file"),descargado,formato))
                    }

                    adapter!!.notifyDataSetChanged()

                    System.out.println("response "+response)

                }, Response.ErrorListener { error ->


            VolleyLog.d("Error", "Error: " + error.message)

            if(load==true){
                dialogDocumentos.dismiss()
            }else{
                //swipeRefreshLayoutApp.isRefreshing = false
            }
        }
        ) {
        }

        // Añadir petición a la cola
        requestQueue!!.add(jsonObjRequestHome)
    }


}
