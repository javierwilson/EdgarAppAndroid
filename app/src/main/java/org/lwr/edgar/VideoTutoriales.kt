package org.lwr.edgar

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.app.Activity
import android.graphics.Color
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import java.util.ArrayList
import android.support.v7.widget.DividerItemDecoration
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import org.lwr.edgar.adapter.AdapterVideo
import org.lwr.edgar.models.videoTutorialM


class VideoTutoriales : AppCompatActivity() {
    lateinit var toolbar: Toolbar

    lateinit var recyclerVideoTutorial: RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    lateinit var dialogVideoTutoriales: ACProgressFlower
    
    companion object {
        lateinit var context: Activity
        internal var listVideoT: MutableList<videoTutorialM> = ArrayList<videoTutorialM>()
        private var adapter: AdapterVideo? = null

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.videotutoriales)
        context = this;

        inits()


    }

    private fun inits() {

        toolbar = findViewById(R.id.my_awesome_toolbar) as Toolbar
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true);
        //toolbar.inflateMenu(R.menu.drawer);



        listVideoT.clear()
        adapter = AdapterVideo(context, listVideoT)
        layoutManager = LinearLayoutManager(context)
        recyclerVideoTutorial = findViewById(R.id.recyclerVideoTutorial) as RecyclerView

        recyclerVideoTutorial.layoutManager = layoutManager
        recyclerVideoTutorial.itemAnimator = DefaultItemAnimator()
        recyclerVideoTutorial.adapter = adapter
        recyclerVideoTutorial.addItemDecoration(DividerItemDecoration(recyclerVideoTutorial.getContext(), DividerItemDecoration.VERTICAL))


        loadVideos(true)

        //adapter!!.notifyDataSetChanged()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish() // close this activity as oppose to navigating up

        return false
    }

    fun loadVideos(load: Boolean){
        if(load==true){
            dialogVideoTutoriales = ACProgressFlower.Builder(context)
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .themeColor(Color.WHITE)
                    .text("Cargando")
                    .fadeColor(Color.DKGRAY).build()

            dialogVideoTutoriales.setCancelable(false)
            dialogVideoTutoriales.show()
        }


        var requestQueue = Volley.newRequestQueue(context)

        val jsonObjRequestHome = object : StringRequest(
                Request.Method.GET,
                Constants.URLTutorial,
                Response.Listener { response ->

                    dialogVideoTutoriales.dismiss()
                    val res = JSONObject(response)
                    var tutorials = res.getJSONArray("tutorials")

                    for(i in 0..tutorials.length()-1) {
                        val itemsVideos = tutorials.getJSONObject(i)

                        listVideoT.add(videoTutorialM(itemsVideos.getString("id"), itemsVideos.getString("name"),itemsVideos.getString("video_link")))
                    }

                    adapter!!.notifyDataSetChanged()

                    System.out.println("response "+response)

                }, Response.ErrorListener { error ->


            VolleyLog.d("Error", "Error: " + error.message)

            if(load==true){
                dialogVideoTutoriales.dismiss()
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
