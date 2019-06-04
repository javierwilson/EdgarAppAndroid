package org.lwr.edgar

import `in`.galaxyofandroid.spinerdialog.OnSpinerItemClick
import `in`.galaxyofandroid.spinerdialog.SpinnerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.app.Activity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import org.lwr.edgar.adapter.AdapterCalendario
import org.lwr.edgar.models.calendarioM
import com.android.volley.toolbox.Volley
import com.android.volley.toolbox.StringRequest
import android.graphics.Color
import android.icu.util.Calendar
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.android.volley.*
import org.json.JSONObject
import org.lwr.edgar.utils.EndlessRecyclerOnScrollListener
import android.support.v4.os.HandlerCompat.postDelayed
import com.google.android.youtube.player.internal.e
import android.os.AsyncTask.execute
import android.os.AsyncTask
import android.os.Handler
import java.util.*


class Calendario : AppCompatActivity(),View.OnClickListener {


    lateinit var toolbar: Toolbar

    lateinit var recyclerCalendario: RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    internal var listCalendario: MutableList<calendarioM> = ArrayList<calendarioM>()
    private var adapter: AdapterCalendario? = null
    lateinit var context: Activity
    lateinit var dialogCalendario: ACProgressFlower
    lateinit var txYear: TextView
    //var calendar = Calendar.getInstance
    var year = 0
    lateinit var spinnerDialog: SpinnerDialog
    private var items = ArrayList<String>()
    lateinit var mostrarYear: RelativeLayout
    var pagina = 0
    var activarPaginacion = false
    lateinit var endlessRecyclerOnScrollListener:EndlessRecyclerOnScrollListener

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

        activarPaginacion = false

        mostrarYear = findViewById(R.id.mostrarYear) as RelativeLayout
        mostrarYear.setOnClickListener(this)

        listCalendario.clear()
        adapter = AdapterCalendario(context, listCalendario)
        layoutManager = LinearLayoutManager(context)
        recyclerCalendario = findViewById(R.id.recyclerCalendario) as RecyclerView

        recyclerCalendario.layoutManager = layoutManager
        recyclerCalendario.itemAnimator = DefaultItemAnimator()
        recyclerCalendario.adapter = adapter

        endlessRecyclerOnScrollListener = object : EndlessRecyclerOnScrollListener(layoutManager) {
          override fun onLoadMore(current_page: Int) {

              if(activarPaginacion==true){
                  listCalendario.add(calendarioM())
                  adapter!!.notifyItemInserted(listCalendario.size)

                  pagina = pagina + 1
                  loadCalendario(false,pagina)
              }

              println("load more")
          }

        }



        recyclerCalendario?.addOnScrollListener(endlessRecyclerOnScrollListener)


        txYear = findViewById(R.id.txYear) as TextView
        val cal = Calendar.getInstance()
        year = cal.get(Calendar.YEAR)

        txYear.text = "$year"


        loadCalendario(true,pagina)
    }

    override fun onClick(v: View?) {
        val item_id = v!!.id
        when (item_id) {
            R.id.mostrarYear -> {
                spinnerDialog.showSpinerDialog()
            }

        }
    }

    fun loadCalendario(load: Boolean,pag:Int){
        activarPaginacion = false
        if(load==true){
            dialogCalendario = ACProgressFlower.Builder(context)
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .themeColor(Color.WHITE)
                    .text("Cargando")
                    .fadeColor(Color.DKGRAY).build()

            dialogCalendario.setCancelable(false)
            dialogCalendario.show()

            listCalendario.clear()
            endlessRecyclerOnScrollListener!!.reset(0, true)
        }

        var requestQueue = Volley.newRequestQueue(context)
        var urlFinal = Constants.URLCalendario

        urlFinal = "$urlFinal?paginate=$pag&year=$year"

        System.out.println("urlFinal $urlFinal")


        val jsonObjRequestHome = object : StringRequest(
                Request.Method.GET,
                urlFinal,
                Response.Listener { response ->
                    if(load==true){
                        dialogCalendario.dismiss()
                    }else{
                        listCalendario.removeAt(listCalendario.size - 1)
                        adapter!!.notifyItemRemoved(listCalendario.size)
                    }

                    val res = JSONObject(response)
                    var events = res.getJSONArray("events")
                    var years = res.getJSONArray("years")

                    for(i in 0..events.length()-1) {
                        val itemsEvents = events.getJSONObject(i)
                        listCalendario.add(calendarioM(
                                itemsEvents.getString("id"),
                                itemsEvents.getString("title"),
                                itemsEvents.getString("description"),
                                itemsEvents.getInt("first_moon_cycle"),
                                itemsEvents.getInt("second_moon_cycle"),
                                itemsEvents.getInt("third_moon_cycle"),
                                itemsEvents.getInt("quarter_moon_cycle"),
                                itemsEvents.getString("article"),
                                itemsEvents.getString("start_date"),
                                itemsEvents.getString("end_date"),
                                itemsEvents.getString("name"),
                                itemsEvents.getString("color")))

                    }





                    adapter!!.notifyDataSetChanged()

                    if(load==true){
                        items.clear()

                        for(j in 0..years.length()-1) {
                            items.add(years.getString(j))

                        }

                        spinnerDialog = SpinnerDialog(context, items, "Seleccione Año", R.style.DialogAnimations_SmileWindow, "Cerrar")// With 	Animation

                        spinnerDialog.bindOnSpinerListener(object : OnSpinerItemClick {
                            override fun onClick(p0: String?, position: Int) {
                                //Toast.makeText(context, p0 + "  " + p1+"", Toast.LENGTH_SHORT).show();
                                //pais = listCountry.get(position).id
                                txYear.text = items[position]
                                Constants.cerrarTeclado(context)
                                year = items[position].toInt()

                                pagina = 0
                                loadCalendario(true,pagina)
                            }

                        })

                        layoutManager.scrollToPositionWithOffset(0, 0)

                    }

                    val handler = Handler()
                    val timer = Timer()

                    val task = object : TimerTask() {
                        override fun run() {
                            handler.post(Runnable {
                                activarPaginacion = true
                            })
                        }

                    }

                    timer.schedule(task, 0, 1000)



                }, Response.ErrorListener { error ->


            VolleyLog.d("Error", "Error: " + error.message)

            if(load==true){
                dialogCalendario.dismiss()
            }else{
                //swipeRefreshLayoutApp.isRefreshing = false
            }
        }
        ) {
        }

        // Añadir petición a la cola
        requestQueue!!.add(jsonObjRequestHome)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish() // close this activity as oppose to navigating up

        return false
    }

}
